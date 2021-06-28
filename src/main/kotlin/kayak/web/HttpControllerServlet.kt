package kayak.web

import kayak.json.Json

abstract class HttpControllerServlet() : HttpRouterServlet() {

  protected operator fun Request.get(attributeName: String): Any? = this.getAttribute(attributeName)

  protected operator fun <T> Request.get(parameter: PathParameter<T>) = parameter.loadFrom(this)

  protected val Request.body: String
    get() = reader.use(java.io.BufferedReader::readText)

  protected val Request.jsonBody: Json
    get() = Json.parse(reader)

  protected fun Response.unprocessableEntity() = sendError(422)

  protected fun Response.notFound() = sendError(Response.SC_NOT_FOUND)

  /* contents updaters */
  protected fun Response.writeHtml(content: CharSequence) {
    contentType = "text/html"
    send(this, content)
  }

  protected fun Response.writeText(content: CharSequence) {
    contentType = "text/plain"
    send(this, content)
  }

  protected fun Response.writeJson(content: CharSequence) {
    contentType = "application/json"
    send(this, content)
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
  protected open fun send(response: Response, content: CharSequence) {
    if (response.isCommitted) {
      throw jakarta.servlet.ServletException("The response has already been committed")
    }
    commit(response, content.toString())
  }

  protected open fun commit(response: Response, content: String) {
    if (response.contentType == null) {
      response.contentType = "text/html"
    }
    response.setContentLength(content.toByteArray().size)
    response.writer.append(content)
    response.status = Response.SC_OK
  }
}
