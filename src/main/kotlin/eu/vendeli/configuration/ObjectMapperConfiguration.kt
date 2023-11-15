package eu.vendeli.configuration

import arrow.integrations.jackson.module.registerArrowModule
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


@Configuration
class ObjectMapperConfiguration {
    @Bean
    fun objectMapper() = ObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

        registerModules(
            KotlinModule.Builder().apply {
                configure(KotlinFeature.SingletonSupport, true)
            }.build(),

            JavaTimeModule(),
            SimpleModule(
                "CustomModule", Version.unknownVersion(),
                mapOf(
                    LocalDateTime::class.java to UnixTimestampDeserializer
                ),
                listOf(UnixTimestampSerializer)
            )
        )

        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }.registerArrowModule()

    private object UnixTimestampSerializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
        private fun readResolve(): Any = UnixTimestampSerializer
        override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider?) =
            gen.writeNumber(value.toInstant(ZoneOffset.UTC).toEpochMilli())
    }

    private object UnixTimestampDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {
        private fun readResolve(): Any = UnixTimestampDeserializer
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime =
            p.valueAsLong.takeIf { it > 0 }?.let {
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
            } ?: throw MismatchedInputException.from(p, LocalDateTime::class.java, "Timestamp cannot be negative")
    }
}
