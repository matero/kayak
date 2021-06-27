package kayak

abstract class KayakFailure : RuntimeException {
  protected constructor(message: String) : super(message)

  protected constructor(message: String, cause: Throwable) : super(message, cause)

  protected constructor(
    message: String,
    cause: Throwable,
    enableSuppression: Boolean,
    writableStackTrace: Boolean
  ) : super(message, cause, enableSuppression, writableStackTrace)
}

class KayakConfigurationFailure : KayakFailure {
  constructor(message: String) : super(message)

  constructor(message: String, cause: Throwable) : super(message, cause)
}
