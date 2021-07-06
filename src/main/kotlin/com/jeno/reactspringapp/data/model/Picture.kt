package com.jeno.reactspringapp.data.model

import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Picture(

		val name: String?,

		val picture: Binary? = null,

		@Id
		val id: String? = null,
)