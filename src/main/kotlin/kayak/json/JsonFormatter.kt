package kayak.json

/**
 * A `JsonFormatter` provides operations to turn `Json`s into valid JSON text.
 */
class JsonFormatter(private val jsonWriter: JsonWriter) {
  /**
   * Returns the specified `JsonNode` formatted as a String.
   *
   * @param json the `JsonNode` to format.
   * @return the specified `JsonNode` formatted as a String.
   */
  fun format(json: Json): String {
    val writer = java.io.StringWriter()
    try {
      format(json, toWriter = writer)
    } catch (e: java.io.IOException) {
      throw IllegalStateException("failed to format json node", e)
    }
    return writer.toString()
  }

  /**
   * Streams the specified `JsonNode` formatted to the specified `Writer`.
   *
   * @param json the `JsonNode` to format.
   * @param toWriter       the `Writer` to stream the formatted `JsonNode` to.
   * @throws java.io.IOException if there was a problem writing to the `Writer`.
   */
  fun format(json: Json, toWriter: java.io.Writer) {
    jsonWriter.write(toWriter, json)
  }
}
