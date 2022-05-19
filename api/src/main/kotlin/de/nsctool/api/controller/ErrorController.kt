package de.nsctool.api.controller

import de.nsctool.api.controller.exceptions.RestException
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.context.request.WebRequest
import javax.servlet.http.HttpServletRequest

@Controller
class ErrorController(var errorAttributes: ErrorAttributes): AbstractErrorController(errorAttributes) {
    @GetMapping(value = ["/error"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun error(request: HttpServletRequest, webRequest: WebRequest): ResponseEntity<Map<String, Any>> {
        val error = errorAttributes.getError(webRequest)
        val attrs = super.getErrorAttributes(request, ErrorAttributeOptions.defaults())
        var statusCode = this.getStatus(request)

        if(error is RestException) {
            statusCode = error.statusCode
            attrs["status"] = error.statusCode.value()
            attrs["message"] = error.message
        }

        return ResponseEntity(attrs, statusCode)
    }
}