package com.jeno.reactspringapp.data.controller

import com.jeno.reactspringapp.data.repository.PictureRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/pictures")
class PictureController (val pictureRepository: PictureRepository) {

	@GetMapping("/{id}", produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE])
	fun getPicture(@PathVariable id: String): ByteArray {
		return pictureRepository.findById(id)
				.filter { p -> p.picture != null }.
				map { p -> p.picture!!.data }
				.orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found") }
	}

}