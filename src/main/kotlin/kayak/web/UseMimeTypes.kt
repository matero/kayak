package kayak.web

import java.lang.IllegalArgumentException
import java.util.*

interface UseMimeTypes {

  object application {
    operator fun div(selector: ApplicationContentTypeSelector): MediaType {
      when (selector) {
        json -> ApplicationMediaType.APPLICATION_JSON
        xml -> ApplicationMediaType.APPLICATION_XML
      }
      throw IllegalArgumentException("""unknown ContentType: "application/$selector"""")
    }
  }

  sealed interface ApplicationContentTypeSelector

  object json : ApplicationContentTypeSelector
  object xml : ApplicationContentTypeSelector

  fun Request.headers(name: String): Enumeration<String> =
    getHeaders(name) ?: throw ServletException("request headers can not be accessed, please update your server configuration")

  fun Request.accepts(contentType: String): Boolean = accepts(MediaType.of(contentType))

  fun Request.accepts(mediaType: MediaType): Boolean {
    val values = headers("Accept")

    if (!values.hasMoreElements()) {
      return true // not defined -> accepts anything
    }

    do {
      val header = values.nextElement()

      if (mediaType.matches(header)) {
        return true
      }
    } while (values.hasMoreElements())

    return false
  }
}
