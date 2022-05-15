package de.nsctool.api.authentication

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.web.authentication.AuthenticationFailureHandler

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class WebSecurityConfig(
    private val authErrorHandler: CustomAuthenticationFailureHandler,
    private val converter: KeycloakJwtRoleConverter
) : WebSecurityConfigurerAdapter() {
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private lateinit var issuerUrl: String

    override fun configure(http: HttpSecurity?) {
        http ?: throw IllegalStateException("http security is null")

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests { auth ->
                auth
                    .antMatchers("/health", "/doc/**", "/test", "/error", "/login")
                        .permitAll()
                    .anyRequest()
                        .authenticated().and()
                        .csrf().disable()
                        .oauth2ResourceServer {
                            it.jwt { jwt ->
                                jwt.decoder(JwtDecoders.fromIssuerLocation(issuerUrl))
                                jwt.jwtAuthenticationConverter(converter)
                            }
                        }
            }
    }

    @Bean
    fun authenticationFailureHandler(): AuthenticationFailureHandler = authErrorHandler
}