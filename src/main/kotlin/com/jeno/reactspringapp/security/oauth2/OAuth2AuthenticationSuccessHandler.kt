package com.jeno.reactspringapp.security.oauth2

import com.jeno.reactspringapp.common.ApplicationProperties
import com.jeno.reactspringapp.security.TokenProvider
import com.jeno.reactspringapp.util.CookieUtils.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import com.jeno.reactspringapp.util.removeAuthorizationCookies
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.WebUtils
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler(
		val oauth2AuthorizedClientService: OAuth2AuthorizedClientService,
		val applicationProperties: ApplicationProperties,
		val tokenProvider: TokenProvider) : SimpleUrlAuthenticationSuccessHandler() {

	companion object {
		val LOG = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler::class.java)!!
		const val ACCESS_TOKEN_COOKIE_NAME: String = "accessToken"
	}

	init {
		targetUrlParameter = "redirect_uri"
	}

	override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
		LOG.info("OAuth2 authentication succeeded for $authentication")
		val targetUrl = determineTargetUrl(request, response, authentication)

		if (response!!.isCommitted) {
			logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
			return
		}

		clearAuthenticationAttributes(request, response)
		redirectStrategy.sendRedirect(request, response, targetUrl)
	}

	override fun determineTargetUrl(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?): String? {
		val redirectUri: String? = request
				?.let { WebUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME) }
				?.let { cookie -> cookie.value }
		if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
			throw AccessDeniedException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
		}
		val targetUrl = redirectUri ?: defaultTargetUrl

		return if (authentication != null) {
			UriComponentsBuilder.fromUriString(targetUrl)
					.queryParam("token", tokenProvider.createToken(authentication))
					.build().toUriString()
		} else {
			"/401"
		}
	}

	protected fun clearAuthenticationAttributes(request: HttpServletRequest?, response: HttpServletResponse?) {
		if (request != null) {
			super.clearAuthenticationAttributes(request)
		}
		removeAuthorizationCookies(request, response)
	}

	private fun isAuthorizedRedirectUri(uri: String): Boolean {
		val clientRedirectUri = URI.create(uri)
		return applicationProperties.oauth2.authorizedRedirectUrls
				.any { authorizedRedirectUri ->
					// Only validate host and port. Let the clients use different paths if they want to
					val authorizedURI = URI.create(authorizedRedirectUri)
					if (authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true)
							&& authorizedURI.port == clientRedirectUri.port) {
						return true
					}
					return false
				}
	}

}