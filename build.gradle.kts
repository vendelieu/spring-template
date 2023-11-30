import eu.vendeli.jooq.generator.ExtendedJavaJooqGenerator
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaVersion = JavaVersion.VERSION_17
val dbUrl = project.findProperty("database-url")!!.toString()
val dbUser = project.findProperty("database-user")!!.toString()
val dbPwd = project.findProperty("database-pwd")!!.toString()
val dbTargetSchema = project.findProperty("database-target-schema")!!.toString()
val dbTargetSchemaList = dbTargetSchema.split(',')
val jooqOutputDir = project.layout.projectDirectory.dir("src/generated-jooq")

plugins {
    alias(libs.plugins.jooq)
    alias(libs.plugins.jooq.extension)
    alias(libs.plugins.fly.plugin)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

group = "eu.vendeli"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.jooq)

    implementation(libs.spring.boot.security.oauth.jose)
    implementation(libs.spring.boot.security.oauth.client)
    implementation(libs.spring.boot.security.oauth.resource.server)

    implementation(libs.swagger)
    implementation(libs.kotlin.reflections)
    implementation(libs.arrow.core)
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.arrow)

    implementation(libs.mu.logging)
    runtimeOnly(libs.logback.classic)

    implementation(libs.fly.core)
    runtimeOnly(libs.postgres)
    jooqCodegen(libs.postgres)

    testImplementation(libs.spring.boot.starter.test) {
        exclude("junit", "junit")
        exclude("org.skyscreamer", "jsonassert")
        exclude("org.mockito", "mockito-core")
        exclude("org.mockito", "mockito-junit-jupiter")
        exclude("org.hamcrest", "hamcrest")
        exclude("org.assertj", "assertj-core")
    }
    testImplementation(libs.kotest.framework.api)
    testImplementation(libs.kotest.property)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.spring)
    testRuntimeOnly(libs.kotest.junit5)
}

flyway {
    url = dbUrl
    user = dbUser
    password = dbPwd
    schemas = dbTargetSchemaList.toTypedArray()
//    cleanDisabled = false
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = javaVersion.majorVersion
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }

    clean {
        delete(jooqOutputDir)
    }

    jar {
        archiveFileName.set("app.jar")
    }

    generateJooqClasses {
        schemas.set(dbTargetSchemaList)
        basePackageName.set("${project.group}.jooq.generated")
        migrationLocations.setFromFilesystem("src/main/resources/db/migration")
        outputDirectory.set(jooqOutputDir)
        flywayProperties.put("flyway.placeholderReplacement", "false")
        includeFlywayTable.set(false)
        outputSchemaToDefault.add("public")

        usingJavaConfig {
            generate.apply {
                name = ExtendedJavaJooqGenerator::class.java.name
                isDeprecated = false
                isRecords = true
                isPojos = true
                isImmutablePojos = false
                isFluentSetters = true
                isInterfaces = true
                isDaos = true
                isSpringAnnotations = true
            }
        }
    }
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion.majorVersion))
    }
}