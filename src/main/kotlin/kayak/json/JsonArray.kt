package kayak.json

@JvmInline
internal value class JsonArray private constructor(private val elements: List<Json>) : Json {
  override fun type() = Json.NodeType.ARRAY

  override fun isArray() = true

  override fun asArray(): List<Json> = elements

  override fun asArrayOrElse(defaultTo: List<Json>) = elements

  override fun isNullableArray(): Boolean = true

  override fun asNullableArray(): List<Json> = elements

  override operator fun get(index: Int): Json = if (elements.size <= index) JsonUndefined else elements[index]

  override fun toString() = "JsonArray{$elements}"

  companion object {
    internal val EMPTY_ARRAY = JsonArray(emptyList())

    fun make(elements: Iterator<Json>) =
      if (elements.hasNext())
        make(elements.asSequence().toList())
      else
        EMPTY_ARRAY

    fun make(elements: Iterable<Json>): JsonArray =
      when (elements) {
        is List<Json> -> make(elements)
        is Collection<Json> -> if (elements.isEmpty()) EMPTY_ARRAY else JsonArray(elements.toList())
        else -> make(elements.toList())
      }

    fun make(elements: List<Json>) = if (elements.isEmpty()) EMPTY_ARRAY else JsonArray(elements.toList())
  }
}
