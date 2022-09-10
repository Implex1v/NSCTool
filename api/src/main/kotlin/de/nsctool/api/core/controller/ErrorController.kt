package de.nsctool.api.core.controller

import de.nsctool.api.core.exceptions.RestException
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.context.request.WebRequest
import javax.servlet.http.HttpServletRequest

@Controller
class ErrorController(var errorAttributes: ErrorAttributes): AbstractErrorController(errorAttributes) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RequestMapping(value = ["/error"], produces = [MediaType.APPLICATION_JSON_VALUE], method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT ])
    fun error(request: HttpServletRequest, webRequest: WebRequest): ResponseEntity<Map<String, Any>> {
        val error = errorAttributes.getError(webRequest)
        val attrs = super.getErrorAttributes(request, ErrorAttributeOptions.defaults())
        var statusCode = this.getStatus(request)

        if(error is RestException) {
            statusCode = error.statusCode
            attrs["status"] = error.statusCode.value()
            attrs["message"] = error.message
        }

        if (error != null) {
            logger.error(error.message ?: "Not found", error)
        }

        return ResponseEntity(attrs, statusCode)
    }
}