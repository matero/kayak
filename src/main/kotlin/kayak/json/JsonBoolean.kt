package kayak.json

enum class JsonBoolean : Json {
  TRUE {
    override fun asString() = "true"
    override fun asBoolean() = true
    override fun isTrue() = true
    override fun isFalse() = false
    override fun asBooleanOrElse(defaultTo: Boolean) = true
    override fun asNullableBoolean() = true
    override fun toString() = "JsonBoolean{true}"
  },
  FALSE {
    override fun asString() = "false"
    override fun asBoolean() = false
    override fun isTrue() = false
    override fun isFalse() = true
    override fun asBooleanOrElse(defaultTo: Boolean) = false
    override fun asNullableBoolean() = false
    override fun toString() = "JsonBoolean{false}"
  };

  override fun type(): Json.NodeType = Json.NodeType.BOOLEAN

  override fun isBoolean() = true

  override fun isNullableBoolean() = true
}
