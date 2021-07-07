package com.jeno.reactspringapp.security.oauth2

import com.jeno.reactspringapp.security.RestAuthenticationEntryPoint
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {

	companion object {
		val LOG = LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler::class.java)!!
	}

	override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {
		LOG.error("Oauth2 authentication failed", exception)
		super.onAuthenticationFailure(request, response, exception)
	}
}