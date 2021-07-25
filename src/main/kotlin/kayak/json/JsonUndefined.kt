package kayak.json

import java.math.BigDecimal
import java.math.BigInteger

object JsonUndefined : Json {
  override fun type() = Json.NodeType.UNDEFINED

  override fun isDefined() = false

  override fun isUndefined() = true

  override fun isNotNull(): Boolean = false

  override fun asBooleanOrElse(defaultTo: Boolean) = defaultTo

  override fun asStringOrElse(defaultTo: String) = defaultTo

  override fun asNumberOrElse(defaultTo: String) = defaultTo

  override fun asByteOrElse(defaultTo: Byte) = defaultTo

  override fun asShortOrElse(defaultTo: Short) = defaultTo

  override fun asIntOrElse(defaultTo: Int) = defaultTo

  override fun asLongOrElse(defaultTo: Long) = defaultTo

  override fun asFloatOrElse(defaultTo: Float) = defaultTo

  override fun asDoubleOrElse(defaultTo: Double) = defaultTo

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = defaultTo

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = defaultTo

  override fun asArrayOrElse(defaultTo: List<Json>) = defaultTo

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = defaultTo
}
