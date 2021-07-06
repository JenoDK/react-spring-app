package com.jeno.reactspringapp.data.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.lang.NonNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Objects

@Document
data class Client (

		@NonNull
		val name: String,

		@NonNull
		val email: String,

		@DBRef(lazy = true)
		// JsonIgnore so that we do not include this in the response
		@JsonIgnore
		val picture: Picture? = null,

		@Id
		val id: String? = null,
) {
	fun equalsObject(other: Client): Boolean {
		return Objects.equals(name, other.name) && Objects.equals(email, other.email)
	}
}