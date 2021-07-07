package com.jeno.reactspringapp.configuration

import com.jeno.reactspringapp.security.oauth2.OAuth2AuthenticationFailureHandler
import com.jeno.reactspringapp.security.oauth2.OAuth2AuthenticationSuccessHandler
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
class SecurityConfig(
		private val oauth2SuccessHandler: OAuth2AuthenticationSuccessHandler,
		private val oAuth2FailureHandler: OAuth2AuthenticationFailureHandler) : WebSecurityConfigurerAdapter() {

	override fun configure(http: HttpSecurity?) {
//		http!!
//				.cors()
//				.and()
//				.sessionManagement()
//					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				.and()
//				.csrf().disable()
//				.formLogin().disable()
//				.httpBasic().disable()
////				.exceptionHandling()
////					.authenticationEntryPoint(RestAuthenticationEntryPoint())
////				.and()
//				.authorizeRequests()
//					.antMatchers("/",
//							"/#",
//							"/#/",
//							"/#/**",
//							"/error",
//							"/favicon.ico",
//							"/**/*.png",
//							"/**/*.gif",
//							"/**/*.svg",
//							"/**/*.jpg",
//							"/**/*.html",
//							"/**/*.css",
//							"/**/*.js")
//						.permitAll()
//					.antMatchers("/login/oauth2/**", "/oauth2/**")
//						.permitAll()
//					.anyRequest()
//						.authenticated()
//				.and()
//				.oauth2Login()
////					.successHandler(oauth2SuccessHandler)
////					.failureHandler(oAuth2FailureHandler)
//					.defaultSuccessUrl("/#/")
//					.failureUrl("/#/login")
////				.and()
////					.redirectionEndpoint()
////						.baseUri("/oauth2/callback/*")
////				.and()
////				.userInfoEndpoint()
////					.userService(customOAuth2UserService)
////				.and()
////				.successHandler(oAuth2AuthenticationSuccessHandler)
////				.failureHandler(oAuth2AuthenticationFailureHandler)
	}
}