package kayak.validation

@JvmInline
value class MultipleFailures(private val details: Map<String, Validated<*>>) : Failure {
  override fun details(): Map<String, Validated<*>> = details
}
