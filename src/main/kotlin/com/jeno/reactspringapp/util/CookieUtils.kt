package com.jeno.reactspringapp.util

import com.jeno.reactspringapp.util.CookieUtils.Companion.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
import com.jeno.reactspringapp.util.CookieUtils.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import org.springframework.util.SerializationUtils
import java.util.Base64
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

fun serialize(obj: Any): String {
	return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj))
}

fun <T> deserialize(cookie: Cookie, clazz: Class<T>): T {
	return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.value)))
}

fun addCookie(response: HttpServletResponse, name: String?, value: String?, maxAge: Int) {
	val cookie = Cookie(name, value)
	cookie.path = "/"
	cookie.isHttpOnly = true
	cookie.maxAge = maxAge
	response.addCookie(cookie)
}

fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
	val cookies = request.cookies
	if (cookies != null && cookies.isNotEmpty()) {
		for (cookie in cookies) {
			if (cookie.name == name) {
				cookie.value = ""
				cookie.path = "/"
				cookie.maxAge = 0
				response.addCookie(cookie)
			}
		}
	}
}

fun removeAuthorizationCookies(request: HttpServletRequest?, response: HttpServletResponse?) {
	if (request != null && response != null) {
		deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
		deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
	}
}

class CookieUtils {
	companion object {
		const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME: String = "oauth2_auth_request"
		const val REDIRECT_URI_PARAM_COOKIE_NAME: String = "redirect_uri"
		const val COOKIE_EXPIRES_SECONDS = 180
	}
}