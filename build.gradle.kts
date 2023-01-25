import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.1-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("plugin.jpa") version "1.7.21"
	kotlin("plugin.allopen") version "1.7.21"
	kotlin("plugin.spring") version "1.7.21"
	kotlin("jvm") version "1.7.21"
}

allOpen {
	annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embedabble")
}

group = "rubber.dutch.hat"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.flywaydb:flyway-core")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter:1.17.6")
	testImplementation("org.testcontainers:postgresql:1.17.6")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.getByName<Jar>("jar") {
	enabled = false
}

tasks.withType<Test> {
	useJUnitPlatform()
}
