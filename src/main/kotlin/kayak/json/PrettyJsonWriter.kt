package kayak.json

import java.io.Writer

/**
 * JsonWriter that writes JSON in a human-readable form.  Instances of this class can safely be shared between threads.
 */
class PrettyJsonWriter(private val lineSeparator: String = DEFAULT_LINE_SEPARATOR) : JsonWriter() {
  override fun write(toWriter: Writer, node: Json) {
    write(toWriter, node, indent = 0)
  }

  private fun write(toWriter: Writer, node: Json, indent: Int) {
    when (node) {
      is JsonArray -> writeArrayNode(toWriter, node.asArray(), indent)
      is JsonObject -> writeObjectNode(toWriter, node.asObject(), indent)
      is JsonString -> write(toWriter, node.asString())
      is JsonNumber -> writeNumber(toWriter, node.asNumber().toCharArray())
      is JsonBoolean -> toWriter.write(node.toString())
      is JsonNull -> toWriter.write("null")
      is JsonUndefined -> throw IllegalStateException("undefined nodes are not writeable")
      is IllegalJson -> throw IllegalStateException("illegal nodes are not writeable")
    }
  }

  private fun writeNumber(toWriter: Writer, number: CharArray, offset: Int = 0, length: Int = number.size) {
    if (!JsonNumberValidator.isValid(number, offset, length)) {
      throw IllegalArgumentException("Attempted to write characters that do not conform to the JSON number specification.")
    }
    toWriter.write(number, offset, length)
  }

  private fun writeArrayNode(toWriter: Writer, arrayElements: List<Json>, indent: Int) {
    if (arrayElements.isEmpty()) {
      toWriter.write("[]")
    } else {
      toWriter.write('['.code)
      toWriter.write(lineSeparator)
      addTabs(toWriter, indent + 1)
      write(toWriter, arrayElements[0], indent + 1)
      if (arrayElements.size > 1) {
        for (index in 2 until arrayElements.size) {
          toWriter.write(','.code)
          toWriter.write(lineSeparator)
          addTabs(toWriter, indent + 1)
          write(toWriter, arrayElements[index], indent + 1)
        }
      }
      toWriter.write(lineSeparator)
      addTabs(toWriter, indent)
      toWriter.write(']'.code)
    }
  }

  private fun writeObjectNode(toWriter: Writer, objectFields: Map<JsonString, Json>, indent: Int) {
    if (objectFields.isEmpty()) {
      toWriter.write("{}")
    } else {
      toWriter.write('{'.code)
      val fieldsIterator = objectFields.iterator()
      writeField(toWriter, fieldsIterator.next(), indent)
      while (fieldsIterator.hasNext()) {
        toWriter.write(','.code)
        writeField(toWriter, fieldsIterator.next(), indent)
      }
      toWriter.write(lineSeparator)
      addTabs(toWriter, indent)
      toWriter.write('}'.code)
    }
  }

  private fun writeField(toWriter: Writer, field: Map.Entry<JsonString, Json>, indent: Int) {
    toWriter.write(lineSeparator)
    addTabs(toWriter, indent + 1)
    write(toWriter, field.key.asString())
    toWriter.write(": ")
    write(toWriter, field.value, indent + 1)
  }

  companion object {
    private val DEFAULT_LINE_SEPARATOR: String = System.getProperty("line.separator")

    private fun addTabs(toWriter: Writer, tabs: Int) {
      for (i in 0 until tabs) {
        toWriter.write('\t'.code)
      }
    }
  }
}
