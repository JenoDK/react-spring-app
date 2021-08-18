package com.jeno.reactspringapp.data.controller

import com.jeno.reactspringapp.data.model.AuthProviderType
import com.jeno.reactspringapp.data.model.User
import com.jeno.reactspringapp.data.repository.UserRepository
import com.jeno.reactspringapp.error.exception.ValidationException
import com.jeno.reactspringapp.security.TokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder


@RestController
@RequestMapping("api/public")
class AuthController(
		private val userRepository: UserRepository,
		private val authenticationManager: AuthenticationManager,
		private val tokenProvider: TokenProvider,
		private val passwordEncoder: PasswordEncoder) {

	@PostMapping("/login")
	fun authenticateUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
		val authentication: Authentication =
				authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password))
		SecurityContextHolder.getContext().authentication = authentication
		return ResponseEntity.ok()
				.body(AuthenticationResponse(authentication, tokenProvider.createToken(authentication)))
	}

	@PostMapping("/signup")
	fun registerUser(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<Any> {
		if (userRepository.existsByEmail(signUpRequest.email)) {
			throw ValidationException(mapOf("email" to "Email address already in use."))
		}

		val user = User(
				signUpRequest.username,
				signUpRequest.email,
				passwordEncoder.encode(signUpRequest.password),
				provider = AuthProviderType.LOCAL,
		)
		val savedUser = userRepository.save(user)
		val location = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("api/users/me/{id}")
				.buildAndExpand(mapOf("id" to savedUser.id))
				.toUri()
		return ResponseEntity
				.created(location)
				.body(savedUser)
	}

	data class LoginRequest(val email: String, val password: String)

	data class SignUpRequest(val username: String, val email: String, val password: String)

	data class AuthenticationResponse(val authentication: Authentication, val accessToken: String)
}