package eu.vendeli.configuration

import com.zaxxer.hikari.HikariDataSource
import org.jooq.ConnectionProvider
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderKeywordCase
import org.jooq.conf.RenderNameCase
import org.jooq.conf.Settings
import org.jooq.conf.SettingsTools
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableAutoConfiguration(
    exclude = [
        TransactionAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        DataSourceAutoConfiguration::class,
        R2dbcAutoConfiguration::class,
        JooqAutoConfiguration::class
    ]
)
@EnableTransactionManagement
@PropertySource("classpath:application.yaml")
class DatabaseConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dsProperties() = DataSourceProperties()

    @Bean("defaultDs")
    @ConfigurationProperties("spring.datasource.hikari")
    fun dataSource(@Qualifier("dsProperties") properties: DataSourceProperties): HikariDataSource = properties.run {
        initializeDataSourceBuilder()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean
    fun platformTransactionManager(@Qualifier("defaultDs") dataSource: DataSource): PlatformTransactionManager =
        DataSourceTransactionManager(dataSource).apply {
            // defaultTimeout = -1
        }

    @Bean
    fun dataSourceConnectionProvider(@Qualifier("defaultDs") dataSource: DataSource): ConnectionProvider =
        DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))

    @Bean
    fun settings(): Settings = SettingsTools.defaultSettings().apply {
        isRenderFormatted = true
        //statementType = StatementType.STATIC_STATEMENT
        renderKeywordCase = RenderKeywordCase.UPPER
        renderNameCase = RenderNameCase.AS_IS
        isReturnAllOnUpdatableRecord = true
    }

    @Bean
    fun jooqExceptionTranslatorExecuteListenerProvider(): DefaultExecuteListenerProvider =
        DefaultExecuteListenerProvider(JooqExceptionTranslator())

    @Bean
    fun jooq(cp: ConnectionProvider, settings: Settings): org.jooq.Configuration = createJooq(cp, settings)

    fun createJooq(cp: ConnectionProvider, settings: Settings): org.jooq.Configuration = DefaultConfiguration().apply {
        setSQLDialect(SQLDialect.POSTGRES)
        setConnectionProvider(cp)
        setSettings(settings)
    }

    @Bean
    fun dslContext(configuration: org.jooq.Configuration): DSLContext = DSL.using(configuration)
}
