package com.jeno.reactspringapp.security.oauth2

import com.jeno.reactspringapp.util.CookieUtils.Companion.COOKIE_EXPIRES_SECONDS
import com.jeno.reactspringapp.util.CookieUtils.Companion.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
import com.jeno.reactspringapp.util.CookieUtils.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import com.jeno.reactspringapp.util.addCookie
import com.jeno.reactspringapp.util.deserialize
import com.jeno.reactspringapp.util.removeAuthorizationCookies
import com.jeno.reactspringapp.util.serialize
import com.nimbusds.oauth2.sdk.util.StringUtils
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CookieOAuth2AuthorizationRequestRepository: AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	override fun loadAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
		return request
				?.let { WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME) }
				?.let { cookie -> deserialize(cookie, OAuth2AuthorizationRequest::class.java) }
	}

	override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest?, request: HttpServletRequest?, response: HttpServletResponse?) {
		if (authorizationRequest == null) {
			removeAuthorizationCookies(request, response)
			return
		}

		if (response != null) {
			addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest), COOKIE_EXPIRES_SECONDS)
			val redirectUriAfterLogin = request!!.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
			if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
				addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRES_SECONDS)
			}
		}
	}

	override fun removeAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
		return loadAuthorizationRequest(request)
	}

}