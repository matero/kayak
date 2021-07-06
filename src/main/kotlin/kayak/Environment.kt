package kayak

import kayak.json.CompactJsonWriter
import kayak.json.JsonFormatter
import kayak.json.JsonWriter
import kayak.json.PrettyJsonWriter

/** Environment in which barman is running.  */
enum class Environment {
  /** development environment, this is the default.  */
  DEVELOPMENT,

  /** unit testing environment, defined by surefire.  */
  UNIT_TEST,

  /** integration testing environment, defined by failsafe.  */
  INTEGRATION_TEST,

  /** acceptance testing environment, defined by failsafe.  */
  ACCEPTANCE_TEST,

  /** integration testing environment, defined by launcher / user.  */
  QUALITY_ASSURANCE,

  /** integration testing environment, defined by launcher / user.  */
  PRODUCTION;

  /**
   * @return true if the current environment is equivalent to [Environment.PRODUCTION]; false other way.
   * @see Environment.PRODUCTION
   *
   * @see Environment.CURRENT
   */
  val isProduction: Boolean
    get() = current == PRODUCTION

  /**
   * @return true if the current environment is equivalent to [Environment.QUALITY_ASSURANCE]; false other way.
   * @see Environment.QUALITY_ASSURANCE
   *
   * @see Environment.CURRENT
   */
  val isQA: Boolean
    get() = current == QUALITY_ASSURANCE

  /**
   * @return true if the current environment is equivalent to [Environment.DEVELOPMENT]; false other way.
   * @see Environment.DEVELOPMENT
   *
   * @see Environment.CURRENT
   */
  val isDevelopment: Boolean
    get() = current == DEVELOPMENT

  /**
   * @return true if the current environment is a testing one; false other way.
   * @see Environment.QUALITY_ASSURANCE
   *
   * @see Environment.ACCEPTANCE_TEST
   *
   * @see Environment.INTEGRATION_TEST
   *
   * @see Environment.UNIT_TEST
   */
  val isTesting: Boolean
    get() {
      return current == ACCEPTANCE_TEST || current == INTEGRATION_TEST || current == UNIT_TEST
    }

  val defaultJsonWriter: JsonWriter by lazy { if (isDevelopment || isTesting) PrettyJsonWriter() else CompactJsonWriter }

  val defaultJsonFormatter: JsonFormatter by lazy { JsonFormatter(defaultJsonWriter) }

  companion object {
    /** currently active environment.  */
    val current = readConfiguration()

    private fun readConfiguration(): Environment {
      val logger = org.slf4j.LoggerFactory.getLogger("kayak.Environment")
      val value = System.getenv("ENVIRONMENT")

      return if (value == null || value.isEmpty() || value.isBlank()) {
        logger.warn("No ENVIRONMENT definition found, using 'DEVELOPMENT'")
        DEVELOPMENT
      } else {
        val environment = try {
          valueOf(value)
        } catch (unknownValue: IllegalArgumentException) {
          throw KayakConfigurationFailure("PORT should be one of ${values()}, but it is configured as '$value'")
        }
        logger.info("ENVIRONMENT configured to '{}'", environment)
        environment
      }
    }
  }
}
