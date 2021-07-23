package com.jeno.reactspringapp.data.controller

import com.jeno.reactspringapp.data.model.Client
import com.jeno.reactspringapp.data.repository.ClientRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("api/clients")
class ClientController(private val clientRepository: ClientRepository) {

	@GetMapping
	fun getClients(): List<Client> {
		return clientRepository.findAll()
	}

	@GetMapping("/{id}")
	fun getClient(@PathVariable id: String): Client {
		return clientRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found") }
	}

	@GetMapping("/{id}/image", produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE])
	fun getPicture(@PathVariable id: String): ByteArray {
		return clientRepository.findById(id)
				.filter { p -> p.picture != null && p.picture!!.picture != null }
				.map { p -> p.picture!!.picture!!.data }
				.orElseGet { ByteArray(0) }
	}

}