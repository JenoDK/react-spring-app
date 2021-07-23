package com.jeno.reactspringapp.data.repository

import com.jeno.reactspringapp.data.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {

	fun findByEmail(email: String): User?

	fun findByProviderId(providerId: String): User?

	fun existsByEmail(email: String): Boolean

}