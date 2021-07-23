package com.jeno.reactspringapp.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jeno.reactspringapp.data.model.AuthProviderType
import com.jeno.reactspringapp.data.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
		val id: String,
		val email: String,
		@JsonIgnore
		val pw: String?,
		val provider: AuthProviderType,
		val auths: MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("ROLE_USER")),
		val attr: MutableMap<String, Any> = mutableMapOf()): OAuth2User, UserDetails {
	companion object {
		fun create(user: User): UserPrincipal {
			return UserPrincipal(
					user.id ?: throw RuntimeException("user id is null"),
					user.email,
					user.password,
					user.provider)
		}

		fun create(user: User, attributes: MutableMap<String, Any>): UserPrincipal {
			return UserPrincipal(
					user.id ?: throw RuntimeException("user id is null"),
					user.email,
					user.password,
					user.provider,
					attr = attributes)
		}
	}

	override fun getName(): String {
		return id
	}

	override fun getAttributes(): MutableMap<String, Any> {
		return attr
	}

	override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
		return auths
	}

	@JsonIgnore
	override fun getPassword(): String? {
		return pw
	}

	override fun getUsername(): String {
		return email
	}

	override fun isAccountNonExpired(): Boolean {
		return true
	}

	override fun isAccountNonLocked(): Boolean {
		return true
	}

	override fun isCredentialsNonExpired(): Boolean {
		return true
	}

	override fun isEnabled(): Boolean {
		return true
	}

}