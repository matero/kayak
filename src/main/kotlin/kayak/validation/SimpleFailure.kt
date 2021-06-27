package kayak.validation

@JvmInline
value class SimpleFailure(private val message: String) : Failure {
  override fun message(): String = message
}
