package com.jeno.reactspringapp.data.demo

import com.jeno.reactspringapp.data.model.Client
import com.jeno.reactspringapp.data.model.Picture
import com.jeno.reactspringapp.data.repository.ClientRepository
import com.jeno.reactspringapp.data.repository.PictureRepository
import org.bson.types.Binary
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class ClientDemoDataCommandLineRunner(
		val clientRepository: ClientRepository,
		val pictureRepository: PictureRepository) : CommandLineRunner {

	override fun run(vararg args: String?) {
		clientRepository.deleteAll()
		val jenoPicture = Picture("jeno.jpg", Binary(javaClass.getResource("/demo/jeno.jpg").readBytes()))
		val odinPicture = Picture("jeno.jpg", Binary(javaClass.getResource("/demo/odin.jpg").readBytes()))
		val landoPicture = Picture("jeno.jpg", Binary(javaClass.getResource("/demo/lando.jpg").readBytes()))
		val jeno = Client("Jeno", "jenodekeyzer@gmail.com", pictureRepository.save(jenoPicture))
		val odin = Client("Odin", "odin.dekeyzer@gmail.com", pictureRepository.save(odinPicture))
		val lando = Client("Lando", "lando.de.keyzer@gmail.com", pictureRepository.save(landoPicture))

		val clients = listOf(jeno, odin, lando)
		if (!clientRepository.findAll().any { c -> clients.any { client -> client.equalsObject(c) } }) {
			clientRepository.saveAll(clients)
		}
	}

}