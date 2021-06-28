package kayak.validation

interface Validated<T> {
  val ok: Boolean
  val failed: Boolean
  val value: T
  val failure: Failure
}

private object Valid : Validated<Unit> {
  override val ok: Boolean = true
  override val failed: Boolean = false
  override val value: Unit
    get() = throw IllegalStateException("Validation was successful, but no value provided")
  override val failure: Failure
    get() = throw IllegalStateException("Validation was successful, no failure detected")
}


@Suppress("UNCHECKED_CAST") fun <T> validate(v1: Pair<String, Validated<*>>, v2: Pair<String, Validated<*>>): Validated<T> {
  return if (v1.second.ok) {
    if (v2.second.ok) {
      Valid as Validated<T>
    } else {
      UnsuccessfulValidation(v2.second.failure)
    }
  } else {
    if (v2.second.ok) {
      UnsuccessfulValidation(v1.second.failure)
    } else {
      UnsuccessfulValidation(MultipleFailures(mapOf(v1, v2)))
    }
  }
}

@Suppress("UNCHECKED_CAST") fun <T> validate(vararg validations: Pair<String, Validated<*>>): Validated<T> {
  return when (validations.size) {
    0 -> Valid as Validated<T>
    1 -> if (validations[0].second.ok) Valid as Validated<T> else UnsuccessfulValidation(validations[0].second.failure)
    2 -> validate(validations[0], validations[1])
    else -> validate(validations.filter { it.second.failed })
  }
}

fun <T> validate(validations: Iterable<Pair<String, Validated<*>>>): Validated<T> {
  return validate(validations.filter { it.second.failed })
}

@Suppress("UNCHECKED_CAST") fun <T> validate(validations: List<Pair<String, Validated<*>>>): Validated<T> {
  return when (validations.size) {
    0 -> Valid as Validated<T>
    1 -> if (validations[0].second.ok) Valid as Validated<T> else UnsuccessfulValidation(validations[0].second.failure)
    2 -> validate(validations[0], validations[1])
    else -> {
      val failedValidations = validations.filter { it.second.failed }
      return when (failedValidations.size) {
        0 -> Valid as Validated<T>
        1 -> UnsuccessfulValidation(failedValidations.first().second.failure)
        else -> UnsuccessfulValidation(MultipleFailures(failedValidations.toMap()))
      }
    }
  }
}

@Suppress("UNCHECKED_CAST") fun <T> validate(validations: Map<String, Validated<*>>): Validated<T> {
  return if (validations.isEmpty())
    Valid as Validated<T>
  else
    validate(validations.filter { it.value.failed }.toList())
}
