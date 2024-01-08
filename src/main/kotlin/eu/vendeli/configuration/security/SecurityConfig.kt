package eu.vendeli.configuration.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!test")
class SecurityConfig(
    private val jwtAuthConverter: JwtAuthConverter
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.csrf {
            it.disable()
        }.authorizeHttpRequests {
            it.requestMatchers("/api/**").authenticated()
            it.anyRequest().permitAll()
        }.oauth2ResourceServer { configurer ->
            configurer.jwt { it.jwtAuthenticationConverter(jwtAuthConverter) }
        }.oauth2Login {
            it.defaultSuccessUrl("/").permitAll()
        }.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.build()
}