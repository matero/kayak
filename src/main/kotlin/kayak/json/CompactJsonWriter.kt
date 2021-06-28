package kayak.json

import java.io.Writer

/**
 * JsonWriter that writes JSON as compactly as possible.  Instances of this class can safely be shared between threads.
 */
object CompactJsonWriter : JsonWriter() {
  override fun write(toWriter: Writer, node: Json) {
    when (node) {
      is ArrayNode -> writeArrayNode(toWriter, node.asArray())
      is ObjectNode -> writeObjectNode(toWriter, node.asObject())
      is StringValue -> write(toWriter, node.asString())
      is NumberNode -> write(toWriter, node.asNumber())
      is BooleanNode -> toWriter.write(node.toString())
      is NullNode -> toWriter.write("null")
      is UndefinedJson -> throw IllegalStateException("undefined nodes are not writeable")
      is IllegalJson -> throw IllegalStateException("illegal nodes are not writeable")
    }
  }

  private fun writeArrayNode(toWriter: Writer, arrayElements: List<Json>) {
    if (arrayElements.isEmpty()) {
      toWriter.write("[]")
    } else {
      toWriter.write('['.code)
      write(toWriter, arrayElements[0])
      if (arrayElements.size > 1) {
        for (index in 2 until arrayElements.size) {
          toWriter.write(','.code)
          write(toWriter, arrayElements[index])
        }
      }
      toWriter.write(']'.code)
    }
  }

  private fun writeObjectNode(toWriter: Writer, fields: Map<StringValue, Json>) {
    if (fields.isEmpty()) {
      toWriter.write("{}")
    } else {
      toWriter.write('{'.code)
      val fieldsIterator = fields.iterator()
      writeField(toWriter, fieldsIterator.next())
      while (fieldsIterator.hasNext()) {
        toWriter.write(','.code)
        writeField(toWriter, fieldsIterator.next())
      }
      toWriter.write('}'.code)
    }
  }

  private fun writeField(toWriter: Writer, field: Map.Entry<StringValue, Json>) {
    write(toWriter, field.key.asString())
    toWriter.write(':'.code)
    write(toWriter, field.value)
  }
}
