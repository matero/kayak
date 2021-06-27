package kayak.web

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import java.text.MessageFormat
import java.util.*
import java.util.regex.Pattern

abstract class HttpRouterServlet : jakarta.servlet.http.HttpServlet() {
  protected open fun notAuthorized(response: HttpServletResponse) {
    response.status = HttpServletResponse.SC_FORBIDDEN
  }

  /**
   * Receives standard HTTP requests from the public `service` method and dispatches them to the
   * `do`*XXX* methods defined in this class. This method is an HTTP-specific version of the
   * [jakarta.servlet.Servlet.service] method. There's no need to override this method.
   *
   * @param request the [HttpServletRequest] object that contains the request the client made of the servlet
   *
   * @param response the [HttpServletResponse] object that contains the response the servlet returns to the client
   *
   * @throws IOException if an input or output error occurs while the servlet is handling the HTTP request
   *
   * @throws ServletException if the HTTP request cannot be handled
   *
   * @see jakarta.servlet.Servlet.service
   */
  @Throws(ServletException::class, IOException::class)
  override fun service(request: HttpServletRequest, response: HttpServletResponse) {
    when (request.method) {
      "GET" -> {
        val lastModified = getLastModified(request)
        if (lastModified == -1L) {
          // servlet doesn't support if-modified-since, no reason to go through further expensive logic
          doGet(request, response)
        } else {
          val ifModifiedSince = request.getDateHeader("If-Modified-Since")
          if (ifModifiedSince < lastModified) {
            // If the servlet mod time is later, call doGet()
            // Round down to the nearest second for a proper compare
            // A ifModifiedSince of -1 will always be less
            maybeSetLastModified(response, lastModified)
            doGet(request, response)
          } else {
            response.status = HttpServletResponse.SC_NOT_MODIFIED
          }
        }
      }
      "HEAD" -> {
        val lastModified = getLastModified(request)
        maybeSetLastModified(response, lastModified)
        doHead(request, response)
      }
      "POST" -> doPost(request, response)
      "PUT" -> doPut(request, response)
      "PATCH" -> doPatch(request, response)
      "DELETE" -> doDelete(request, response)
      "OPTIONS" -> doOptions(request, response)
      "TRACE" -> doTrace(request, response)
      else -> {
        //
        // Note that this means NO servlet supports whatever method was requested, anywhere on this server.
        //
        var errMsg = localizedMessage("http.method_not_implemented")
        val errArgs = arrayOf(request.method)
        errMsg = MessageFormat.format(errMsg, *errArgs)
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, errMsg)
      }
    }
  }

  /**
   * Called by the server (via the `service` method) to allow a servlet to handle a PATCH request.
   *<p/>
   * When overriding this method, read the request data, write the response headers, get the response's writer or output stream object, and finally,
   * write the response data. It's best to include content type and encoding. When using a `PrintWriter` object to return the response, set the
   * content type before accessing the `PrintWriter` object.
   *<p/>
   * The servlet container must write the headers before committing the response, because in HTTP the headers must be sent before the response body.
   *<p/>
   * Where possible, set the Content-Length header (with the [jakarta.servlet.ServletResponse.setContentLength]  method), to allow the servlet
   * container to use a persistent connection to return its response to the client, improving performance. The content length is automatically set if
   * the entire response fits inside the response buffer.
   *<p/>
   * When using HTTP 1.1 chunked encoding (which means that the response has a Transfer-Encoding header), do not set the Content-Length header.
   *<p/>
   * This method does not need to be either safe or idempotent. Operations requested through PATCH can have side effects for which the user can be
   * held accountable, for example, updating stored data or buying items online.
   *<p/>
   * If the HTTP PATCH request is incorrectly formatted, `doPost` returns an HTTP "Bad Request" message.
   *<p/>
   * @param request an [HttpServletRequest] object that contains the request the client has made of the servlet
   * @param response an [HttpServletResponse] object that contains the response the servlet sends to the client
   *
   * @throws IOException if an input or output error is detected when the servlet handles the request
   * @throws ServletException if the request for the POST could not be handled
   *
   * @see jakarta.servlet.ServletOutputStream
   * @see jakarta.servlet.ServletResponse.setContentType
   */
  fun doPatch(request: HttpServletRequest, response: HttpServletResponse) = unknownPatchPath(request, response)

  protected open fun unknownGetPath(request: HttpServletRequest, response: HttpServletResponse) = super.doGet(request, response)

  protected open fun unknownDeletePath(request: HttpServletRequest, response: HttpServletResponse) = super.doDelete(request, response)

  protected open fun unknownHeadPath(request: HttpServletRequest, response: HttpServletResponse) = super.doHead(request, response)

  protected open fun unknownOptionsPath(request: HttpServletRequest, response: HttpServletResponse) = super.doOptions(request, response)

  protected open fun unknownPostPath(request: HttpServletRequest, response: HttpServletResponse) = super.doPost(request, response)

  protected open fun unknownPutPath(request: HttpServletRequest, response: HttpServletResponse) = super.doPut(request, response)

  protected open fun unknownPatchPath(request: HttpServletRequest, response: HttpServletResponse) {
    response.sendError(getMethodNotSupportedCode(request.protocol), localizedMessage("http.method_patch_not_supported") ?: "PATCH not supported")
  }

  /**
   * Sets the Last-Modified entity header field, if it has not already been set and if the value is meaningful.
   * <p/>
   * Called before doGet, to ensure that headers are set before response data is written. A subclass might have set this header already, so we check.
   *
   * @param response response to update
   * @param lastModified value to set as header `Last-Modified`.
   */
  private fun maybeSetLastModified(response: HttpServletResponse, lastModified: Long) {
    if (response.containsHeader("Last-Modified")) return
    if (lastModified >= 0) response.setDateHeader("Last-Modified", lastModified)
  }

  protected open fun unknownTracePath(request: HttpServletRequest, response: HttpServletResponse) = super.doTrace(request, response)

  companion object {
    private val localizedMessages = ResourceBundle.getBundle("jakarta.servlet.http.LocalStrings")

    private val pathVariable = Pattern.compile("\\{(\\w+)}")

    protected sealed interface Path {
      fun matches(request: HttpServletRequest): Boolean
    }

    /** Path that represents the INDEX or ROOT path `"/"`.  */
    object IndexPath : Path {
      override fun matches(request: HttpServletRequest): Boolean {
        val pathInfo = request.pathInfo
        return null == pathInfo || pathInfo.isEmpty() || "/" == pathInfo
      }

      override fun toString(): String {
        return "Path('/')"
      }
    }

    @JvmInline
    internal value class StaticPath constructor(private val value: String) : Path {
      override fun toString() = "Path('$value')"

      override fun matches(request: HttpServletRequest) = value == request.pathInfo
    }

    internal class ParameterizedPath constructor(
      private val uri: String,
      private val regex: Pattern,
      private val parameters: Array<String>
    ) : Path {
      override fun toString() = "Path('$uri')"

      override fun hashCode() = uri.hashCode()

      override fun equals(other: Any?) = (this === other) || (if (other is ParameterizedPath) uri == other.uri else false)


      override fun matches(request: HttpServletRequest): Boolean {
        if (!IndexPath.matches(request)) {
          val matcher = regex.matcher(request.pathInfo)
          if (matcher.matches()) {
            for (parameter in parameters) {
              val group = matcher.group(parameter)
              request.setAttribute(parameter, group)
            }
            return true
          }
        }
        return false
      }
    }

    protected fun path(value: String): Path {
      if (value.isEmpty() || "/" == value) {
        return IndexPath
      }

      val matcher = pathVariable.matcher(value)
      return if (matcher.find()) {
        val parameters = ArrayList<String>(2)
        val parameterizedUri = StringBuilder()
        do {
          val parameter = matcher.group(1)
          val parameterRegEx = "(?<$parameter>[^/]+)"
          matcher.appendReplacement(parameterizedUri, parameterRegEx)
          parameters.add(parameter)
        } while (matcher.find())
        ParameterizedPath(value, Pattern.compile(parameterizedUri.toString()), parameters.toTypedArray())
      } else {
        StaticPath(value)
      }
    }

    private fun localizedMessage(key: String): String? = localizedMessages.getString(key)

    private fun getMethodNotSupportedCode(protocol: String): Int {
      return when (protocol) {
        "HTTP/0.9", "HTTP/1.0" -> HttpServletResponse.SC_BAD_REQUEST
        else -> HttpServletResponse.SC_METHOD_NOT_ALLOWED
      }
    }
  }
}
