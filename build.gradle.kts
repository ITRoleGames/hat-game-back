import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("plugin.jpa") version "1.7.21"
	kotlin("plugin.allopen") version "1.7.21"
	kotlin("plugin.spring") version "1.7.21"
	kotlin("jvm") version "1.7.21"
	id("io.gitlab.arturbosch.detekt") version "1.22.0"
	id("jacoco")
	id("com.palantir.git-version") version "1.0.0"
	kotlin("kapt") version "1.8.20"
}

allOpen {
  annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embedabble")
}

group = "rubber.dutch.hat"
val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion()
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
  maven { url = uri("https://repo.spring.io/milestone") }
  maven { url = uri("https://repo.spring.io/snapshot") }
  maven {
    url = uri("https://maven.pkg.github.com/itrolegames/hat-game-event-api")
    credentials {
      username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
      password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_KEY")
    }
  }
}

dependencies {
	implementation("rubber.dutch.hat:hat-game-event-api:0.0.1")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation ("org.hibernate.orm:hibernate-jpamodelgen:6.2.0.Final")
	kapt("org.hibernate.orm:hibernate-jpamodelgen:6.2.0.Final")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.flywaydb:flyway-core")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
	testImplementation("org.testcontainers:junit-jupiter:1.17.6")
	testImplementation("org.awaitility:awaitility:4.2.0")
	testImplementation("org.testcontainers:postgresql:1.17.6")
	testImplementation("org.testcontainers:rabbitmq:1.17.6")
	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
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

tasks.processResources {
	expand("version" to project.version)
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		csv.required.set(false)
		html.required.set(false)
	}
}

jacoco {
	toolVersion = "0.8.7"
}

detekt {
  source = objects.fileCollection().from(
    io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_JAVA,
    io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_JAVA,
    io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
    io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
  )
  buildUponDefaultConfig = true
  baseline = file("$rootDir/config/detekt/baseline.xml")
  config = files("$rootDir/config/detekt/detekt.yml", "$rootDir/config/detekt/detekt-custom.yml")
}
