package com.jeno.reactspringapp.security

import com.jeno.reactspringapp.data.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CustomUserDetailsService(val userRepository: UserRepository): UserDetailsService {

	@Transactional
	override fun loadUserByUsername(username: String?): UserDetails {
		val user = username
				?.let { userRepository.findByEmail(username) }
				?: throw UsernameNotFoundException("User not found with email $username")
		return UserPrincipal.create(user)
	}

	@Transactional
	fun loadUserById(id: String?): UserDetails {
		val optionalUser = id
				?.let { userRepository.findById(id) }
		return UserPrincipal.create(optionalUser!!.orElseThrow { UsernameNotFoundException("User not found with id $id") })
	}

}