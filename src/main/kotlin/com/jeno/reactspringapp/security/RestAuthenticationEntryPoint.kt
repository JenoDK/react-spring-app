package com.jeno.reactspringapp.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

	companion object {
		val LOG = LoggerFactory.getLogger(RestAuthenticationEntryPoint::class.java)!!
	}

	override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, e: AuthenticationException?) {
		LOG.error("Responding with unauthorized error.", e)
		response!!.sendError(HttpServletResponse.SC_UNAUTHORIZED, e!!.localizedMessage)
	}
}