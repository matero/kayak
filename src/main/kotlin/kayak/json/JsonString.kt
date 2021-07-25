package kayak.json

@JvmInline
value class JsonString private constructor(private val value: String) : Json {
  override fun type() = Json.NodeType.STRING

  override fun isString() = true

  override fun asString() = value

  override fun asStringOrElse(defaultTo: String) = value

  override fun isNullableString() = true

  override fun asNullableString() = value

  override fun toString() = """JsonString{"$value"}"""

  companion object {
    private val EMPTY = JsonString("")

    fun make(value: CharSequence) = if (value.isEmpty()) EMPTY else JsonString(value.toString())
  }
}
