package de.nsctool.api.exceptions

import org.springframework.http.HttpStatus

/**
 * Base class for handles exceptions directly mapped to status codes.
 *
 * [statusCode] the returned status code
 *
 * [message] will be exposed by the api in the error response
 *
 * [cause] optional the cause which caused this exception
 */
abstract class RestException(var statusCode: HttpStatus, message: String, cause: Throwable? = null): RuntimeException(message, cause)

class NotFoundException(message: String, throwable: Throwable? = null): RestException(HttpStatus.NOT_FOUND, message, throwable)
class BadRequestException(message: String, throwable: Throwable? = null): RestException(HttpStatus.BAD_REQUEST, message, throwable)
class UnauthorizedException(message: String, throwable: Throwable? = null): RestException(HttpStatus.UNAUTHORIZED, message, throwable)
