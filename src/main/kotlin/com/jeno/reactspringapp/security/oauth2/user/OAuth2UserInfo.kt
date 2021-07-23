package com.jeno.reactspringapp.security.oauth2.user

import com.jeno.reactspringapp.data.model.AuthProviderType
import com.jeno.reactspringapp.error.exception.OAuth2AuthenticationProcessingException

abstract class OAuth2UserInfo(val attributes: Map<String, Any>) {

	companion object {
		fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
			return if (registrationId.equals(AuthProviderType.GOOGLE.toString(), ignoreCase = true)) {
				GoogleOAuth2UserInfo(attributes)
			} else if (registrationId.equals(AuthProviderType.FACEBOOK.toString(), ignoreCase = true)) {
				FacebookOAuth2UserInfo(attributes)
			} else {
				throw OAuth2AuthenticationProcessingException("Sorry! Login with $registrationId is not supported yet.")
			}
		}
	}

	abstract fun getId(): String?

	abstract fun getName(): String?

	abstract fun getEmail(): String?

	abstract fun getImageUrl(): String?
}