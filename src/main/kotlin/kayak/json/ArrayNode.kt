package kayak.json

@JvmInline
value class ArrayNode private constructor(private val elements: List<Json>) : Json {
  override fun isArray() = true

  override fun asArray(): List<Json> = elements

  override fun asArrayOrElse(defaultTo: List<Json>) = elements

  override fun isNullableArray(): Boolean = true

  override fun asNullableArray(): List<Json> = elements

  override fun asBoolean() = throw IllegalJsonInterpretation("Json node is array")

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

  override fun asObjectOrElse(defaultTo: Map<StringValue, Json>) = throw IllegalJsonInterpretation("Json node is array")

  override fun asNullableObject() = throw IllegalJsonInterpretation("Json node is array")

  override fun toString(): String {
    return "ArrayNode{elements=$elements}"
  }

  companion object {
    private val EMPTY_ARRAY = ArrayNode(emptyList())

    operator fun invoke() = EMPTY_ARRAY

    operator fun invoke(elements: Iterator<Json>) =
      if (elements.hasNext())
        invoke(elements.asSequence().toList())
      else
        EMPTY_ARRAY

    operator fun invoke(elements: Iterable<Json>): ArrayNode =
      when (elements) {
        is List<Json> -> invoke(elements)
        is Collection<Json> -> if (elements.isEmpty()) EMPTY_ARRAY else ArrayNode(elements.toList())
        else -> invoke(elements.toList())
      }

    operator fun invoke(vararg elements: Json) = invoke(listOf(*elements))

    operator fun invoke(elementList: List<Json>) =
      if (elementList.isEmpty()) EMPTY_ARRAY else ArrayNode(elementList.toList())
  }
}
