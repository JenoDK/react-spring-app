package com.jeno.reactspringapp.data.repository

import com.jeno.reactspringapp.data.model.Client
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ClientRepository : MongoRepository<Client, String> {

	fun findByName(name: String): List<Client>

}