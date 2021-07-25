package kayak.validation

import kayak.json.Json

@JvmInline
value class SimpleFailure(private val message: String) : Failure {
  override fun message(): String = message

  override fun asJson() = Json.of(message)
}
