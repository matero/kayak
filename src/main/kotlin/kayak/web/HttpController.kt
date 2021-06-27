package kayak.web

import kayak.validation.Validated
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

abstract class HttpController : HttpServlet() {
    protected fun HttpServletRequest.matches(path: Path): Boolean {
        return true
    }

    protected operator fun HttpServletRequest.get(attributeName: String): Any {
        return this.getAttribute(attributeName)
    }

    protected operator fun <T> HttpServletRequest.get(parameter: PathParameter<T>): Validated<T> =
        parameter.loadFrom(this)

    protected open fun unknownGetPath(request: HttpServletRequest, response: HttpServletResponse) {
        super.doGet(request, response)
    }
}
