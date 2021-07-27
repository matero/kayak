package kayak.web

sealed interface MediaType {
  val mimeName: String
  val template: String

  fun matches(value: String): Boolean


  fun definedAt(value: String): Boolean

  object Any : MediaType {
    override val mimeName: String = "any"
    override val template: String = "*/*"

    override fun matches(value: String): Boolean = true

    override fun definedAt(value: String): Boolean {
      return when (value.length) {
        0, 1, 2 -> false
        3 -> check(value)
        else -> check(value) && identified(value, 3)
      }
    }

    internal fun check(value: String) = value[0].code == '*'.code && value[1].code == '/'.code && value[2].code == '*'.code
  }

  companion object {
    fun of(id: String): MediaType {
      if (id.isEmpty()) {
        throw IllegalArgumentException("id can not be empty")
      }
      if (id.isBlank()) {
        throw IllegalArgumentException("id can not be blank")
      }
      return when (id) {
        "*/*" -> Any
        "application/json" -> ApplicationMediaType.APPLICATION_JSON
        else -> throw IllegalArgumentException("unknown content type '$id'") // TODO: implement ContentType on-the-fly
      }
    }

    internal fun identified(s: String, at: Int): Boolean {
      val c = ApplicationMediaType.nextNonWhiteSpaceAt(s, at)
      return c == ','.code || c == ';'.code || c == '\n'.code
    }
  }
}

internal enum class ApplicationMediaType(override val mimeName: String, override val template: String) : MediaType {
  APPLICATION_JSON(mimeName = "json", template = "application/json") {
    override fun check(value: String): Boolean =
      value[12].code == 'j'.code
            && value[13].code == 's'.code
            && value[14].code == 'o'.code
            && value[15].code == 'n'.code
  },
  APPLICATION_XML(mimeName = "xml", template = "application/xml") {
    override fun check(value: String): Boolean = value[12].code == 'x'.code && value[13].code == 'm'.code && value[14].code == 'l'.code
  };

  override fun matches(value: String): Boolean = definedAt(value)

  override fun definedAt(value: String): Boolean {
    return when (value.length) {
      0, 1, 2 -> false
      3 -> MediaType.Any.check(value)
      in 4 until template.length + mimeName.length -> MediaType.Any.definedAt(value)
      else ->
        when (value[0].code) {
          '*'.code -> isAny(value)
          template[0].code -> isApplication(value) && check(value) && MediaType.identified(value, at = template.length + mimeName.length
          )
          else -> false
        }
    }
  }

  internal abstract fun check(value: String): Boolean

  companion object {
    internal fun isAny(value: String) =
      value[1].code == '/'.code && value[2].code == '*'.code && MediaType.identified(value, 3)

    internal fun isApplication(value: String) =
      value[1].code == 'p'.code
        && value[2].code == 'p'.code
        && value[3].code == 'l'.code
        && value[4].code == 'i'.code
        && value[5].code == 'c'.code
        && value[6].code == 'a'.code
        && value[7].code == 't'.code
        && value[8].code == 'i'.code
        && value[9].code == 'o'.code
        && value[10].code == 'n'.code
        && value[11].code == '/'.code

    internal fun nextNonWhiteSpaceAt(s: String, startingAt: Int): Int {
      if (s.length > startingAt) {
        for (i in startingAt..s.length) {
          if (!s[i].isWhitespace()) {
            return s[i].code
          }
        }
      }
      return '\n'.code
    }
  }
}
