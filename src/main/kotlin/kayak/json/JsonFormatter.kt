package kayak.json

/**
 * A `JsonFormatter` provides operations to turn `JsonNode`s into valid JSON text.
 */
class JsonFormatter(private val jsonWriter: JsonWriter) {
  /**
   * Returns the specified `JsonNode` formatted as a String.
   *
   * @param jsonNode the `JsonNode` to format.
   * @return the specified `JsonNode` formatted as a String.
   */
  fun format(jsonNode: JsonNode): String {
    val json = java.io.StringWriter()
    try {
      format(jsonNode, toWriter = json)
    } catch (e: java.io.IOException) {
      throw IllegalStateException("failed to format json node", e)
    }
    return json.toString()
  }

  /**
   * Streams the specified `JsonNode` formatted to the specified `Writer`.
   *
   * @param jsonNode the `JsonNode` to format.
   * @param writer       the `Writer` to stream the formatted `JsonNode` to.
   * @throws IOException if there was a problem writing to the `Writer`.
   */
  fun format(jsonNode: JsonNode, toWriter: java.io.Writer) {
    jsonWriter.write(toWriter, jsonNode)
  }
}
