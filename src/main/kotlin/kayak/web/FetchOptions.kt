package kayak.web

import jakarta.servlet.http.HttpServletRequest
import kayak.validation.SuccessfulValidation
import kayak.validation.UnsuccessfulValidation
import kayak.validation.Validated
import kayak.validation.validate

class FetchOptions private constructor(
  val chunkSize: ChunkSize,
  val limit: Limit,
  val offset: Offset,
  val prefetchSize: PrefetchSize,
  val orderBy: OrderBy
) {
  @JvmInline
  value class ChunkSize private constructor(val value: Int) {
    companion object {
      private const val queryParameterName = "chunk"
      internal val DEFAULT = ChunkSize(value = 500)
      private val VALIDATED_DEFAULT = SuccessfulValidation.of(DEFAULT)

      fun at(request: HttpServletRequest): Validated<ChunkSize> {
        val parameter = request.getParameter(queryParameterName)
        return if (parameter == null)
          VALIDATED_DEFAULT
        else {
          try {
            val value = parameter.toInt()
            if (value == DEFAULT.value)
              VALIDATED_DEFAULT
            else
              SuccessfulValidation.of(ChunkSize(value))
          } catch (e: NumberFormatException) {
            UnsuccessfulValidation.of("Illegal $queryParameterName: ${e.message}")
          }
        }
      }
    }
  }

  @JvmInline
  value class Limit private constructor(val value: Int) {
    companion object {
      private const val queryParameterName = "limit"
      internal val DEFAULT = Limit(value = 500)
      private val VALIDATED_DEFAULT = SuccessfulValidation.of(DEFAULT)

      fun at(request: HttpServletRequest): Validated<Limit> {
        val parameter = request.getParameter(queryParameterName)
        return if (parameter == null)
          VALIDATED_DEFAULT
        else {
          try {
            val value = parameter.toInt()
            if (value == DEFAULT.value)
              VALIDATED_DEFAULT
            else
              SuccessfulValidation.of(Limit(value))
          } catch (e: NumberFormatException) {
            UnsuccessfulValidation.of("Illegal $queryParameterName: ${e.message}")
          }
        }
      }
    }
  }

  @JvmInline
  value class Offset private constructor(val value: Int) {
    companion object {
      private const val queryParameterName = "offset"
      internal val DEFAULT = Offset(value = 500)
      private val VALIDATED_DEFAULT = SuccessfulValidation.of(DEFAULT)

      fun at(request: HttpServletRequest): Validated<Offset> {
        val parameter = request.getParameter(queryParameterName)
        return if (parameter == null)
          VALIDATED_DEFAULT
        else {
          try {
            val value = parameter.toInt()
            if (value == DEFAULT.value)
              VALIDATED_DEFAULT
            else
              SuccessfulValidation.of(Offset(value))
          } catch (e: NumberFormatException) {
            UnsuccessfulValidation.of("Illegal $queryParameterName: ${e.message}")
          }
        }
      }
    }
  }

  @JvmInline
  value class PrefetchSize private constructor(val value: Int) {
    companion object {
      private const val queryParameterName = "prefetch"
      internal val DEFAULT = PrefetchSize(value = 500)
      private val VALIDATED_DEFAULT = SuccessfulValidation.of(DEFAULT)

      fun at(request: HttpServletRequest): Validated<PrefetchSize> {
        val parameter = request.getParameter(queryParameterName)
        return if (parameter == null)
          VALIDATED_DEFAULT
        else {
          try {
            val value = parameter.toInt()
            if (value == DEFAULT.value)
              VALIDATED_DEFAULT
            else
              SuccessfulValidation.of(PrefetchSize(value))
          } catch (e: NumberFormatException) {
            UnsuccessfulValidation.of("Illegal $queryParameterName: ${e.message}")
          }
        }
      }
    }
  }

  @JvmInline
  value class OrderBy private constructor(private val orders: Array<Order>) {
    fun isDefined() = orders.isNotEmpty()

    enum class Direction { ASC, DESC }

    @JvmInline
    value class Property(val value: String) {
      companion object {
        fun of(value: String): Validated<Property> =
          when {
            value.isEmpty() ->
              UnsuccessfulValidation.of("Illegal $queryParameterName.Property: value can not be empty")
            value.isBlank() ->
              UnsuccessfulValidation.of("Illegal $queryParameterName.Property: value can not be blank")
            else -> SuccessfulValidation.of(Property(value.trim()))
          }
      }
    }

    class Order(val dir: Direction = Direction.ASC, val property: Property)

    companion object {
      private const val queryParameterName = "orderBy"
      internal val DEFAULT = OrderBy(emptyArray())
      private val VALIDATED_DEFAULT = SuccessfulValidation.of(DEFAULT)

      fun at(request: HttpServletRequest): Validated<OrderBy> {
        val parameter = request.getParameterValues(queryParameterName)
        return if (parameter == null || parameter.isEmpty())
          VALIDATED_DEFAULT
        else {
          val orders = ArrayList<Order>(parameter.size)
          for (value in parameter) {
            when {
              value == null ->
                return UnsuccessfulValidation.of("Illegal $queryParameterName.Property: value can not be null")
              value.isEmpty() ->
                return UnsuccessfulValidation.of("Illegal $queryParameterName.Property: value can not be empty")
              value.isBlank() ->
                return UnsuccessfulValidation.of("Illegal $queryParameterName.Property: value can not be blank")
              value.startsWith('-') -> {
                val property = Property.of(value.substring(1))
                if (property.ok)
                  orders.add(Order(Direction.DESC, property.value))
                else
                  return UnsuccessfulValidation.of(property.failure)
              }
              value.startsWith('+') -> {
                val property = Property.of(value.substring(1))
                if (property.ok)
                  orders.add(Order(Direction.ASC, property.value))
                else
                  return UnsuccessfulValidation.of(property.failure)
              }
              else -> {
                val property = Property.of(value)
                if (property.ok)
                  orders.add(Order(Direction.ASC, property.value))
                else
                  return UnsuccessfulValidation.of(property.failure)
              }
            }

          }
          SuccessfulValidation.of(OrderBy(orders.toTypedArray()))
        }
      }
    }
  }

  companion object {
    private val DEFAULT_FETCH_OPTIONS =
      FetchOptions(ChunkSize.DEFAULT, Limit.DEFAULT, Offset.DEFAULT, PrefetchSize.DEFAULT, OrderBy.DEFAULT)
    private val VALIDATED_DEFAULT_FETCH_OPTIONS = SuccessfulValidation.of(DEFAULT_FETCH_OPTIONS)

    fun at(request: HttpServletRequest): Validated<FetchOptions> {
      val chunkSize = ChunkSize.at(request)
      val limit = Limit.at(request)
      val offset = Offset.at(request)
      val prefetchSize = PrefetchSize.at(request)
      val orderBy = OrderBy.at(request)

      val properties = validate<FetchOptions>(
        "chunkSize" to chunkSize,
        "limit" to limit,
        "offset" to offset,
        "prefetchSize" to prefetchSize,
        "orderBy" to orderBy)

      return if (properties.ok) {
        if (chunkSize.value == ChunkSize.DEFAULT
          && limit.value == Limit.DEFAULT
          && offset.value == Offset.DEFAULT
          && prefetchSize.value == PrefetchSize.DEFAULT
          && orderBy.value == OrderBy.DEFAULT
        ) {
          VALIDATED_DEFAULT_FETCH_OPTIONS
        } else {
          SuccessfulValidation.of(
            FetchOptions(chunkSize.value, limit.value, offset.value, prefetchSize.value, orderBy.value)
          )
        }
      } else {
        properties
      }
    }
  }
}
