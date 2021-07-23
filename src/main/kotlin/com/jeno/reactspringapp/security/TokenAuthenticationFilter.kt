package com.jeno.reactspringapp.security

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenAuthenticationFilter(
		val tokenProvider: TokenProvider,
		val customUserDetailsService: CustomUserDetailsService): OncePerRequestFilter() {

	companion object {
		val LOG = LoggerFactory.getLogger(TokenAuthenticationFilter::class.java)!!
	}

	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		try {
			val jwt = getJwtFromRequest(request)
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				val userId = tokenProvider.getUserIdFromToken(jwt)
				val userDetails = customUserDetailsService.loadUserById(userId)
				val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
				authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
				SecurityContextHolder.getContext().authentication = authentication
			}
		} catch (ex: Exception) {
			LOG.error("Could not set user authentication in security context", ex)
		}
		filterChain.doFilter(request, response)
	}

	private fun getJwtFromRequest(request: HttpServletRequest): String? {
		val bearerToken = request.getHeader("Authorization")
		return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			bearerToken.substring(7, bearerToken.length)
		} else {
			null
		}
	}

}