package kayak

object AppPort {
  private const val DEFAULT_HTTP_PORT = 8080

  val value = readConfiguration()

  /**
   * Reads the port that application should run on (it can be set into an environment variable named `PORT`).
   *
   *
   * If `PORT` is not defined, defaults to [.DEFAULT_HTTP_PORT].
   *
   * @return the port to be used to serve the application.
   */
  private fun readConfiguration(): Int {
    val log = org.slf4j.LoggerFactory.getLogger("kayak.AppPort")
    val value = System.getenv("PORT")

    if (value == null || value.isEmpty()) {
      log.warn("No PORT definition found, serving application on '{}'", DEFAULT_HTTP_PORT)
      return DEFAULT_HTTP_PORT
    }

    log.trace("PORT definition found, trying to use PORT='{}'", value)

    val port = try {
      value.toInt()
    } catch (e: NumberFormatException) {
      throw KayakConfigurationFailure("PORT should be a positive integer, but it is configured as '$value'", e)
    }
    if (port < 0) {
      throw KayakConfigurationFailure("PORT should be a positive integer, but it is configured as '$value'")
    }

    log.info("PORT definition found, serving application on '{}'", port)
    return port
  }
}
