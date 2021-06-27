package kayak.json

import java.io.IOException
import java.io.Writer

/**
 * A `JsonWriter` provides operations to stream valid JSON text to a `java.io.Writer`.
 */
sealed class JsonWriter {
  /**
   * Streams the specified `JsonNode` formatted to the specified `Writer`.
   *
   * @param to the `Writer` to output to.
   * @param node the `JsonNode` to output.
   * @throws IOException if there was a problem writing to the `Writer`.
   */
  abstract fun write(to: Writer, node: JsonNode)

  companion object {
    internal fun write(toWriter: Writer, text: String) {
      toWriter.write('"'.code)
      escape(toWriter, fromString = text)
      toWriter.write('"'.code)
    }

    internal fun escape(to: Writer, fromString: String) =
      escape(to, fromString.toCharArray(), 0, fromString.length)

    internal fun escape(to: Writer, fromChars: CharArray, withOffset: Int, andLength: Int) {
      if (withOffset < 0)
        throw IndexOutOfBoundsException("negative offset")
      if (withOffset > fromChars.size)
        throw IndexOutOfBoundsException("offset bigger than unescaped characters size")
      if (andLength < 0)
        throw IndexOutOfBoundsException("negative length")
      if (withOffset + andLength > fromChars.size)
        throw IndexOutOfBoundsException("'offset + length' is bigger than unescaped characters size")
      if (withOffset + andLength < 0)
        throw IllegalArgumentException("apalala!")

      if (andLength != 0) {
        for (i in withOffset until withOffset + andLength) {
          when (fromChars[i]) {
            '\u0000' -> to.append("\\u0000")
            '\u0001' -> to.append("\\u0001")
            '\u0002' -> to.append("\\u0002")
            '\u0003' -> to.append("\\u0003")
            '\u0004' -> to.append("\\u0004")
            '\u0005' -> to.append("\\u0005")
            '\u0006' -> to.append("\\u0006")
            '\u0007' -> to.append("\\u0007")
            '\u0008' -> to.append("\\b")
            '\u0009' -> to.append("\\t")
            '\n' -> to.append("\\n")
            '\u000b' -> to.append("\\u000b")
            '\u000c' -> to.append("\\f")
            '\r' -> to.append("\\r")
            '\u000e' -> to.append("\\u000e")
            '\u000f' -> to.append("\\u000f")
            '\u0010' -> to.append("\\u0010")
            '\u0011' -> to.append("\\u0011")
            '\u0012' -> to.append("\\u0012")
            '\u0013' -> to.append("\\u0013")
            '\u0014' -> to.append("\\u0014")
            '\u0015' -> to.append("\\u0015")
            '\u0016' -> to.append("\\u0016")
            '\u0017' -> to.append("\\u0017")
            '\u0018' -> to.append("\\u0018")
            '\u0019' -> to.append("\\u0019")
            '\u001a' -> to.append("\\u001a")
            '\u001b' -> to.append("\\u001b")
            '\u001c' -> to.append("\\u001c")
            '\u001d' -> to.append("\\u001d")
            '\u001e' -> to.append("\\u001e")
            '\u001f' -> to.append("\\u001f")
            '\\' -> to.append("\\\\")
            '\"' -> to.append("\\\"")
            else -> to.append(fromChars[i])
          }
        }
      }
    }
  }
}
