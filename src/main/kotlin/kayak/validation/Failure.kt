package kayak.validation

interface Failure {
  fun message(): String = "failure"

  fun details(): Map<String, Validated<*>> = emptyMap()
}
