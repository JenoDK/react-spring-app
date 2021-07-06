package com.jeno.reactspringapp.data

import com.jeno.reactspringapp.data.model.Client
import com.jeno.reactspringapp.data.repository.ClientRepository
import org.bson.types.Binary
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.nio.file.Files
import java.nio.file.Paths


@DataMongoTest
@ExtendWith(SpringExtension::class)
class ClientIntegrationTest {

	@Test
	fun testAddingClients(@Autowired clientRepository: ClientRepository) {
		var client = Client("Jeno", "jeno@jeno.com")
		var client2 = Client("Jeno", "jeno2@jeno.com")

		val saveAll = clientRepository.saveAll(listOf(client, client2))

		assertEquals(clientRepository.findByName("Jeno"), saveAll)
	}

	@Test
	fun testAddingImage(@Autowired clientRepository: ClientRepository) {
		val path = "src/test/resources/image_example.png"
		val readAllBytes = Files.readAllBytes(Paths.get(path))
		var client = Client("Jeno", "jeno@jeno.com", Binary(readAllBytes))

		val savedClient = clientRepository.save(client)

		val fetchedClient = clientRepository.findByName("Jeno")
		assertThat(fetchedClient, hasSize(1))
		val client1 = fetchedClient[0]
		assertEquals(client1, savedClient)
		assertNotNull(client1.picture)
		assertTrue(client1.picture!!.data contentEquals readAllBytes)
	}

}