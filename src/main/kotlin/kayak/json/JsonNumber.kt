package kayak.json

import java.math.BigDecimal
import java.math.BigInteger

@JvmInline
internal value class JsonNumber private constructor(private val value: String) : Json {
  override fun type() = Json.NodeType.NUMBER

  override fun isNumber() = true

  override fun asNumber() = value

  override fun asNumberOrElse(defaultTo: String) = value

  override fun isNullableNumber() = true

  override fun asNullableNumber() = value

  override fun asByte() = value.toByte()

  override fun asByteOrElse(defaultTo: Byte) = value.toByte()

  override fun asShort() = value.toShort()

  override fun asShortOrElse(defaultTo: Short) = value.toShort()

  override fun asInt() = value.toInt()

  override fun asIntOrElse(defaultTo: Int) = value.toInt()

  override fun asLong() = value.toLong()

  override fun asLongOrElse(defaultTo: Long) = value.toLong()

  override fun asFloat() = value.toFloat()

  override fun asFloatOrElse(defaultTo: Float) = value.toFloat()

  override fun asDouble() = value.toDouble()

  override fun asDoubleOrElse(defaultTo: Double) = value.toDouble()

  override fun asBigInteger() = value.toBigInteger()

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = value.toBigInteger()

  override fun asBigDecimal() = value.toBigDecimal()

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = value.toBigDecimal()

  override fun toString() = "NumberNode{'$value'}"

  companion object {
    private val ZERO = JsonNumber("0")
    private val ONE = JsonNumber("1")
    private val TWO = JsonNumber("2")
    private val TEN = JsonNumber("10")
    private val ZERO_DEC = JsonNumber("0.0")
    private val ONE_DEC = JsonNumber("1.0")
    private val TEN_DEC = JsonNumber("10.0")

    fun make(value: CharSequence): JsonNumber {
      return when (val s = value.toString()) {
        "0" -> ZERO
        "1" -> ONE
        "2" -> TWO
        "10" -> TEN
        else -> makeFrom(s)
      }
    }

    fun make(value: Byte): JsonNumber {
      return when (value.toInt()) {
        0 -> ZERO
        1 -> ONE
        2 -> TWO
        10 -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun make(value: Short): JsonNumber {
      return when (value.toInt()) {
        0 -> ZERO
        1 -> ONE
        2 -> TWO
        10 -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun make(value: Int): JsonNumber {
      return when (value) {
        0 -> ZERO
        1 -> ONE
        2 -> TWO
        10 -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun make(value: Long): JsonNumber {
      return when (value) {
        0L -> ZERO
        1L -> ONE
        2L -> TWO
        10L -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun make(value: BigInteger): JsonNumber {
      return when (value) {
        BigInteger.ZERO -> ZERO
        BigInteger.ONE -> ONE
        BigInteger.TWO -> TWO
        BigInteger.TEN -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun make(value: Float): JsonNumber {
      return when (value) {
        0.0f -> ZERO_DEC
        1.0f -> ONE_DEC
        10.0f -> ONE_DEC
        else -> JsonNumber(value.toString())
      }
    }

    fun make(value: Double): JsonNumber {
      return when (value) {
        0.0 -> ZERO_DEC
        1.0 -> ONE_DEC
        10.0 -> ONE_DEC
        else -> JsonNumber(value.toString())
      }
    }

    fun make(value: BigDecimal): JsonNumber {
      return when (value) {
        BigDecimal.ZERO -> ZERO_DEC
        BigDecimal.ONE -> ONE_DEC
        BigDecimal.TEN -> TEN_DEC
        else -> JsonNumber(value.toString())
      }
    }

    private fun makeFrom(value: String): JsonNumber {
      if (!JsonNumberValidator.isValid(value))
        throw IllegalArgumentException("Attempt to construct a JsonNumber with a String [$value] that does not match the JSON number specification.")
      return JsonNumber(value)
    }
  }
}
