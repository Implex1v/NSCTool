package de.nsctool.api.authentication

import de.nsctool.api.core.exceptions.RestException
import org.springframework.http.HttpStatus

class AuthenticationException(message: String, throwable: Throwable?)
    : RestException(HttpStatus.UNAUTHORIZED, message, throwable)