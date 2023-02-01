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

    @Value("\${security.enabled}")
    private lateinit var securityEnabled: String

    @Bean
    fun webSecurity(http: HttpSecurity): SecurityFilterChain = http
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors()
        .and()
        .let {
            when(securityEnabled.toBooleanStrictOrNull()) {
                false -> it
                else -> it
                    .authorizeHttpRequests()
                    .requestMatchers("/actuator/**", "/test", "/error", "/login", "/swagger-ui.html", "/v3/api-docs/")
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
            }
        }
        .build()

    @Bean
    fun authenticationFailureHandler(): AuthenticationFailureHandler = authErrorHandler
}