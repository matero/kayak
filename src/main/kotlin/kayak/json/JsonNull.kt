package kayak.json

import java.math.BigDecimal
import java.math.BigInteger

internal object JsonNull : Json {
  override fun type(): Json.NodeType = Json.NodeType.NULL

  override fun isNull(): Boolean = true

  override fun isNotNull(): Boolean = false

  override fun isNullableBoolean() = true

  override fun asNullableBoolean(): Boolean? = null

  override fun asBooleanOrElse(defaultTo: Boolean) = defaultTo

  override fun isNullableString() = true

  override fun asNullableString(): String? = null

  override fun asStringOrElse(defaultTo: String) = defaultTo

  override fun isNullableNumber() = true

  override fun asNullableNumber(): String? = null

  override fun asNumberOrElse(defaultTo: String) = defaultTo

  override fun asByteOrElse(defaultTo: Byte) = defaultTo

  override fun asShortOrElse(defaultTo: Short) = defaultTo

  override fun asIntOrElse(defaultTo: Int) = defaultTo

  override fun asLongOrElse(defaultTo: Long) = defaultTo

  override fun asFloatOrElse(defaultTo: Float) = defaultTo

  override fun asDoubleOrElse(defaultTo: Double) = defaultTo

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = defaultTo

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = defaultTo

  override fun isNullableObject() = true

  override fun asNullableObject(): Map<String, Json>? = null

  override fun asObjectOrElse(defaultTo: Map<String, Json>) = defaultTo

  override fun isNullableArray() = true

  override fun asNullableArray(): List<Json>? = null

  override fun asArrayOrElse(defaultTo: List<Json>) = defaultTo
}
