package kayak.validation

@JvmInline
value class SuccessfulValidation<T> private constructor(private val v: Any?) : Validated<T> {
  override val ok: Boolean
    get() = true
  override val failed: Boolean
    get() = false

  @Suppress("UNCHECKED_CAST")
  override val value: T
    get() = v as T

  override val failure: Failure
    get() = throw IllegalStateException("Validation was successful, no failure detected")

  companion object {
    fun <T> of(value: T): SuccessfulValidation<T> = SuccessfulValidation(value)
  }
}
