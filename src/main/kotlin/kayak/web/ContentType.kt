package kayak.web

import java.lang.IllegalArgumentException

sealed interface ContentType {
  val id: String

  fun matches(value: String): Boolean


  fun definedAt(value: String): Boolean

  object Any : ContentType {
    override val id: String = "*/*"

    override fun matches(value: String): Boolean = true

    override fun definedAt(value: String): Boolean {
      return when (value.length) {
        0, 1, 2 -> false
        3 -> check(value)
        else -> check(value) && isHeaderDefinitionComplete(value, 3)
      }
    }

    internal fun check(value: String) = value[0].code == '*'.code && value[1].code == '/'.code && value[2].code == '*'.code
  }

  companion object {
    fun of(id: String): ContentType {
      if (id.isEmpty()) {
        throw IllegalArgumentException("id can not be empty")
      }
      if (id.isBlank()) {
        throw IllegalArgumentException("id can not be blank")
      }
      return when (id) {
        "*/*" -> ContentType.Any
        "application/json" -> DefaultContentType.APPLICATION_JSON
        else -> throw IllegalArgumentException("unknown content type '$id'") // TODO: implement ContentType on-the-fly
      }
    }
  }
}

internal enum class DefaultContentType(override val id: String) : ContentType {
  APPLICATION_JSON("application/json") {
    override fun definedAt(value: String): Boolean {
      return when (value.length) {
        0, 1, 2 -> false
        3 -> ContentType.Any.check(value)
        in 4..14 -> ContentType.Any.definedAt(value)
        else -> checkDefinitionOf(value)
      }
    }

    private fun checkDefinitionOf(value: String): Boolean {
      return when (value[0].code) {
        '*'.code -> value[1].code == '/'.code && value[2].code == '*'.code && isHeaderDefinitionComplete(value, 3)
        'a'.code ->
          value[1].code == 'p'.code &&
          value[2].code == 'p'.code &&
          value[3].code == 'l'.code &&
          value[4].code == 'i'.code &&
          value[5].code == 'c'.code &&
          value[6].code == 'a'.code &&
          value[7].code == 't'.code &&
          value[8].code == 'i'.code &&
          value[9].code == 'o'.code &&
          value[10].code == 'n'.code &&
          value[11].code == '/'.code &&
          value[12].code == 'j'.code &&
          value[13].code == 's'.code &&
          value[14].code == 'o'.code &&
          value[15].code == 'n'.code &&
          isHeaderDefinitionComplete(value, 16)
        else -> false
      }
    }
  };

  override fun matches(value: String): Boolean = definedAt(value)
}

internal fun isHeaderDefinitionComplete(s: String, startingAt: Int): Boolean {
  val c = nextNonWhiteSpaceAt(s, startingAt)
  return c == ','.code || c == ';'.code || c == '\n'.code
}

internal fun nextNonWhiteSpaceAt(s: String, startingAt: Int): Int {
  if (s.length > startingAt) {
    for (i in startingAt..s.length) {
      if (!s[i].isWhitespace()) {
        return s[i].code;
      }
    }
  }
  return '\n'.code
}
