package de.nsctool.api.authentication

import de.nsctool.api.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class WebSecurityConfig(
    private val userRepository: UserRepository,
    private val jwtTokenFilter: JwtTokenFilter,
    private val authErrorHandler: CustomAuthenticationFailureHandler,
): WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http ?: throw IllegalStateException("http security is null")

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            .exceptionHandling()
            .authenticationEntryPoint { req, res, ex ->
                authErrorHandler.onAuthenticationFailure(req, res, ex)
            }
            .and()

            .authorizeHttpRequests()
            .antMatchers("/health", "/doc/**", "/test", "/error", "/login").permitAll()
            .anyRequest().authenticated()
            .and()

            .csrf().disable()

            .addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth ?: throw IllegalStateException("auth is null")

        val service = UserDetailsService { username ->
            username ?: throw UsernameNotFoundException("Username is null")
            userRepository.findByUserName(username) ?: throw UsernameNotFoundException("User '$username' not found")
        }

        auth.userDetailsService(service)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationFailureHandler(): AuthenticationFailureHandler = authErrorHandler

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()
}