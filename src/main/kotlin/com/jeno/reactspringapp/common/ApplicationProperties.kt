package com.jeno.reactspringapp.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "application")
data class ApplicationProperties(var oauth2: OAuth = OAuth(), var auth: Auth = Auth()) {
	data class Auth(var tokenSecret: String = "", var tokenExpirationMilliSec: Long = 172800000L)
	data class OAuth(var authorizedRedirectUrls: List<String> = listOf())
}