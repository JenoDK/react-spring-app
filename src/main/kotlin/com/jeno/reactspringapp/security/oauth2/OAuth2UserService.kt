package com.jeno.reactspringapp.security.oauth2

import com.jeno.reactspringapp.data.model.AuthProviderType
import com.jeno.reactspringapp.data.model.Picture
import com.jeno.reactspringapp.data.model.User
import com.jeno.reactspringapp.data.repository.PictureRepository
import com.jeno.reactspringapp.data.repository.UserRepository
import com.jeno.reactspringapp.error.exception.OAuth2AuthenticationProcessingException
import com.jeno.reactspringapp.security.UserPrincipal
import com.jeno.reactspringapp.security.oauth2.user.OAuth2UserInfo
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.Objects

@Component
class OAuth2UserService(
		val userRepository: UserRepository,
		val pictureRepository: PictureRepository): DefaultOAuth2UserService() {

	override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
		val oauth2User = super.loadUser(userRequest)
		return try {
			processOAuth2User(userRequest, oauth2User)
		} catch (ex: AuthenticationException) {
			throw ex
		} catch (ex: Exception) {
			// Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
			throw InternalAuthenticationServiceException(ex.message, ex.cause)
		}
	}

	private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest?, oAuth2User: OAuth2User): OAuth2User {
		if (oAuth2UserRequest == null) {
			throw OAuth2AuthenticationProcessingException("OAuth2 request is null")
		}
		val oAuth2UserInfo: OAuth2UserInfo = OAuth2UserInfo.getOAuth2UserInfo(oAuth2UserRequest.clientRegistration.registrationId, oAuth2User.attributes)
		if (!StringUtils.hasText(oAuth2UserInfo.getEmail()) || !StringUtils.hasText(oAuth2UserInfo.getId())) {
			throw OAuth2AuthenticationProcessingException("Insufficient info provided from OAuth2 provider ${oAuth2UserRequest.clientRegistration.registrationId}")
		}
		val user = userRepository.findByProviderId(oAuth2UserInfo.getId()!!)
				?.let { user -> updateExistingUser(user, oAuth2UserInfo) }
				?: registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
		return UserPrincipal.create(user, oAuth2User.attributes)
	}

	private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): User {
		val picture = Picture(oAuth2UserInfo.getId() + "_profile_picture", url = oAuth2UserInfo.getImageUrl())
		val user = User(
				oAuth2UserInfo.getName() ?: "",
				oAuth2UserInfo.getEmail()!!,
				provider = AuthProviderType.valueOf(oAuth2UserRequest.clientRegistration.registrationId.toUpperCase()),
				providerId = oAuth2UserInfo.getId(),
				profilePicture = pictureRepository.save(picture)
		)
		return userRepository.save(user)
	}

	private fun updateExistingUser(existingUser: User, oAuth2UserInfo: OAuth2UserInfo): User {
		val name = oAuth2UserInfo.getName() ?: ""
		if (!Objects.equals(existingUser.name, name)) {
			existingUser.name = name
		}
		if (existingUser.profilePicture != null && !Objects.equals(existingUser.profilePicture!!.url, oAuth2UserInfo.getImageUrl())) {
			existingUser.profilePicture!!.url = oAuth2UserInfo.getImageUrl()
			pictureRepository.save(existingUser.profilePicture!!)
		}
		return userRepository.save(existingUser)
	}

}