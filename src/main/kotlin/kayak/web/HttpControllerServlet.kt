package kayak.web

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.BufferedReader

abstract class HttpControllerServlet : HttpRouterServlet() {
  protected operator fun HttpServletRequest.get(attributeName: String): Any? = this.getAttribute(attributeName)

  protected operator fun <T> HttpServletRequest.get(parameter: PathParameter<T>) = parameter.loadFrom(this)

  protected val HttpServletRequest.body: String
    get() = reader.use(BufferedReader::readText)

  protected fun HttpServletResponse.unprocessableEntity() = sendError(422)

  protected fun HttpServletResponse.notFound() = sendError(HttpServletResponse.SC_NOT_FOUND)

  /* contents updaters */
  protected fun HttpServletResponse.writeHtml(content: CharSequence) {
    contentType = "text/html"
    send(content)
  }

  protected fun HttpServletResponse.writeText(content: CharSequence) {
    contentType = "text/plain"
    send(content)
  }

  protected fun HttpServletResponse.writeJson(content: CharSequence) {
    contentType = "application/json"
    send(content)
  }

  /**
   * Writes the string content directly to the response.
   *
   *
   * This method commits the response.
   *
   * @param content the content to write into the response.
   * @throws ServletException if the response is already committed.
   */
  protected fun HttpServletResponse.send(content: CharSequence) {
    if (isCommitted) {
      throw ServletException("The response has already been committed")
    }
    commit(content.toString())
  }

  protected fun HttpServletResponse.commit(content: String) {
    if (contentType == null) {
      contentType = "text/html"
    }
    setContentLength(content.toByteArray().size)
    writer.append(content)
    status = HttpServletResponse.SC_OK
  }
}
