package de.nsctool.api.authentication

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class WebSecurityConfig(
    private val authErrorHandler: CustomAuthenticationFailureHandler,
    private val converter: KeycloakJwtRoleConverter
) {
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private lateinit var issuerUrl: String

    @Value("\${web.routes.auth.whitelist}")
    private lateinit var whitelist: String

    fun webSecurity(http: HttpSecurity): SecurityFilterChain = http
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors()
        .and()
        .authorizeHttpRequests()
            .requestMatchers("/actuator/**", "/doc/**", "/test", "/error", "/login", "/characters/**", "/users/**", *authWhitelist())
            .permitAll()
            .and()
            .csrf()
            .disable()
        .authorizeHttpRequests { auth ->
            auth.anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .oauth2ResourceServer {
                    it.jwt { jwt ->
                        jwt.decoder(JwtDecoders.fromIssuerLocation(issuerUrl))
                        jwt.jwtAuthenticationConverter(converter)
                    }
                }
        }
        .build()

    @Bean
    fun authenticationFailureHandler(): AuthenticationFailureHandler = authErrorHandler

    private fun authWhitelist(): Array<String> {
        return whitelist
            .split(",")
            .filter { it.isNotBlank() }
            .map { "/$it/**" }.toTypedArray()
    }
}