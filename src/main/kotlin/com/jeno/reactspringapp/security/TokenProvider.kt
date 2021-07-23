package com.jeno.reactspringapp.security

import com.jeno.reactspringapp.common.ApplicationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.Date

@Component
class TokenProvider(val applicationProperties: ApplicationProperties) {
	companion object {
		val LOG = LoggerFactory.getLogger(TokenProvider::class.java)!!
	}

	fun createToken(authentication: Authentication): String {
		val userPrincipal: UserPrincipal = authentication.principal as UserPrincipal
		val now = Date()
		val expiryDate = Date(now.time + applicationProperties.auth.tokenExpirationMilliSec)
		return Jwts.builder()
				.setSubject(userPrincipal.id)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, applicationProperties.auth.tokenSecret)
				.compact()
	}

	fun getUserIdFromToken(token: String?): String? {
		val claims: Claims = Jwts.parser()
				.setSigningKey(applicationProperties.auth.tokenSecret)
				.parseClaimsJws(token)
				.body
		return claims.subject
	}

	fun validateToken(authToken: String?): Boolean {
		if (authToken == null) {
			return false
		}
		try {
			Jwts.parser().setSigningKey(applicationProperties.auth.tokenSecret).parseClaimsJws(authToken)
			return true
		} catch (ex: SignatureException) {
			LOG.error("Invalid JWT signature")
		} catch (ex: MalformedJwtException) {
			LOG.error("Invalid JWT token")
		} catch (ex: ExpiredJwtException) {
			LOG.error("Expired JWT token")
		} catch (ex: UnsupportedJwtException) {
			LOG.error("Unsupported JWT token")
		} catch (ex: IllegalArgumentException) {
			LOG.error("JWT claims string is empty.")
		}
		return false
	}
}