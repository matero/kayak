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
sealed interface Json : AsJson {
  enum class NodeType(private val id: String, internal val errorMessage: String = "JSON node of type '${id}'") {
    ARRAY("array"),
    BOOLEAN("boolean"),
    NUMBER("number"),
    OBJECT("object"),
    STRING("string"),
    NULL("null", "JSON node is null"),
    UNDEFINED("null", "JSON node is undefined"),
    ILLEGAL("ILLEGAL", "Illegal JSON node definition")
  }

  override fun asJson() = this
  /**
   * @return the type of the represented node.
   */
  fun type(): NodeType

  /**
   * @return `true` if the node represents a undefined JSON node/property/element, `false` other way.
   */
  fun isUndefined(): Boolean = false

  /**
   * @return `true` if the node represents a defined JSON node, `false` other way.
   */
  fun isDefined(): Boolean = true

  /**
   * @return `true` if the node represents a JSON `null`, `false` other way.
   */
  fun isNull(): Boolean = false

  /**
   * @return `true` if the node is defined and does not represents a JSON `null`, `false` other way.
   */
  fun isNotNull(): Boolean = true

  /**
   * @return `true` if the node represents a JSON boolean, `false` other way.
   */
  fun isBoolean() = false

  /**
   * @return the `boolean` represented by the node.
   * @throws  IllegalJsonInterpretation if the node does not represents a boolean value.
   */
  fun asBoolean(): Boolean = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents a boolean json node and it represents `true` value; `false` otherwise.
   * @throws  IllegalJsonInterpretation if the node does not represents a boolean value.
   */
  fun isTrue(): Boolean = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents a boolean json node and it represents `false` value; `false` otherwise.
   * @throws  IllegalJsonInterpretation if the node does not represents a boolean value.
   */
  fun isFalse(): Boolean = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `boolean` represented by the node, `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a boolean value.
   */
  fun asBooleanOrElse(defaultTo: Boolean): Boolean = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` when JSON representation for the path used to access this node is `null` or `boolean`; `false` other way.
   */
  fun isNullableBoolean() = false

  /**
   * @return the `boolean` or `null` represented by the node.
   * @throws  IllegalJsonInterpretation if the node does not represents a boolean value or `null`.
   */
  fun asNullableBoolean(): Boolean? = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents a JSON string, `false` other way.
   */
  fun isString() = false

  /**
   * @return the `String` represented by the node.
   * @throws  IllegalJsonInterpretation if the node does not represents a string value.
   */
  fun asString(): String = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `string` represented by the node, `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a string value.
   */
  fun asStringOrElse(defaultTo: String): String = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents `null` or a JSON string, `false` other way.
   */
  fun isNullableString(): Boolean = false

  /**
   * @return the `string` or `null` represented by the node.
   * @throws  IllegalJsonInterpretation if the node does not represents a string value or `null`.
   */
  fun asNullableString(): String? = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents a JSON number, `false` other way.
   */
  fun isNumber(): Boolean = false

  /**
   * @return the `number` represented by the node, as String.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asNumber(): String = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as String; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asNumberOrElse(defaultTo: String): String = throw IllegalJsonInterpretation(type())

  /**
   * @return the `number` represented by the node, as Byte.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asByte(): Byte = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as Byte; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asByteOrElse(defaultTo: Byte): Byte = throw IllegalJsonInterpretation(type())

  /**
   * @return the `number` represented by the node, as Short.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asShort(): Short = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as Short; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asShortOrElse(defaultTo: Short): Short = throw IllegalJsonInterpretation(type())

  /**
   * @return the `number` represented by the node, as Int.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asInt(): Int = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as Int; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asIntOrElse(defaultTo: Int): Int = throw IllegalJsonInterpretation(type())

  /**
   * @return the `number` represented by the node, as Long.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asLong(): Long = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as Long; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asLongOrElse(defaultTo: Long): Long = throw IllegalJsonInterpretation(type())

  /**
   * @return the `number` represented by the node, as BigInteger.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asBigInteger(): BigInteger = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as BigInteger; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asBigIntegerOrElse(defaultTo: BigInteger): BigInteger = throw IllegalJsonInterpretation(type())

  /**
   * @return the `number` represented by the node, as Float.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asFloat(): Float = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as Float; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asFloatOrElse(defaultTo: Float): Float = throw IllegalJsonInterpretation(type())

  /**
   * @return the `number` represented by the node, as Double.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asDouble(): Double = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as Double; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asDoubleOrElse(defaultTo: Double): Double = throw IllegalJsonInterpretation(type())

  /**
   * @return the `number` represented by the node, as BigDecimal.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asBigDecimal(): BigDecimal = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, as BigDecimal; `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents a number.
   */
  fun asBigDecimalOrElse(defaultTo: BigDecimal): BigDecimal = throw IllegalJsonInterpretation(type())

  /**@return `true` if the node represents `null` or a JSON number, `false` other way*/
  fun isNullableNumber(): Boolean = false

  /**
   * @return null or the `number` represented by the node, as String.
   * @throws  IllegalJsonInterpretation if the node does not represents a number value or `null`.
   */
  fun asNullableNumber(): String? = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents a JSON array, `false` other way.
   */
  fun isArray() = false

  /**
   * @return the `array` represented by the node.
   * @throws  IllegalJsonInterpretation if the node does not represents an array.
   */
  fun asArray(): List<Json> = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `number` represented by the node, defaultTo when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents an array or `null` or `undefined`.
   */
  fun asArrayOrElse(defaultTo: List<Json>): List<Json> = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents `null` or a JSON string, `false` other way.
   */
  fun isNullableArray() = false

  /**
   * @return `null` or the `array` represented by the node.
   * @throws  IllegalJsonInterpretation if the node does not represents an array or `null`.
   */
  fun asNullableArray(): List<Json>? = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents a JSON object, `false` other way.
   */
  fun isObject() = false

  /**
   * @return the `object` (map of strings to nodes) represented by the node.
   * @throws IllegalJsonInterpretation if the node is not a JSON object.
   */
  fun asObject(): Map<String, Json> = throw IllegalJsonInterpretation(type())

  /**
   * @param defaultTo default value to provide when no value exists.
   * @return the `object` represented by the node, `defaultTo` when node is `null` or `undefined`.
   * @throws  IllegalJsonInterpretation if the node does not represents an object or `null` or `undefined`.
   */
  fun asObjectOrElse(defaultTo: Map<String, Json>): Map<String, Json> = throw IllegalJsonInterpretation(type())

  /**
   * @return `true` if the node represents `null` or a JSON object, `false` other way.
   */
  fun isNullableObject() = false

  /**
   * @return the `object` (map of strings to nodes) or `null` represented by the node.
   * @throws  IllegalJsonInterpretation if the node does not represents an object or `null`.
   */
  fun asNullableObject(): Map<String, Json>? = throw IllegalJsonInterpretation(type())

  /**
   * @param index index of the value to get.
   * @return the json value at `index` if the node represents an array; UndefinedNode otherwise.
   */
  operator fun get(index: Int): Json = IllegalJson

  /**
   * @param fieldName name of the field to get.
   * @return the value of field named `fieldName` if the node represents an object; UndefinedNode otherwise.
   */
  operator fun get(fieldName: CharSequence): Json = IllegalJson

  companion object {
    // Boolean factory method
    fun from(value: Boolean?): Json = if (value == null) Null else of(value)

    fun of(value: Boolean): Json = if (value) JsonBoolean.TRUE else JsonBoolean.FALSE

    // Arrays factory methods
    fun from(elements: Iterator<Json>?): Json = if (elements == null) Null else of(elements)

    fun from(elements: Iterable<Json>?): Json = if (elements == null) Null else of(elements)

    fun from(elements: List<Json>?): Json = if (elements == null) Null else of(elements)

    fun of(elements: Iterator<Json>): Json = JsonArray.make(elements)

    fun of(elements: Iterable<Json>): Json = JsonArray.make(elements)

    fun of(vararg elements: Json): Json = JsonArray.make(listOf(*elements))

    fun of(elements: List<Json>): Json = JsonArray.make(elements)

    // Number factory methods
    fun from(value: Byte?): Json = if (value == null) Null else of(value)

    fun from(value: Short?): Json = if (value == null) Null else of(value)

    fun from(value: Int?): Json = if (value == null) Null else of(value)

    fun from(value: Long?): Json = if (value == null) Null else of(value)

    fun from(value: BigInteger?): Json = if (value == null) Null else of(value)

    fun from(value: Float?): Json = if (value == null) Null else of(value)

    fun from(value: Double?): Json = if (value == null) Null else of(value)

    fun from(value: BigDecimal?): Json = if (value == null) Null else of(value)

    fun of(value: Byte): Json = JsonNumber.make(value)

    fun of(value: Short): Json = JsonNumber.make(value)

    fun of(value: Int): Json = JsonNumber.make(value)

    fun of(value: Long): Json = JsonNumber.make(value)

    fun of(value: BigInteger): Json = JsonNumber.make(value)

    fun of(value: Float): Json = JsonNumber.make(value)

    fun of(value: Double): Json = JsonNumber.make(value)

    fun of(value: BigDecimal): Json = JsonNumber.make(value)

    // Object factory methods

    fun from(field: Pair<String, Json>?): Json = if (field == null) Null else of(field)

    fun from(fields: Collection<Pair<String, Json>>?): Json = if (fields == null) Null else of(fields)

    fun from(fields: Map<String, Json>?): Json = if (fields == null) Null else of(fields)

    fun of(field: Pair<String, Json>): Json = JsonObject.make(field)

    fun of(vararg fields: Pair<String, Json>):Json =
      if (fields.isEmpty())
        JsonObject.EMPTY
      else {
        val definedFields = fields.filter { it.second.isDefined() }
        if (definedFields.isEmpty())
          JsonObject.EMPTY
        else
          JsonObject.make(definedFields.toMap())
      }

    fun of(fields: Collection<Pair<String, Json>>): Json = JsonObject.make(fields)

    fun of(fields: Map<String, Json>): Json = JsonObject.make(fields)

    // String factory methods
    fun from(value: CharSequence?): Json  = if (value == null) Null else of(value)

    fun of(value: CharSequence): Json = JsonString.make(value)

    // Json Parsing
    fun parse(input: String, desiredBufferCapacity: Int = input.length): Json = JsonParser.of(input, desiredBufferCapacity).parse()

    fun parse(request: kayak.web.Request, desiredBufferCapacity: Int = JsonParser.DEFAULT_BUFFER_CAPACITY): Json =
      JsonParser.of(request.reader, desiredBufferCapacity).parse()

    fun parse(input: java.io.InputStream, desiredBufferCapacity: Int = JsonParser.DEFAULT_BUFFER_CAPACITY): Json =
      JsonParser.of(java.io.InputStreamReader(input), desiredBufferCapacity).parse()

    fun parse(input: java.io.Reader, desiredBufferCapacity: Int = JsonParser.DEFAULT_BUFFER_CAPACITY): Json =
      JsonParser.of(input, desiredBufferCapacity).parse()
  }

  object Null : Json {
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

  object Undefined : Json {
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

    override fun asObjectOrElse(defaultTo: Map<String, Json>) = defaultTo
  }
}

interface AsJson {
  fun asJson(): Json
}

class IllegalJsonInterpretation internal constructor(message: String) : KayakFailure(message) {
  constructor(type: Json.NodeType) : this(type.errorMessage)
}
