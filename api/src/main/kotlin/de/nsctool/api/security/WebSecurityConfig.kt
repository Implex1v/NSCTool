package de.nsctool.api.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http ?: throw IllegalStateException("http security is null")

        http.authorizeHttpRequests()
            .antMatchers("/health").permitAll()
            .antMatchers("/doc/**").permitAll()
            .anyRequest().authenticated()
    }
}