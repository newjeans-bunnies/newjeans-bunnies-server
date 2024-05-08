
plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"

    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

val jjwtVersion = "0.11.5"


group = "newjeans.bunnies"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}
tasks.getByName<Jar>("jar"){
    enabled=false
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.2.2")

    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    implementation(platform("software.amazon.awssdk:bom:2.20.85"))
    implementation("software.amazon.awssdk:dynamodb-enhanced")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation ("com.github.vladimir-bukhtoyarov:bucket4j-core:7.0.0")

    implementation("aws.sdk.kotlin:s3:1.0.0")

    implementation("net.nurigo:sdk:4.3.0")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
    testImplementation("org.springframework.security:spring-security-test:6.0.2")
}