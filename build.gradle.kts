import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.spring") version "1.5.10"

    id("com.google.cloud.tools.jib") version "3.1.1"
}

group = "cn.sabercon"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.5.9")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.5.9")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.3")
    implementation("com.auth0:java-jwt:3.16.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val mainClassKt = "cn.sabercon.megumin.MeguminApplicationKt"

jib {
    container {
        mainClass = mainClassKt
    }
}

springBoot {
    mainClass.set(mainClassKt)
}
