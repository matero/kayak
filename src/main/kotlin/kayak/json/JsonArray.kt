package kayak.json

@JvmInline
value class JsonArray private constructor(private val elements: List<Json>) : Json {
  override fun isArray() = true

  override fun asArray(): List<Json> = elements

  override fun asArrayOrElse(defaultTo: List<Json>) = elements

  override fun isNullableArray(): Boolean = true

  override fun asNullableArray(): List<Json> = elements

  override fun asBoolean() = throw IllegalJsonInterpretation("Json node is array")

  override fun isTrue() = throw IllegalJsonInterpretation("Json node is array")

  override fun isFalse() = throw IllegalJsonInterpretation("Json node is array")

  override fun asBooleanOrElse(defaultTo: Boolean) = throw IllegalJsonInterpretation("Json node is array")

  override fun asNullableBoolean() = throw IllegalJsonInterpretation("Json node is array")

  override fun asString() = throw IllegalJsonInterpretation("Json node is array")

  override fun asStringOrElse(defaultTo: String) = throw IllegalJsonInterpretation("Json node is array")

  override fun asNullableString() = throw IllegalJsonInterpretation("Json node is array")

  override fun asNumber() = throw IllegalJsonInterpretation("Json node is array")

  override fun asNumberOrElse(defaultTo: String) = throw IllegalJsonInterpretation("Json node is array")

  override fun asByte() = throw IllegalJsonInterpretation("Json node is array")

  override fun asByteOrElse(defaultTo: Byte) = throw IllegalJsonInterpretation("Json node is array")

  override fun asShort() = throw IllegalJsonInterpretation("Json node is array")

  override fun asShortOrElse(defaultTo: Short) = throw IllegalJsonInterpretation("Json node is array")

  override fun asInt() = throw IllegalJsonInterpretation("Json node is array")

  override fun asIntOrElse(defaultTo: Int) = throw IllegalJsonInterpretation("Json node is array")

  override fun asLong() = throw IllegalJsonInterpretation("Json node is array")

  override fun asLongOrElse(defaultTo: Long) = throw IllegalJsonInterpretation("Json node is array")

  override fun asBigInteger() = throw IllegalJsonInterpretation("Json node is array")

  override fun asBigIntegerOrElse(defaultTo: java.math.BigInteger) = throw IllegalJsonInterpretation("Json node is array")

  override fun asFloat() = throw IllegalJsonInterpretation("Json node is array")

  override fun asFloatOrElse(defaultTo: Float) = throw IllegalJsonInterpretation("Json node is array")

  override fun asDouble() = throw IllegalJsonInterpretation("Json node is array")

  override fun asDoubleOrElse(defaultTo: Double) = throw IllegalJsonInterpretation("Json node is array")

  override fun asBigDecimal() = throw IllegalJsonInterpretation("Json node is array")

  override fun asBigDecimalOrElse(defaultTo: java.math.BigDecimal) = throw IllegalJsonInterpretation("Json node is array")

  override fun asNullableNumber() = throw IllegalJsonInterpretation("Json node is array")

  override fun asObject() = throw IllegalJsonInterpretation("Json node is array")

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = throw IllegalJsonInterpretation("Json node is array")

  override fun asNullableObject() = throw IllegalJsonInterpretation("Json node is array")

  override fun toString(): String {
    return "ArrayNode{elements=$elements}"
  }

  companion object {
    private val EMPTY_ARRAY = JsonArray(emptyList())

    fun of(elements: Iterator<Json>) =
      if (elements.hasNext()) {
        of(elements.asSequence().toList())
      } else
        EMPTY_ARRAY

    fun of(elements: Iterable<Json>): JsonArray =
      when (elements) {
        is List<Json> -> of(elements)
        is Collection<Json> -> if (elements.isEmpty()) EMPTY_ARRAY else JsonArray(elements.toList())
        else -> of(elements.toList())
      }

    fun of(vararg elements: Json) = of(listOf(*elements))

    fun of(elements: List<Json>) =
      if (elements.isEmpty()) EMPTY_ARRAY else JsonArray(elements.toList())
  }
}
