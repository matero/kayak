package kayak.json

@JvmInline
value class ArrayNode private constructor(private val elements: List<JsonNode>) : JsonNode {
  override fun isArray() = true

  override fun asArray(): List<JsonNode> = elements

  override fun isNullableArray(): Boolean = true

  override fun asNullableArray(): List<JsonNode> = elements

  override fun toString(): String {
    return "JsonArray{elements=$elements}"
  }

  companion object {
    private val EMPTY_ARRAY = ArrayNode(emptyList())

    operator fun invoke(elements: Iterator<JsonNode>) =
      if (elements.hasNext())
        invoke(elements.asSequence().toList())
      else
        EMPTY_ARRAY

    operator fun invoke(elements: Iterable<JsonNode>): ArrayNode =
      when (elements) {
        is List<JsonNode> -> invoke(elements)
        is Collection<JsonNode> -> if (elements.isEmpty()) EMPTY_ARRAY else ArrayNode(elements.toList())
        else -> invoke(elements.toList())
      }

    operator fun invoke(vararg elements: JsonNode) = invoke(listOf(*elements))

    operator fun invoke(elementList: List<JsonNode>) =
      if (elementList.isEmpty()) EMPTY_ARRAY else ArrayNode(elementList)
  }
}
