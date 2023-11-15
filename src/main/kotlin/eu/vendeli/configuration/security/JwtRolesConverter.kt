package eu.vendeli.configuration.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class JwtRolesConverter : Converter<Jwt, Collection<GrantedAuthority?>?> {
    /**
     * Extracts the realm-level roles from a JWT token distinguishing between them using prefixes.
     */
    override fun convert(jwt: Jwt): List<GrantedAuthority> {
        val grantedAuthorities: MutableList<GrantedAuthority> = mutableListOf()
        val realmAccess: Map<String, List<String>> = jwt.getClaim(CLAIM_REALM_ACCESS)

        if (realmAccess.isNotEmpty()) {
            val roles = realmAccess[CLAIM_ROLES]
            if (!roles.isNullOrEmpty()) {
                val realmRoles: List<GrantedAuthority> = roles.map { role ->
                    SimpleGrantedAuthority(PREFIX_ROLE + role)
                }
                grantedAuthorities.addAll(realmRoles)
            }
        }
        return grantedAuthorities
    }

    companion object {
        /**
         * Prefix used for realm level roles.
         */
        const val PREFIX_ROLE = "ROLE_"

        /**
         * Name of the claim containing the realm level roles
         */
        private const val CLAIM_REALM_ACCESS = "realm_access"

        /**
         * Name of the claim containing roles. (Applicable to realm and resource level.)
         */
        private const val CLAIM_ROLES = "roles"
    }
}
