package com.jeno.reactspringapp.error.exception

class ValidationException(val errorMap: Map<String, Any>): RuntimeException("Errors: $errorMap") {

	constructor(fieldName: String, error: String) : this(mapOf(fieldName to error))

}