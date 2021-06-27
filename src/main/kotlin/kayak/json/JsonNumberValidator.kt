package kayak.json

internal object JsonNumberValidator {
  private enum class ParserState(val isEndState: Boolean) {
    BEFORE_START(false) {
      override fun handle(character: Char): ParserState { // NOPMD TODO this should be turned off in the rules
        return when (character) {
          '-' -> NEGATIVE
          '0' -> ZERO
          '1', '2', '3', '4', '5', '6', '7', '8', '9' -> INTEGER_PART
          else -> FAILED
        }
      }
    },
    NEGATIVE(false) {
      override fun handle(character: Char): ParserState { // NOPMD TODO this should be turned off in the rules
        return when (character) {
          '0' -> ZERO
          '1', '2', '3', '4', '5', '6', '7', '8', '9' -> INTEGER_PART
          else -> FAILED
        }
      }
    },
    ZERO(true) {
      override fun handle(character: Char): ParserState {
        return when (character) {
          '.' -> DECIMAL_POINT
          'e', 'E' -> EXPONENT_MARKER
          else -> FAILED
        }
      }
    },
    INTEGER_PART(true) {
      override fun handle(character: Char): ParserState { // NOPMD TODO this should be turned off in the rules
        return when (character) {
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> INTEGER_PART
          '.' -> DECIMAL_POINT
          'e', 'E' -> EXPONENT_MARKER
          else -> FAILED
        }
      }
    },
    DECIMAL_POINT(false) {
      override fun handle(character: Char): ParserState { // NOPMD TODO this should be turned off in the rules
        return when (character) {
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> FRACTIONAL_PART
          else -> FAILED
        }
      }
    },
    FRACTIONAL_PART(true) {
      override fun handle(character: Char): ParserState { // NOPMD TODO this should be turned off in the rules
        return when (character) {
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> FRACTIONAL_PART
          'e', 'E' -> EXPONENT_MARKER
          else -> FAILED
        }
      }
    },
    EXPONENT_MARKER(false) {
      override fun handle(character: Char): ParserState { // NOPMD TODO this should be turned off in the rules
        return when (character) {
          '+', '-' -> EXPONENT_SIGN
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> EXPONENT
          else -> FAILED
        }
      }
    },
    EXPONENT_SIGN(false) {
      override fun handle(character: Char): ParserState { // NOPMD TODO this should be turned off in the rules
        return when (character) {
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> EXPONENT
          else -> FAILED
        }
      }
    },
    EXPONENT(true) {
      override fun handle(character: Char): ParserState { // NOPMD TODO this should be turned off in the rules
        return when (character) {
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> EXPONENT
          else -> FAILED
        }
      }
    },
    FAILED(false) {
      override fun handle(character: Char) = this
    };

    abstract fun handle(character: Char): ParserState
  }

  fun isValid(text: String): Boolean {
    var parserState = ParserState.BEFORE_START
    for (nextChar in text) {
      parserState = parserState.handle(nextChar)
      if (parserState === ParserState.FAILED)
        break
    }
    return parserState.isEndState
  }

  fun isValid(cbuf: CharArray, offset: Int, length: Int): Boolean {
    var i = offset
    var parserState = ParserState.BEFORE_START
    while (i < offset + length && i < cbuf.size) {
      parserState = parserState.handle(cbuf[i])
      if (parserState === ParserState.FAILED)
        break
      i++
    }
    return parserState.isEndState
  }
}
