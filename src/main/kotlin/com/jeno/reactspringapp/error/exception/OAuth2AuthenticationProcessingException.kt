package com.jeno.reactspringapp.error.exception

import org.springframework.security.core.AuthenticationException

class OAuth2AuthenticationProcessingException(msg: String?) : AuthenticationException(msg) {
}