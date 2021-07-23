package com.jeno.reactspringapp.data.controller

import com.jeno.reactspringapp.data.model.User
import com.jeno.reactspringapp.data.repository.UserRepository
import com.jeno.reactspringapp.error.exception.ResourceNotFoundException
import com.jeno.reactspringapp.security.UserPrincipal
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/users")
class UserController(private val userRepository: UserRepository) {

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	fun getUsers(): List<User> {
		return userRepository.findAll()
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	fun getCurrentUser(userPrincipal: UserPrincipal): User {
		return userRepository.findById(userPrincipal.id)
				.orElseThrow { ResourceNotFoundException("User", "id", userPrincipal.id) }
	}

}