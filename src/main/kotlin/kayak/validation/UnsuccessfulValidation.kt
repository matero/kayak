package kayak.validation

@JvmInline
value class UnsuccessfulValidation<T> private constructor(override val failure: Failure) : Validated<T> {
  override val ok: Boolean
    get() = false
  override val failed: Boolean
    get() = true
  override val value: T
    get() = throw IllegalStateException("Validation was unsuccessful, no value to get")

  companion object {
    fun <T> of(e: Exception): Validated<T> = of(e.message!!)
    fun <T> of(message: String): Validated<T> = of(SimpleFailure(message))
    fun <T> of(failure: Failure): Validated<T> = UnsuccessfulValidation(failure)
  }
}
