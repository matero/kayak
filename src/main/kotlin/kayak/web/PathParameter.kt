package kayak.web

import jakarta.servlet.http.HttpServletRequest
import kayak.validation.SuccessfulValidation
import kayak.validation.UnsuccessfulValidation
import kayak.validation.Validated

abstract class PathParameter<T>(private val name: String) {
  abstract fun loadFrom(request: HttpServletRequest): Validated<T>

  protected fun read(request: HttpServletRequest): Any {
    return request.getAttribute(name)
      ?: throw IllegalStateException("PathParameters should always be defined,. Please review your configuration")
  }
}

class IntPathParameter(named: String) : PathParameter<Int>(named) {
  override fun loadFrom(request: HttpServletRequest) =
    when (val value = read(request)) {
      is Int -> SuccessfulValidation.of(value)
      is String -> try {
        SuccessfulValidation.of(value.toInt())
      } catch (e: NumberFormatException) {
        UnsuccessfulValidation.of(e)
      }
      else -> UnsuccessfulValidation.of("can not interpret value: '$value'")
    }
}

class LongPathParameter(named: String) : PathParameter<Long>(named) {
  override fun loadFrom(request: HttpServletRequest) =
    when (val value = read(request)) {
      is Long -> SuccessfulValidation.of(value)
      is String -> try {
        SuccessfulValidation.of(value.toLong())
      } catch (e: NumberFormatException) {
        UnsuccessfulValidation.of(e)
      }
      else -> UnsuccessfulValidation.of("can not interpret value: '$value'")
    }
}
