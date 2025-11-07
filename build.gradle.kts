
plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.0"
    java
}

group = "br.com.example"
version = "0.2.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.ws:spring-ws-core:4.0.8")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.0")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")
    implementation("io.opentelemetry:opentelemetry-api:1.29.0")
    implementation("io.opentelemetry:opentelemetry-sdk:1.29.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.34.0")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("redis.clients:jedis:4.3.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.35.0")
    testImplementation("org.testcontainers:junit-jupiter:1.19.0")
    testImplementation("org.testcontainers:postgresql:1.19.0")
    testImplementation("org.springframework.ws:spring-ws-test:4.1.0")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.0.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
