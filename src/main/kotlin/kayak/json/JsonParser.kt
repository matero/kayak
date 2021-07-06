package kayak.json

import kayak.KayakFailure
import java.io.Reader
import java.io.StringReader

/**
 * An immutable object that represents a location in the parsed text.
 */
data class Location(
  /**
   * The absolute character index, starting at 0.
   */
  val offset: Int,
  /**
   * the line number, starting at 1
   */
  val line: Int,
  /**
   * The column number, starting at 1.
   */
  val column: Int
)

/**
 * A streaming parser for JSON text, based on the one defined at [minimal-json](https://github.com/ralfstx/minimal-json), without event handling.
 */
internal class JsonParser(private val input: Reader, private val buffer: CharArray) {
  /*
   * |                      bufferOffset
   *                        v
   * [a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t]        < input
   *                       [l|m|n|o|p|q|r|s|t|?|?]    < buffer
   *                          ^               ^
   *                       |  index           fill
   */

  private var bufferOffset: Int = 0
  private var index: Int = 0
  private var fill: Int = 0
  private var line: Int = 1
  private var lineOffset: Int = 0
  private var current: Int = 0
  private var captureBuffer = StringBuilder()
  private var captureStart: Int = -1
  private var nestingLevel: Int = 0


  /**
   * Reads the entire input from the given reader and parses it as JSON. The input must contain a
   * valid JSON value, optionally padded with whitespace.
   *
   * Characters are read in chunks into a default-sized input buffer. Hence, wrapping a reader in an
   * additional `BufferedReader` likely won't improve reading performance.
   *
   * @throws IllegalJsonSyntax if the input is not valid JSON
   */
  fun parse(): Json {
    read()
    skipWhiteSpace()
    val json = readValue()
    skipWhiteSpace()
    if (!isEndOfText(current)) {
      throw error("Unexpected character")
    }
    return json
  }

  private fun read() {
    if (index == fill) {
      if (captureStart != -1) {
        captureBuffer.append(buffer, captureStart, fill - captureStart)
        captureStart = 0
      }
      bufferOffset += fill
      fill = input.read(buffer, 0, buffer.size)
      if (fill == -1) {
        current = -1
        index = 1
        return
      } else {
        index = 0
      }
    }
    if (current == '\n'.code) {
      line++
      lineOffset = bufferOffset + index
    }
    current = buffer[index++].code
  }

  private fun skipWhiteSpace() {
    while (isWhitespace(current)) {
      read()
    }
  }

  private fun readValue(): Json {
    return when (current) {
      'n'.code -> readNull()
      't'.code -> readTrue()
      'f'.code -> readFalse()
      '"'.code -> readString()
      '['.code -> readArray()
      '{'.code -> readObject()
      '-'.code, in '0'.code..'9'.code -> readNumber()
      else -> throw expected("value")
    }
  }

  private fun readNull(): JsonNull {
    read()
    readRequiredChar('u')
    readRequiredChar('l')
    readRequiredChar('l')
    return JsonNull
  }

  private fun readTrue(): JsonBoolean {
    read()
    readRequiredChar('r')
    readRequiredChar('u')
    readRequiredChar('e')
    return JsonBoolean.TRUE
  }

  private fun readFalse(): JsonBoolean {
    read()
    readRequiredChar('a')
    readRequiredChar('l')
    readRequiredChar('s')
    readRequiredChar('e')
    return JsonBoolean.FALSE
  }

  private fun readRequiredChar(ch: Char) {
    if (!readChar(ch)) {
      throw expected("'$ch'")
    }
  }

  private fun readString(): JsonString {
    return JsonString.of(readStringInternal())
  }

  private fun readArray(): Json {
    if (++nestingLevel > MAX_NESTING_LEVEL) {
      throw error("Nesting too deep")
    }

    read()
    skipWhiteSpace()
    if (readChar(']')) {
      nestingLevel--
      return JsonArray.of()
    }

    val elements = ArrayList<Json>(5)
    do {
      skipWhiteSpace()
      elements.add(readValue())
      skipWhiteSpace()
    } while (readChar(','))
    if (!readChar(']')) {
      throw expected("',' or ']'")
    }
    nestingLevel--
    return JsonArray.of(elements)
  }

  private fun readObject(): Json {
    if (++nestingLevel > MAX_NESTING_LEVEL) {
      throw error("Nesting too deep")
    }

    read()
    skipWhiteSpace()
    if (readChar('}')) {
      nestingLevel--
      return JsonObject.of()
    }

    val properties = LinkedHashMap<JsonString, Json>()
    do {
      skipWhiteSpace()
      val name = readName()
      skipWhiteSpace()
      if (!readChar(':')) {
        throw expected("':'")
      }
      skipWhiteSpace()
      properties[name] = readValue()
      skipWhiteSpace()
    } while (readChar(','))

    if (!readChar('}')) {
      throw expected("',' or '}'")
    }
    nestingLevel--

    return JsonObject(properties)
  }

  private fun readName(): JsonString {
    if (current != '"'.code) {
      throw expected("name")
    }
    return JsonString.of(readStringInternal())
  }

  private fun readStringInternal(): String {
    read()
    startCapture()
    while (current != '"'.code) {
      when {
        current == '\\'.code -> {
          pauseCapture()
          readEscape()
          startCapture()
        }
        current < 0x20 -> {
          throw expected("valid string character")
        }
        else -> {
          read()
        }
      }
    }
    val value = endCapture()
    read()
    return value
  }

  private fun readEscape() {
    read()
    when (current) {
      '"'.code, '/'.code, '\\'.code -> captureBuffer.append(current.toChar())
      'b'.code -> captureBuffer.append('\b')
      'f'.code -> captureBuffer.append('\u000C') // kotlin needs unicode for \f char :O
      'n'.code -> captureBuffer.append('\n')
      'r'.code -> captureBuffer.append('\r')
      't'.code -> captureBuffer.append('\t')
      'u'.code -> {
        val hexChars = CharArray(4)
        var i = 0
        while (i < 4) {
          read()
          if (!isHexDigit(current)) {
            throw expected("hexadecimal digit")
          }
          hexChars[i] = current.toChar()
          i++
        }
        captureBuffer.append(String(hexChars).toInt(16).toChar())
      }
      else -> throw expected("valid escape sequence")
    }
    read()
  }

  private fun readNumber(): JsonNumber {
    startCapture()
    readChar('-')
    val firstDigit = current
    if (!readDigit()) {
      throw expected("digit")
    }
    if (firstDigit != '0'.code) {
      while (readDigit()) {
      }
    }
    readFraction()
    readExponent()
    return JsonNumber.of(endCapture())
  }

  private fun readFraction(): Boolean {
    if (!readChar('.')) {
      return false
    }
    if (!readDigit()) {
      throw expected("digit")
    }
    while (readDigit()) {
    }
    return true
  }

  private fun readExponent(): Boolean {
    if (!readChar('e') && !readChar('E')) {
      return false
    }
    if (!readChar('+')) {
      readChar('-')
    }
    if (!readDigit()) {
      throw expected("digit")
    }
    while (readDigit()) {
    }
    return true
  }

  private fun readChar(ch: Char): Boolean {
    if (current != ch.code) {
      return false
    }
    read()
    return true
  }

  private fun readDigit(): Boolean {
    if (!isDigit(current)) {
      return false
    }
    read()
    return true
  }

  private fun startCapture() {
    captureStart = index - 1
  }

  private fun pauseCapture() {
    val end = if (current == -1) index else index - 1
    captureBuffer.append(buffer, captureStart, end - captureStart)
    captureStart = -1
  }

  private fun endCapture(): String {
    val start = captureStart
    val end = index - 1
    captureStart = -1
    if (captureBuffer.isNotEmpty()) {
      captureBuffer.append(buffer, start, end - start)
      val captured = captureBuffer.toString()
      captureBuffer.setLength(0)
      return captured
    }
    return String(buffer, start, end - start)
  }

  private fun location(): Location {
    val offset = bufferOffset + index - 1
    val column = offset - lineOffset + 1
    return Location(offset, line, column)
  }

  private fun expected(expected: String): IllegalJsonSyntax {
    if (isEndOfText(current)) {
      return error("Unexpected end of input")
    }
    return error("Expected $expected")
  }

  private fun error(message: String): IllegalJsonSyntax {
    return IllegalJsonSyntax(message, location())
  }

  class IllegalJsonSyntax internal constructor(message: String, location: Location) : KayakFailure("${location}: $message")

  companion object {
    private const val MAX_NESTING_LEVEL: Int = 1000
    private const val MIN_BUFFER_CAPACITY: Int = 10
    private val START = Location(offset = 0, line = 1, column = 1)
    const val DEFAULT_BUFFER_CAPACITY: Int = 1024

    /**
     * Parses the given input string. The input must contain a valid JSON value, optionally padded
     * with whitespace.
     *
     * @param input
     * the input string, must be valid JSON
     * @throws IllegalJsonSyntax
     * if the input is not valid JSON
     */
    fun of(input: String, desiredBufferCapacity: Int): JsonParser {
      if (input.isEmpty() || input.isBlank()) {
        // simulates parsing, avoid reader and buffer creation
        throw IllegalJsonSyntax("Unexpected end of input", START)
      }
      return JsonParser(StringReader(input), bufferWith(desiredBufferCapacity))
    }

    fun of(input: Reader, desiredBufferCapacity: Int) = JsonParser(input, bufferWith(desiredBufferCapacity))

    private fun bufferWith(desiredBufferCapacity: Int) = CharArray(coerceBufferCapacity(desiredBufferCapacity))

    private fun coerceBufferCapacity(bufferCapacity: Int): Int {
      if (bufferCapacity <= 0) {
        throw IllegalArgumentException("buffer capacity must be greater than zero")
      }
      return MIN_BUFFER_CAPACITY.coerceAtLeast(DEFAULT_BUFFER_CAPACITY.coerceAtMost(bufferCapacity))
    }

    private fun isWhitespace(ch: Int): Boolean = (Character.isWhitespace(ch) || Character.isSpaceChar(ch))

    private fun isEndOfText(ch: Int): Boolean = ch == -1

    private fun isDigit(ch: Int): Boolean = ch in '0'.code..'9'.code

    private fun isHexDigit(ch: Int): Boolean = (ch in '0'.code..'9'.code) || (ch in 'a'.code..'f'.code) || (ch in 'A'.code..'F'.code)
  }
}
