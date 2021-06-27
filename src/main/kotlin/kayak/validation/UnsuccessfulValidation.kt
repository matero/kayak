package kayak.validation

@JvmInline
value class UnsuccessfulValidation<T>(override val failure: Failure) : Validated<T> {
  constructor(e: Exception) : this(e.message!!)
  constructor(message: String) : this(SimpleFailure(message))

  override val ok: Boolean
    get() = false
  override val value: T
    get() = throw IllegalStateException("Validation was unsuccessful, no value to get")
}
