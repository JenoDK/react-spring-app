package com.jeno.reactspringapp.data.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.lang.NonNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(

		@NonNull
		var name: String,

		@NonNull
		@Indexed(unique = true)
		val email: String,

		@JsonIgnore
		val password: String? = null,

		@NonNull
		val provider: AuthProviderType,

		@JsonIgnore
		val providerId: String? = null,

		@DBRef(lazy = true)
		val profilePicture: Picture? = null,

		@Id
		val id: String? = null,
) {
}