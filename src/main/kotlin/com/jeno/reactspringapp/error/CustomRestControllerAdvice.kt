package com.jeno.reactspringapp.error

import com.jeno.reactspringapp.error.exception.ResourceNotFoundException
import com.jeno.reactspringapp.error.exception.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CustomRestControllerAdvice: ResponseEntityExceptionHandler() {

	companion object {
		val LOG = LoggerFactory.getLogger(CustomRestControllerAdvice::class.java)!!
	}

	@ExceptionHandler(ValidationException::class)
	fun validationException(validationException: ValidationException): ResponseEntity<Map<String, Any>> {
		LOG.info("Validation went wrong $validationException", validationException)
		return ResponseEntity<Map<String, Any>>(validationException.errorMap, HttpStatus.BAD_REQUEST)
	}

	@ExceptionHandler(ResourceNotFoundException::class)
	fun resourceNotFoundException(resourceNotFoundException: ResourceNotFoundException): ResponseEntity<String> {
		return ResponseEntity<String>(resourceNotFoundException.message, HttpStatus.NOT_FOUND)
	}

	@ExceptionHandler(AuthenticationException::class)
	fun resourceNotFoundException(authException: AuthenticationException): ResponseEntity<String> {
		return ResponseEntity<String>(authException.message, HttpStatus.UNAUTHORIZED)
	}
}