package kayak.json

class ObjectNode(private val fields: Map<StringNode, JsonNode>) : JsonNode {
  override fun isObject() = true

  override fun asObject() = fields

  override fun isNullableObject() = true

  override fun asNullableObject() = fields

  override fun equals(other: Any?): Boolean = (this === other) || (other is ObjectNode && fields == other.fields)

  override fun hashCode(): Int {
    return fields.hashCode()
  }

  override fun toString(): String {
    return "JsonObject{fields=$fields}"
  }

  companion object {
    private val EMPTY_OBJECT: ObjectNode = ObjectNode(emptyMap())

    operator fun invoke(field: Pair<StringNode, JsonNode>): ObjectNode = ObjectNode(mapOf(field))

    operator fun invoke(vararg fields: Pair<StringNode, JsonNode>): ObjectNode = invoke(mapOf(*fields))

    operator fun invoke(fields: Iterable<Pair<StringNode, JsonNode>>): ObjectNode = ObjectNode(fields.toMap())

    operator fun invoke(fields: Map<StringNode, JsonNode>): ObjectNode =
      if (fields.isEmpty())
        EMPTY_OBJECT
      else
        ObjectNode(fields)
  }
}
