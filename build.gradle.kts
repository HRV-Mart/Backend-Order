import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.0"
	id("io.spring.dependency-management") version "1.1.0"
	id("io.gitlab.arturbosch.detekt") version "1.23.0"
	id("jacoco")// This is to use Jacoco for coverage testing
	kotlin("jvm") version "1.8.21"
	kotlin("plugin.spring") version "1.8.21"
}

group = "com.hrv.mart"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven {
		name = "GitHubPackages"
		url = uri("https://maven.pkg.github.com/hrv-mart/api-call")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
			password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	// Detekt Formatting
	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.0")
	// Cart Response
	implementation("com.hrv.mart:cart-response:0.0.1")
	// Order Response
	implementation("com.hrv.mart:order-library:0.0.3")
	// Kafka
	implementation("io.projectreactor.kafka:reactor-kafka")
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	// Actuator
	implementation("org.springframework.boot:spring-boot-starter-actuator")
}
detekt {
	toolVersion = "1.22.0"
	config = files("config/detekt/detekt.yml")
}
tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	// To run Jacoco Test Coverage Verification
	// Uncomment this later
	// finalizedBy("jacocoTestCoverageVerification")
}
tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.9".toBigDecimal()
			}
		}
	}
}
tasks.jacocoTestReport{
	reports {
		html.required.set(true)
		generate()
	}
}