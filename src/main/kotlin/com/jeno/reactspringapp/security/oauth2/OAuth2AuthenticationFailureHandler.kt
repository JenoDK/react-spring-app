package com.jeno.reactspringapp.security.oauth2

import com.jeno.reactspringapp.util.CookieUtils
import com.jeno.reactspringapp.util.removeAuthorizationCookies
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.WebUtils
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {

	companion object {
		val LOG = LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler::class.java)!!
	}

	override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {
		LOG.error("OAuth2 authentication failed", exception)
		var targetUrl: String? = request
				?.let { WebUtils.getCookie(request, CookieUtils.REDIRECT_URI_PARAM_COOKIE_NAME) }
				?.let { cookie -> cookie.value }

		targetUrl = UriComponentsBuilder.fromUriString(targetUrl ?: "/")
				.queryParam("error", exception!!.localizedMessage)
				.build().toUriString()

		removeAuthorizationCookies(request, response)

		redirectStrategy.sendRedirect(request, response, targetUrl)
	}
}