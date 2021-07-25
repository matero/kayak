package kayak.json

@JvmInline
internal value class JsonObject(private val fields: Map<String, Json>) : Json {
  override fun type() = Json.NodeType.OBJECT

  override fun isObject() = true

  override fun asObject() = fields

  override fun asObjectOrElse(defaultTo: Map<String, Json>) = fields

  override fun isNullableObject() = true

  override fun asNullableObject() = fields

  override operator fun get(fieldName: CharSequence): Json = fields[fieldName] ?: JsonUndefined

  override fun toString() = "JsonObject{$fields}"

  companion object {
    internal val EMPTY: JsonObject = JsonObject(emptyMap())

    fun make(field: Pair<String, Json>): JsonObject =
      if (field.second.isUndefined())
        EMPTY
      else
        JsonObject(mapOf(field))

    fun make(fields: Collection<Pair<String, Json>>): JsonObject =
      if (fields.isEmpty())
        EMPTY
      else {
        val definedFields = fields.filter { it.second.isDefined() }
        if (definedFields.isEmpty())
          EMPTY
        else
          JsonObject(definedFields.toMap())
      }

    fun make(fields: Map<String, Json>): JsonObject =
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
