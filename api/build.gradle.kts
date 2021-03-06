import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.7"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "de.nsctool"
version = System.getenv().getOrDefault("APP_VERSION", "0.1.0")
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

val jwtVersion = "0.11.5"
val mockkVersion = "1.12.3"
val kotestVersion = "5.1.0"
val springMockVersion = "3.1.1"
val keycloakAdminClientVersion = "18.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-config")
//	implementation("org.springframework.security:spring-security-oauth2-jose")
//	implementation("org.springframework.security:spring-security-oauth2-client")
//	implementation("org.springframework.security:spring-security-oauth2-resource-server")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	//implementation("org.flywaydb:flyway-core")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
	implementation("org.keycloak:keycloak-admin-client:$keycloakAdminClientVersion")

	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jwtVersion")

	testImplementation("io.mockk:mockk:$mockkVersion")
	testImplementation("com.ninja-squad:springmockk:$springMockVersion")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.jar {
	archiveFileName.set("nsctool-${project.name}.jar")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		setEvents(listOf("failed"))
		setExceptionFormat("full")
		showCauses = true
		showStackTraces = true
	}
}
