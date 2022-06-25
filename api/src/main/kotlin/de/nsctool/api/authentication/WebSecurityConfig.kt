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

    @Value("\${web.routes.auth.whitelist}")
    private lateinit var whitelist: String

    override fun configure(http: HttpSecurity?) {
        http ?: throw IllegalStateException("http security is null")

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors().and()
            .authorizeHttpRequests { auth ->

                auth
                    .antMatchers("/health", "/doc/**", "/test", "/error", "/login", "/characters/**", "/users/**", *authWhitelist())
                        .permitAll().and()
                        .csrf().disable()

                auth.anyRequest()
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

    private fun authWhitelist(): Array<String> {
        return whitelist
            .split(",")
            .filter { it.isNotBlank() }
            .map { "/$it/**" }.toTypedArray()
    }
}