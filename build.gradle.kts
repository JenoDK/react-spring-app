import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.github.node-gradle.node") version "3.1.0"
	id("org.jetbrains.kotlin.plugin.allopen") version "1.5.20"
	kotlin("jvm") version "1.5.20"
	kotlin("plugin.spring") version "1.5.20"
}

group = "com.jeno"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

allOpen {
	annotation("org.springframework.data.mongodb.core.mapping.Document")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
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

node {
	nodeProjectDir.set(file("${project.projectDir}/src/main/webapp"))
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmBuild") {
	dependsOn(tasks.npmInstall)
	npmCommand.set(listOf("run", "build"))
}

tasks.register<Copy>("copyWebApp") {
	dependsOn("npmBuild")
	description = "Copies built npm project files to where it will be served"
	from("${project.projectDir}/src/main/webapp/build")
	into("${project.projectDir}/build/resources/main/static/.")
}

tasks.withType<KotlinCompile> {
	// So that all the tasks run with ./gradlew build
//	dependsOn("copyWebApp")
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

