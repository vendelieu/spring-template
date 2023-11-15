package eu.vendeli.configuration.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component


@Component
class JwtAuthConverter(
    @Value("\${jwt.auth.converter.principle-attribute}")
    private val principleAttribute: String,

    private val jwtRolesConverter: JwtRolesConverter
) : Converter<Jwt?, AbstractAuthenticationToken?> {
    override fun convert(jwt: Jwt): AbstractAuthenticationToken =
        JwtAuthenticationToken(
            jwt,
            jwtRolesConverter.convert(jwt),
            jwt.getClaim(principleAttribute)
        )
}