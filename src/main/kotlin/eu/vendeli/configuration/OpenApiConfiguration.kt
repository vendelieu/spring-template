package eu.vendeli.configuration

import io.swagger.v3.oas.models.Components

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.Type
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {
    private val securitySchemeName = "bearerAuth"

    @Bean
    fun customOpenAPI(
    ): OpenAPI = OpenAPI().components(
        Components().addSecuritySchemes(
            securitySchemeName, SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT")
        )
    ).security(listOf(SecurityRequirement().addList(securitySchemeName)))
}