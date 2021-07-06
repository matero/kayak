package kayak.json

import kayak.KayakFailure
import java.math.BigDecimal
import java.math.BigInteger

/**
 * A node (leaf or otherwise) in a JSON document.
 *
 *
 * Supplies methods for examining the node, and also examining and navigating the hierarchy at and below this node.
 * Methods for navigating the hierarchy are of the form `getXXXValue(Object... pathElements)`.
 *
 *
 * For example, [.getStringValue] takes a series of `String`s and
 * `Integer`s as its argument which tell it how to navigate down a hierarchy to a particular JSON string.
 * The `String`s tell it to select a field with the given name from an object, and the `Integer`s
 * tell it to select an element with the given index from an array.  If no field of that name exists, or the field
 * exists, but it isn't a JSON string, an `IllegalArgumentException` is thrown.
 *
 *
 * Methods for examining the hierarchy work on the same principal as the
 * `getXXXValue(Object... pathElements)` methods, but return a `boolean` indicating whether
 * or not the element at the given path exists and is of the type specified, for example,
 * `getStringValue("my field")` returns `true` if the node has a field called
 * "`my field`", and its value is a JSON string.
 */
sealed interface Json {
  /**@return `true` if the node represents a undefined JSON node/property/element, `false` other way*/
  fun isUndefined(): Boolean = false

  /**@return `true` if the node represents a defined JSON node, `false` other way*/
  fun isDefined(): Boolean = true

  /**@return `true` if the node represents a JSON `null`, `false` other way*/
  fun isNull(): Boolean = false

  /**@return `true` if the node represents a JSON boolean, `false` other way*/
  fun isBoolean() = false

  /**
   * @return the `boolean` represented by the node.
   * @throws IllegalJsonInterpretation if the node is not a JSON boolean.
   */
  fun asBoolean(): Boolean
  fun isTrue(): Boolean
  fun isFalse(): Boolean

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `boolean` represented by the node, defaultTo when node is null or undefined.
   * @throws IllegalJsonInterpretation if the node is not a JSON boolean.
   */
  fun asBooleanOrElse(defaultTo: Boolean): Boolean

  /** @return `true` when JSON representation for the path used to access this node is `null` or `boolean`; `false` other way. */
  fun isNullableBoolean() = false

  /**
   * @return the `boolean` or `null` represented by the node.
   * @throws IllegalJsonInterpretation if the node is not `null` or a JSON boolean.
   */
  fun asNullableBoolean(): Boolean?

  /**@return `true` if the node represents a JSON string, `false` other way*/
  fun isString() = false

  /**
   * @return the `String` represented by the node.
   * @throws IllegalJsonInterpretation if the node is not a JSON string.
   */
  fun asString(): String

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `string` represented by the node, defaultTo when node is null or undefined.
   * @throws IllegalJsonInterpretation if the node is not a JSON string.
   */
  fun asStringOrElse(defaultTo: String): String

  /**@return `true` if the node represents `null` or a JSON string, `false` other way*/
  fun isNullableString(): Boolean = false

  /**
   * @return the `string` or `null` represented by the node.
   * @throws IllegalJsonInterpretation if the node is not `null` or a JSON string.
   */
  fun asNullableString(): String?

  /**@return `true` if the node represents a JSON number, `false` other way*/
  fun isNumber(): Boolean = false

  /**
   * @return the `number` represented by the node, as String.
   * @throws IllegalJsonInterpretation if the node is not a JSON string.
   */
  fun asNumber(): String

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, defaultTo when node is null or undefined.
   * @throws IllegalJsonInterpretation if the node is not a JSON string.
   */
  fun asNumberOrElse(defaultTo: String): String

  fun asByte(): Byte
  fun asByteOrElse(defaultTo: Byte): Byte
  fun asShort(): Short
  fun asShortOrElse(defaultTo: Short): Short
  fun asInt(): Int
  fun asIntOrElse(defaultTo: Int): Int
  fun asLong(): Long
  fun asLongOrElse(defaultTo: Long): Long
  fun asBigInteger(): BigInteger
  fun asBigIntegerOrElse(defaultTo: BigInteger): BigInteger
  fun asFloat(): Float
  fun asFloatOrElse(defaultTo: Float): Float
  fun asDouble(): Double
  fun asDoubleOrElse(defaultTo: Double): Double
  fun asBigDecimal(): BigDecimal
  fun asBigDecimalOrElse(defaultTo: BigDecimal): BigDecimal

  /**@return `true` if the node represents `null` or a JSON number, `false` other way*/
  fun isNullableNumber(): Boolean = false

  /**
   * @return null or the `number` represented by the node, as String.
   * @throws IllegalJsonInterpretation if the node is not a JSON string.
   */
  fun asNullableNumber(): String?

  /**@return `true` if the node represents a JSON array, `false` other way*/
  fun isArray() = false

  /**
   * @return the `array` represented by the node.
   * @throws IllegalJsonInterpretation if the node is not a JSON string.
   */
  fun asArray(): List<Json>

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, defaultTo when node is null or undefined.
   * @throws IllegalJsonInterpretation if the node is not a JSON string.
   */
  fun asArrayOrElse(defaultTo: List<Json>): List<Json>

  /**@return `true` if the node represents `null` or a JSON string, `false` other way*/
  fun isNullableArray() = false

  /**
   * @return null or the `array` represented by the node.
   * @throws IllegalJsonInterpretation if the node is not a JSON string.
   */
  fun asNullableArray(): List<Json>?

  /**@return `true` if the node represents a JSON object, `false` other way*/
  fun isObject() = false

  /**
   * @return the `object` (map of strings to nodes) represented by the node.
   * @throws IllegalJsonInterpretation if the node is not a JSON object.
   */
  fun asObject(): Map<JsonString, Json>

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `object` represented by the node, defaultTo when node is null or undefined.
   * @throws IllegalJsonInterpretation if the node is not a JSON object.
   */
  fun asObjectOrElse(defaultTo: Map<JsonString, Json>): Map<JsonString, Json>

  /**@return `true` if the node represents `null` or a JSON object, `false` other way*/
  fun isNullableObject() = false

  /**
   * @return the `object` (map of strings to nodes) or `null` represented by the node.
   * @throws IllegalJsonInterpretation if the node is not `null` or a JSON object.
   */
  fun asNullableObject(): Map<JsonString, Json>?

  operator fun get(index: Int): Json = IllegalJson
  operator fun get(fieldName: String): Json = IllegalJson
  operator fun get(fieldName: JsonString): Json = IllegalJson

  companion object {
    fun parse(input: String, desiredBufferCapacity: Int = input.length): Json = JsonParser.of(input, desiredBufferCapacity).parse()

    fun parse(request: kayak.web.Request, desiredBufferCapacity: Int = JsonParser.DEFAULT_BUFFER_CAPACITY): Json =
      JsonParser.of(request.reader, desiredBufferCapacity).parse()

    fun parse(input: java.io.InputStream, desiredBufferCapacity: Int = JsonParser.DEFAULT_BUFFER_CAPACITY): Json =
      JsonParser.of(java.io.InputStreamReader(input), desiredBufferCapacity).parse()

    fun parse(input: java.io.Reader, desiredBufferCapacity: Int = JsonParser.DEFAULT_BUFFER_CAPACITY): Json =
      JsonParser.of(input, desiredBufferCapacity).parse()

    fun field(name: CharSequence, value: Json): Pair<JsonString, Json> = field(JsonString.of(name.toString()), value)

    fun field(name: JsonString, value: Json): Pair<JsonString, Json> = Pair(name, value)
  }
}

internal object IllegalJson : Json {
  override fun isDefined() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun isUndefined() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun isNull() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asBoolean() = throw IllegalJsonInterpretation("ILLEGAL Json")
  override fun isTrue() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun isFalse() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asBooleanOrElse(defaultTo: Boolean) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asNullableBoolean() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asString() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asStringOrElse(defaultTo: String) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asNullableString() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asNumber() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asNumberOrElse(defaultTo: String) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asByte() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asByteOrElse(defaultTo: Byte) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asShort() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asShortOrElse(defaultTo: Short) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asInt() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asIntOrElse(defaultTo: Int) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asLong() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asLongOrElse(defaultTo: Long) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asBigInteger() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asFloat() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asFloatOrElse(defaultTo: Float) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asDouble() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asDoubleOrElse(defaultTo: Double) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asBigDecimal() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asNullableNumber() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asArray(): List<Json> = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asArrayOrElse(defaultTo: List<Json>) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asNullableArray() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asObject() = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = throw IllegalJsonInterpretation("ILLEGAL Json")

  override fun asNullableObject() = throw IllegalJsonInterpretation("ILLEGAL Json")
}

object JsonUndefined : Json {
  override fun isDefined() = false

  override fun isUndefined() = true

  override fun asBoolean() = throw IllegalJsonInterpretation("Json node is undefined")
  override fun isTrue() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun isFalse() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asBooleanOrElse(defaultTo: Boolean) = defaultTo

  override fun asNullableBoolean() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asString() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asStringOrElse(defaultTo: String) = defaultTo

  override fun asNullableString() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asNumber() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asNumberOrElse(defaultTo: String) = defaultTo

  override fun asByte() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asByteOrElse(defaultTo: Byte) = defaultTo

  override fun asShort() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asShortOrElse(defaultTo: Short) = defaultTo

  override fun asInt() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asIntOrElse(defaultTo: Int) = defaultTo

  override fun asLong() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asLongOrElse(defaultTo: Long) = defaultTo

  override fun asBigInteger() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asFloatOrElse(defaultTo: Float) = defaultTo

  override fun asDouble() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asDoubleOrElse(defaultTo: Double) = defaultTo

  override fun asBigDecimal() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = defaultTo

  override fun asFloat() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = defaultTo

  override fun asNullableNumber() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asArray() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asArrayOrElse(defaultTo: List<Json>) = defaultTo

  override fun asNullableArray() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asObject() = throw IllegalJsonInterpretation("Json node is undefined")

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = defaultTo

  override fun asNullableObject() = throw IllegalJsonInterpretation("Json node is undefined")
}

object JsonNull : Json {
  override fun isNull(): Boolean = true

  override fun isNullableBoolean() = true

  override fun asBoolean() = throw IllegalJsonInterpretation("Json node is null")

  override fun isTrue() = throw IllegalJsonInterpretation("Json node is null")

  override fun isFalse() = throw IllegalJsonInterpretation("Json node is null")

  override fun asNullableBoolean(): Boolean? = null

  override fun asBooleanOrElse(defaultTo: Boolean) = defaultTo

  override fun isNullableString() = true

  override fun asString() = throw IllegalJsonInterpretation("Json node is null")

  override fun asNullableString(): String? = null

  override fun asStringOrElse(defaultTo: String) = defaultTo

  override fun asNumber() = throw IllegalJsonInterpretation("Json node is null")

  override fun isNullableNumber() = true

  override fun asNullableNumber(): String? = null

  override fun asNumberOrElse(defaultTo: String) = defaultTo

  override fun asByte() = throw IllegalJsonInterpretation("Json node is null")

  override fun asByteOrElse(defaultTo: Byte) = defaultTo

  override fun asShort() = throw IllegalJsonInterpretation("Json node is null")

  override fun asShortOrElse(defaultTo: Short) = defaultTo

  override fun asInt() = throw IllegalJsonInterpretation("Json node is null")

  override fun asIntOrElse(defaultTo: Int) = defaultTo

  override fun asLong() = throw IllegalJsonInterpretation("Json node is null")

  override fun asLongOrElse(defaultTo: Long) = defaultTo

  override fun asFloat() = throw IllegalJsonInterpretation("Json node is null")

  override fun asFloatOrElse(defaultTo: Float) = defaultTo

  override fun asDouble() = throw IllegalJsonInterpretation("Json node is null")

  override fun asDoubleOrElse(defaultTo: Double) = defaultTo

  override fun asBigInteger() = throw IllegalJsonInterpretation("Json node is null")

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = defaultTo

  override fun asBigDecimal() = throw IllegalJsonInterpretation("Json node is null")

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = defaultTo

  override fun asObject() = throw IllegalJsonInterpretation("Json node is null")

  override fun isNullableObject() = true

  override fun asNullableObject(): Map<JsonString, Json>? = null

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = defaultTo

  override fun isNullableArray() = true

  override fun asArray() = throw IllegalJsonInterpretation("Json node is null")

  override fun asNullableArray(): List<Json>? = null

  override fun asArrayOrElse(defaultTo: List<Json>) = defaultTo
}

enum class JsonBoolean : Json {
  TRUE {
    override fun toString() = "true"
    override fun asBoolean() = true
    override fun isTrue() = true
    override fun isFalse() = false
    override fun asBooleanOrElse(defaultTo: Boolean) = true
    override fun asNullableBoolean() = true
  },
  FALSE {
    override fun toString() = "false"
    override fun asBoolean() = false
    override fun isTrue() = false
    override fun isFalse() = true
    override fun asBooleanOrElse(defaultTo: Boolean) = false
    override fun asNullableBoolean() = false
  };

  override fun isBoolean() = true

  override fun isNullableBoolean() = true

  override fun asString() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asNullableString() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asStringOrElse(defaultTo: String) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asNumber() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asNullableNumber() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asNumberOrElse(defaultTo: String) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asByte() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asByteOrElse(defaultTo: Byte) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asShort() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asShortOrElse(defaultTo: Short) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asInt() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asIntOrElse(defaultTo: Int) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asLong() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asLongOrElse(defaultTo: Long) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asFloat() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asFloatOrElse(defaultTo: Float) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asDouble() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asDoubleOrElse(defaultTo: Double) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asBigInteger() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asBigDecimal() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asObject() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asNullableObject(): Map<JsonString, Json>? = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = throw IllegalJsonInterpretation("Json node is boolean")

  override fun isNullableArray() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asArray() = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asNullableArray(): List<Json>? = throw IllegalJsonInterpretation("Json node is boolean")

  override fun asArrayOrElse(defaultTo: List<Json>) = throw IllegalJsonInterpretation("Json node is boolean")

  companion object {
    fun of(value: Boolean): JsonBoolean = if (value) TRUE else FALSE
  }
}

@JvmInline
value class JsonNumber private constructor(private val value: String) : Json {
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

  override fun toString() = "NumberNode{value='$value'}"

  override fun asBoolean() = throw IllegalJsonInterpretation("Json node is number")

  override fun isTrue() = throw IllegalJsonInterpretation("Json node is number")

  override fun isFalse() = throw IllegalJsonInterpretation("Json node is number")

  override fun asNullableBoolean() = throw IllegalJsonInterpretation("Json node is number")

  override fun asBooleanOrElse(defaultTo: Boolean) = throw IllegalJsonInterpretation("Json node is number")

  override fun asString() = throw IllegalJsonInterpretation("Json node is number")

  override fun asNullableString() = throw IllegalJsonInterpretation("Json node is number")

  override fun asStringOrElse(defaultTo: String) = throw IllegalJsonInterpretation("Json node is number")

  override fun asObject() = throw IllegalJsonInterpretation("Json node is number")

  override fun asNullableObject(): Map<JsonString, Json>? = throw IllegalJsonInterpretation("Json node is number")

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = throw IllegalJsonInterpretation("Json node is number")

  override fun isNullableArray() = throw IllegalJsonInterpretation("Json node is number")

  override fun asArray() = throw IllegalJsonInterpretation("Json node is number")

  override fun asNullableArray(): List<Json>? = throw IllegalJsonInterpretation("Json node is number")

  override fun asArrayOrElse(defaultTo: List<Json>) = throw IllegalJsonInterpretation("Json node is number")

  companion object {
    private val ZERO = JsonNumber("0")
    private val ONE = JsonNumber("1")
    private val TWO = JsonNumber("2")
    private val TEN = JsonNumber("10")
    private val ZERO_DEC = JsonNumber("0.0")
    private val ONE_DEC = JsonNumber("1.0")
    private val TEN_DEC = JsonNumber("10.0")

    fun of(value: CharSequence): JsonNumber {
      return when (val s = value.toString()) {
        "0" -> ZERO
        "1" -> ONE
        "2" -> TWO
        "10" -> TEN
        else -> makeFrom(s)
      }
    }

    fun of(value: Byte): JsonNumber {
      return when (value.toInt()) {
        0 -> ZERO
        1 -> ONE
        2 -> TWO
        10 -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun of(value: Short): JsonNumber {
      return when (value.toInt()) {
        0 -> ZERO
        1 -> ONE
        2 -> TWO
        10 -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun of(value: Int): JsonNumber {
      return when (value) {
        0 -> ZERO
        1 -> ONE
        2 -> TWO
        10 -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun of(value: Long): JsonNumber {
      return when (value) {
        0L -> ZERO
        1L -> ONE
        2L -> TWO
        10L -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun of(value: BigInteger): JsonNumber {
      return when (value) {
        BigInteger.ZERO -> ZERO
        BigInteger.ONE -> ONE
        BigInteger.TWO -> TWO
        BigInteger.TEN -> TEN
        else -> JsonNumber(value.toString())
      }
    }

    fun of(value: Float): JsonNumber {
      return when (value) {
        0.0f -> ZERO_DEC
        1.0f -> ONE_DEC
        10.0f -> ONE_DEC
        else -> JsonNumber(value.toString())
      }
    }

    fun of(value: Double): JsonNumber {
      return when (value) {
        0.0 -> ZERO_DEC
        1.0 -> ONE_DEC
        10.0 -> ONE_DEC
        else -> JsonNumber(value.toString())
      }
    }

    fun of(value: BigDecimal): JsonNumber {
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

@JvmInline
value class JsonString private constructor(private val value: String) : Json, Comparable<JsonString> {
  override fun isString() = true

  override fun asString() = value

  override fun asStringOrElse(defaultTo: String) = value

  override fun isNullableString() = true

  override fun asNullableString() = value

  override fun asBoolean() = throw IllegalJsonInterpretation("Json node is string")

  override fun isTrue() = throw IllegalJsonInterpretation("Json node is string")

  override fun isFalse() = throw IllegalJsonInterpretation("Json node is string")

  override fun asNullableBoolean() = throw IllegalJsonInterpretation("Json node is string")

  override fun asBooleanOrElse(defaultTo: Boolean) = throw IllegalJsonInterpretation("Json node is string")

  override fun asNumber() = throw IllegalJsonInterpretation("Json node is string")

  override fun asNullableNumber() = throw IllegalJsonInterpretation("Json node is string")

  override fun asNumberOrElse(defaultTo: String) = throw IllegalJsonInterpretation("Json node is string")

  override fun asByte() = throw IllegalJsonInterpretation("Json node is string")

  override fun asByteOrElse(defaultTo: Byte) = throw IllegalJsonInterpretation("Json node is string")

  override fun asShort() = throw IllegalJsonInterpretation("Json node is string")

  override fun asShortOrElse(defaultTo: Short) = throw IllegalJsonInterpretation("Json node is string")

  override fun asInt() = throw IllegalJsonInterpretation("Json node is string")

  override fun asIntOrElse(defaultTo: Int) = throw IllegalJsonInterpretation("Json node is string")

  override fun asLong() = throw IllegalJsonInterpretation("Json node is string")

  override fun asLongOrElse(defaultTo: Long) = throw IllegalJsonInterpretation("Json node is string")

  override fun asFloat() = throw IllegalJsonInterpretation("Json node is string")

  override fun asFloatOrElse(defaultTo: Float) = throw IllegalJsonInterpretation("Json node is string")

  override fun asDouble() = throw IllegalJsonInterpretation("Json node is string")

  override fun asDoubleOrElse(defaultTo: Double) = throw IllegalJsonInterpretation("Json node is string")

  override fun asBigInteger() = throw IllegalJsonInterpretation("Json node is string")

  override fun asBigIntegerOrElse(defaultTo: BigInteger) = throw IllegalJsonInterpretation("Json node is string")

  override fun asBigDecimal() = throw IllegalJsonInterpretation("Json node is string")

  override fun asBigDecimalOrElse(defaultTo: BigDecimal) = throw IllegalJsonInterpretation("Json node is string")

  override fun asObject() = throw IllegalJsonInterpretation("Json node is string")

  override fun asNullableObject(): Map<JsonString, Json>? = throw IllegalJsonInterpretation("Json node is string")

  override fun asObjectOrElse(defaultTo: Map<JsonString, Json>) = throw IllegalJsonInterpretation("Json node is string")

  override fun isNullableArray() = throw IllegalJsonInterpretation("Json node is string")

  override fun asArray() = throw IllegalJsonInterpretation("Json node is string")

  override fun asNullableArray(): List<Json>? = throw IllegalJsonInterpretation("Json node is string")

  override fun asArrayOrElse(defaultTo: List<Json>) = throw IllegalJsonInterpretation("Json node is string")

  override fun toString() = "StringValue{value='$value'}"

  override operator fun compareTo(other: JsonString): Int = value.compareTo(other.value)

  companion object {
    private val EMPTY = JsonString("")

    fun of(value: CharSequence) = if (value.isEmpty()) EMPTY else JsonString(value.toString())
  }
}

class IllegalJsonInterpretation internal constructor(message: String) : KayakFailure(message)
