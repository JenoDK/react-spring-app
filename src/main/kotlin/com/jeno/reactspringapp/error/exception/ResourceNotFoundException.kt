package com.jeno.reactspringapp.error.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(val resource: String, val field: String = "", val value: String = ""):
		RuntimeException("$resource not found for $field : $value") {
}