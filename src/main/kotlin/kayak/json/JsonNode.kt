package kayak.json

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
sealed interface JsonNode {
  /**@return `true` if the node represents a undefined JSON node/property/element, `false` other way*/
  fun isUndefined(): Boolean = false

  /**@return `true` if the node represents a defined JSON node, `false` other way*/
  fun isNode(): Boolean = true

  /**@return `true` if the node represents a JSON `null`, `false` other way*/
  fun isNull(): Boolean = false

  /**@return `true` if the node represents a JSON boolean, `false` other way*/
  fun isBoolean() = false

  /**
   * @return the `boolean` represented by the node.
   * @throws IllegalStateException if the node is not a JSON boolean.
   */
  fun asBoolean(): Boolean = throw IllegalStateException("can't interpret JsonNode as Boolean")

  /** @return `true` when JSON representation for the path used to access this node is `null` or `boolean`; `false` other way. */
  fun isNullableBoolean() = false

  /**
   * @return the `boolean` or `null` represented by the node.
   * @throws IllegalStateException if the node is not `null` or a JSON boolean.
   */
  fun asNullableBoolean(): Boolean? = throw IllegalStateException("can't interpret JsonNode as null or Boolean")

  /**@return `true` if the node represents a JSON string, `false` other way*/
  fun isString() = false

  /**
   * @return the `String` represented by the node.
   * @throws IllegalStateException if the node is not a JSON string.
   */
  fun asString(): String = throw IllegalStateException("can't interpret JsonNode as String")

  /**@return `true` if the node represents `null` or a JSON string, `false` other way*/
  fun isNullableString(): Boolean = false

  /**
   * @return the `string` or `null` represented by the node.
   * @throws IllegalStateException if the node is not `null` or a JSON string.
   */
  fun asNullableString(): String? = throw IllegalStateException("can't interpret JsonNode as null or String")

  /**@return `true` if the node represents a JSON number, `false` other way*/
  fun isNumber(): Boolean = false

  /**
   * @return the `number` represented by the node, as String.
   * @throws IllegalStateException if the node is not a JSON string.
   */
  fun asNumber(): String = throw IllegalStateException("can't interpret JsonNode as Number")

  /**@return `true` if the node represents `null` or a JSON number, `false` other way*/
  fun isNullableNumber(): Boolean = false

  /**
   * @return the `number` represented by the node, as String.
   * @throws IllegalStateException if the node is not a JSON string.
   */
  fun asNullableNumber(): String? = throw IllegalStateException("can't interpret JsonNode as null or Number")

  /**@return `true` if the node represents a JSON array, `false` other way*/
  fun isArray() = false

  /**@return `true` if the node represents `null` or a JSON string, `false` other way*/
  fun isNullableArray() = false

  /**@return `true` if the node represents a JSON object, `false` other way*/
  fun isObject() = false

  /**
   * @return the `object` (map of strings to nodes) represented by the node.
   * @throws IllegalStateException if the node is not a JSON object.
   */
  fun asObject(): Map<StringNode, JsonNode> = throw IllegalStateException("can't interpret JsonNode as Object")

  /**@return `true` if the node represents `null` or a JSON object, `false` other way*/
  fun isNullableObject() = false

  /**
   * @return the `object` (map of strings to nodes) or `null` represented by the node.
   * @throws IllegalStateException if the node is not `null` or a JSON object.
   */
  fun asNullableObject(): Map<StringNode, JsonNode>? = throw IllegalStateException("can't interpret JsonNode as null or Object")
  fun asArray(): List<JsonNode> = throw IllegalStateException("can't interpret JsonNode as array")
  fun asNullableArray(): List<JsonNode>? =
    throw IllegalStateException("can't interpret JsonNode as String")

  operator fun get(index: Int): JsonNode = UndefinedJsonNode
  operator fun get(fieldName: String): JsonNode = UndefinedJsonNode
  operator fun get(fieldName: StringNode): JsonNode = UndefinedJsonNode
}

internal object UndefinedJsonNode : JsonNode {
  override fun isUndefined() = true
  override fun asBoolean() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as Boolean")
  override fun asNullableBoolean() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as null or Boolean")
  override fun asString() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as String")
  override fun asNullableString() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as null or String")
  override fun asNumber() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as Number")
  override fun asNullableNumber() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as null or Number")
  override fun asObject() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as Object")
  override fun asNullableObject() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as null or Object")
  override fun asArray() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as array")
  override fun asNullableArray() = throw IllegalStateException("can't interpret UNDEFINED JsonNode as String")
}

object NullNode : JsonNode {
  override fun isNull(): Boolean = true
  override fun isNullableBoolean() = true
  override fun asNullableBoolean(): Boolean? = null
  override fun isNullableString() = true
  override fun asNullableString(): String? = null
  override fun isNullableNumber() = true
  override fun asNullableNumber(): String? = null
  override fun isNullableObject() = true
  override fun asNullableObject(): Map<StringNode, JsonNode>? = null
  override fun isNullableArray() = true
  override fun asNullableArray(): List<JsonNode>? = null
}

enum class BooleanNode : JsonNode {
  TRUE {
    override fun toString() = "true"
    override fun asBoolean() = true
    override fun asNullableBoolean() = true
  },
  FALSE {
    override fun toString() = "false"
    override fun asBoolean() = false
    override fun asNullableBoolean() = false
  };

  override fun isBoolean() = true
  override fun isNullableBoolean() = true
}

@JvmInline
value class NumberNode private constructor(private val value: String) : JsonNode {
  override fun isNumber() = true

  override fun asNumber() = value

  override fun isNullableNumber() = true

  override fun asNullableNumber() = value

  override fun toString(): String {
    return "JsonNumberNode{value='$value}"
  }

  companion object {
    private val ZERO = NumberNode("0")
    private val ONE = NumberNode("1")
    private val ZERO_DEC = NumberNode("0.0")
    private val ONE_DEC = NumberNode("1.0")

    operator fun invoke(value: String): NumberNode {
      return when (value) {
        "0" -> ZERO
        "1" -> ONE
        else -> makeFrom(value)
      }
    }

    operator fun invoke(value: Int): NumberNode {
      return when (value) {
        0 -> ZERO
        1 -> ONE
        else -> NumberNode(value.toString())
      }
    }

    operator fun invoke(value: Long): NumberNode {
      return when (value) {
        0L -> ZERO
        1L -> ONE
        else -> NumberNode(value.toString())
      }
    }

    operator fun invoke(value: Float): NumberNode {
      return when (value) {
        0.0f -> ZERO_DEC
        1.0f -> ONE_DEC
        else -> NumberNode(value.toString())
      }
    }

    operator fun invoke(value: Double): NumberNode {
      return when (value) {
        0.0 -> ZERO_DEC
        1.0 -> ONE_DEC
        else -> NumberNode(value.toString())
      }
    }

    private fun makeFrom(value: String): NumberNode {
      if (!JsonNumberValidator.isValid(value))
        throw IllegalArgumentException("Attempt to construct a JsonNumber with a String [$value] that does not match the JSON number specification.")
      return NumberNode(value)
    }
  }
}

@JvmInline
value class StringNode private constructor(private val value: String) : JsonNode, Comparable<StringNode> {
  override fun isString() = true

  override fun asString() = value

  override fun isNullableString() = true

  override fun asNullableString() = value

  override fun toString(): String {
    return "JsonStringNode{value='$value'}"
  }

  override operator fun compareTo(that: StringNode): Int = value.compareTo(that.value)

  companion object {
    private val EMPTY = StringNode("")

    operator fun invoke(value: String) = if ("".equals(value)) EMPTY else StringNode(value)
  }
}
