package kayak.json

import java.math.BigDecimal
import java.math.BigInteger

@JvmInline
value class JsonObject(private val fields: Map<JsonString, Json>) : Json {
  override fun isObject() = true

  override fun asObject() = fields

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = fields

  override fun isNullableObject() = true

  override fun asNullableObject() = fields

  override fun asBoolean() = throw IllegalJsonInterpretation("Json node is object")

  override fun isTrue() = throw IllegalJsonInterpretation("Json node is object")

  override fun isFalse() = throw IllegalJsonInterpretation("Json node is object")

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
    private val EMPTY: JsonObject = JsonObject(emptyMap())

    fun of(field: Pair<JsonString, Json>): JsonObject =
      if (field.second.isUndefined())
        EMPTY
      else
        JsonObject(mapOf(field))

    fun of(vararg fields: Pair<JsonString, Json>): JsonObject =
      if (fields.isEmpty())
        EMPTY
      else {
        val definedFields = fields.filter { it.second.isDefined() }
        if (definedFields.isEmpty())
          EMPTY
        else
          JsonObject(definedFields.toMap())
      }

    fun of(fields: Collection<Pair<JsonString, Json>>): JsonObject =
      if (fields.isEmpty())
        EMPTY
      else {
        val definedFields = fields.filter { it.second.isDefined() }
        if (definedFields.isEmpty())
          EMPTY
        else
          JsonObject(definedFields.toMap())
      }

    fun of(fields: Map<JsonString, Json>): JsonObject =
      if (fields.isEmpty())
        EMPTY
      else {
        val definedFields = fields.filter { it.value.isDefined() }
        if (definedFields.isEmpty())
          EMPTY
        else
          JsonObject(definedFields.toMap())
      }
  }
}
