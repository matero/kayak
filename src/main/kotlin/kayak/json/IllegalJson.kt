package kayak.json

internal object IllegalJson : Json {
  override fun type() = Json.NodeType.ILLEGAL

  override fun isDefined() = throw IllegalJsonInterpretation(Json.NodeType.ILLEGAL)

  override fun isUndefined() = throw IllegalJsonInterpretation(Json.NodeType.ILLEGAL)

  override fun isNull() = throw IllegalJsonInterpretation(Json.NodeType.ILLEGAL)

  override fun isNotNull() = throw IllegalJsonInterpretation(Json.NodeType.ILLEGAL)
}
