package kayak.validation

interface Validated<T> {
  val ok: Boolean
  val value: T
  val failure: Failure
}
