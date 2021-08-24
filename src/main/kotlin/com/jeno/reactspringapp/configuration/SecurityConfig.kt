package com.jeno.reactspringapp.configuration

import com.jeno.reactspringapp.security.CustomUserDetailsService
import com.jeno.reactspringapp.security.RestAuthenticationEntryPoint
import com.jeno.reactspringapp.security.TokenAuthenticationFilter
import com.jeno.reactspringapp.security.oauth2.CookieOAuth2AuthorizationRequestRepository
import com.jeno.reactspringapp.security.oauth2.OAuth2AuthenticationFailureHandler
import com.jeno.reactspringapp.security.oauth2.OAuth2AuthenticationSuccessHandler
import com.jeno.reactspringapp.security.oauth2.OAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true
)
class SecurityConfig(
		private val oauth2SuccessHandler: OAuth2AuthenticationSuccessHandler,
		private val oAuth2FailureHandler: OAuth2AuthenticationFailureHandler,
		private val cookieOAuth2AuthorizationRequestRepository: CookieOAuth2AuthorizationRequestRepository,
		private val oAuth2UserService: OAuth2UserService,
		private val tokenAuthenticationFilter: TokenAuthenticationFilter,
		private val customUserDetailsService: CustomUserDetailsService) : WebSecurityConfigurerAdapter() {

	override fun configure(http: HttpSecurity?) {
		http!!
				.cors()
				.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.exceptionHandling()
					.authenticationEntryPoint(RestAuthenticationEntryPoint())
					.and()
				.authorizeRequests()
					.antMatchers("/",
							"/#",
							"/#/",
							"/#/**",
							"/public/**",
							"/api/public/**",
							"/static/**",
							"/error",
							"/favicon.ico",
							"/manifest.json",
							"/**/*.png",
							"/**/*.gif",
							"/**/*.svg",
							"/**/*.jpg",
							"/**/*.html",
							"/**/*.css",
							"/**/*.js")
						.permitAll()
					.antMatchers("/login/oauth2/**", "/oauth2/**")
						.permitAll()
					.anyRequest()
						.authenticated()
				.and()
				.oauth2Login()
					.successHandler(oauth2SuccessHandler)
					.failureHandler(oAuth2FailureHandler)
					.authorizationEndpoint()
						.authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository)
						.and()
					.userInfoEndpoint()
						.userService(oAuth2UserService)
//					.defaultSuccessUrl("/#/")
//					.failureUrl("/#/login")
//				.and()
//					.redirectionEndpoint()
//						.baseUri("/oauth2/callback/*")
//				.and()
//				.userInfoEndpoint()
//					.userService(customOAuth2UserService)
//				.and()
//				.successHandler(oAuth2AuthenticationSuccessHandler)
//				.failureHandler(oAuth2AuthenticationFailureHandler)
		http.addFilterAt(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
	}

	override fun configure(builder: AuthenticationManagerBuilder?) {
		builder!!
				.userDetailsService(customUserDetailsService)
				.passwordEncoder(passwordEncoder())
	}

	@Bean
	override fun authenticationManagerBean(): AuthenticationManager {
		return super.authenticationManagerBean()
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	// TODO see if we need CORS this open
//	@Bean
//	fun corsFilter(): CorsFilter? {
//		val source = UrlBasedCorsConfigurationSource()
//		val config = CorsConfiguration()
//		config.allowCredentials = true
//		config.setAllowedOriginPatterns(listOf("*"))
//		config.addAllowedHeader("*")
//		config.addAllowedMethod("*")
//		source.registerCorsConfiguration("/**", config)
//		return CorsFilter(source)
//	}

}