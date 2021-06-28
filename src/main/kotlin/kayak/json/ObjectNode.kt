package kayak.json

import java.math.BigDecimal
import java.math.BigInteger

@JvmInline
value class ObjectNode(private val fields: Map<StringValue, Json>) : Json {
  override fun isObject() = true

  override fun asObject() = fields

  override fun asObjectOrElse(defaultTo: Map<StringValue, Json>) = fields

  override fun isNullableObject() = true

  override fun asNullableObject() = fields

  override fun asBoolean() = throw IllegalJsonInterpretation("Json node is object")

  override fun asBooleanOrElse(defaultTo: Boolean) = throw IllegalJsonInterpretation("Json node is object")

  override fun asNullableBoolean() = throw IllegalJsonInterpretation("Json node is object")

  override fun asString() = throw IllegalJsonInterpretation("Json node is object")

  override fun asStringOrElse(defaultTo: String) = throw IllegalJsonInterpretation("Json node is object")

  override fun asNullableString() = throw IllegalJsonInterpretation("Json node is object")

  override fun asNumber() = throw IllegalJsonInterpretation("Json node is object")

  override fun asNumberOrElse(defaultTo: String) = throw IllegalJsonInterpretation("Json node is object")

  override fun asByte() = throw IllegalJsonInterpretation("Json node is object")

  override fun asByteOrElse(defaultTo: Byte) = throw IllegalJsonInterpretation("Json node is object")

  override fun asShort() = throw IllegalJsonInterpretation("Json node is object")

  override fun asShortOrElse(defaultTo: Short) = throw IllegalJsonInterpretation("Json node is object")

  override fun asInt() = throw IllegalJsonInterpretation("Json node is object")

  override fun asIntOrElse(defaultTo: Int) = throw IllegalJsonInterpretation("Json node is object")

  override fun asLong() = throw IllegalJsonInterpretation("Json node is object")

  override fun asLongOrElse(defaultTo: Long) = throw IllegalJsonInterpretation("Json node is object")

  override fun asBigInteger() = throw IllegalJsonInterpretation("Json node is object")

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = throw IllegalJsonInterpretation("Json node is object")

  override fun asFloat() = throw IllegalJsonInterpretation("Json node is object")

  override fun asFloatOrElse(defaultTo: Float) = throw IllegalJsonInterpretation("Json node is object")

  override fun asDouble() = throw IllegalJsonInterpretation("Json node is object")

  override fun asDoubleOrElse(defaultTo: Double) = throw IllegalJsonInterpretation("Json node is object")

  override fun asBigDecimal() = throw IllegalJsonInterpretation("Json node is object")

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = throw IllegalJsonInterpretation("Json node is object")

  override fun asNullableNumber() = throw IllegalJsonInterpretation("Json node is object")

  override fun asArray() = throw IllegalJsonInterpretation("Json node is object")

  override fun asArrayOrElse(defaultTo: List<Json>) = throw IllegalJsonInterpretation("Json node is object")

  override fun asNullableArray() = throw IllegalJsonInterpretation("Json node is object")

  override fun toString() = "ObjectNode{fields=$fields}"

  companion object {
    private val EMPTY_OBJECT: ObjectNode = ObjectNode(emptyMap())

    operator fun invoke(): ObjectNode = EMPTY_OBJECT

    operator fun invoke(field: Pair<StringValue, Json>): ObjectNode = ObjectNode(mapOf(field))

    operator fun invoke(vararg fields: Pair<StringValue, Json>): ObjectNode = if (fields.isEmpty()) EMPTY_OBJECT else ObjectNode(mapOf(*fields))

    operator fun invoke(fields: Collection<Pair<StringValue, Json>>): ObjectNode = if (fields.isEmpty()) EMPTY_OBJECT else ObjectNode(fields.toMap())

    operator fun invoke(fields: Map<StringValue, Json>): ObjectNode =
      if (fields.isEmpty())
        EMPTY_OBJECT
      else
        ObjectNode(fields.toMap())
  }
}
