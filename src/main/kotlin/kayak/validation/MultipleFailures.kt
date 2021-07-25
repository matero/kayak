package kayak.validation

import kayak.json.Json

@JvmInline
value class MultipleFailures internal constructor(private val details: Map<String, Failure>) : Failure {
  override fun details(): Map<String, Failure> = details

  override fun asJson(): Json {
    val fields = mutableMapOf<String, Json>()
    details.forEach {
      fields[it.key] = it.value.asJson()
    }
    return Json.of(fields)
  }

  companion object {
    internal fun of(failedValidations: List<Pair<String, Validated<*>>>): MultipleFailures {
      val failures = mutableMapOf<String, Failure>()
      failedValidations.forEach {
        failures[it.first] = it.second.failure
      }
      return MultipleFailures(failures)
    }
  }
}
