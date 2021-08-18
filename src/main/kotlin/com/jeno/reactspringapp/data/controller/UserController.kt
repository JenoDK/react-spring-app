package com.jeno.reactspringapp.data.controller

import com.jeno.reactspringapp.data.model.User
import com.jeno.reactspringapp.data.repository.UserRepository
import com.jeno.reactspringapp.error.exception.ResourceNotFoundException
import com.jeno.reactspringapp.security.UserPrincipal
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException
import java.security.Principal


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
	fun getCurrentUser(authentication: Authentication): User {
		val principal = authentication.principal
		if (principal is UserPrincipal) {
			return userRepository.findById(principal.id)
					.orElseThrow { ResourceNotFoundException("User", "id", principal.id) }
		} else {
			throw IllegalArgumentException("Invalid Authentication object found")
		}
	}

}