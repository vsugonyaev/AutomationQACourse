plugins {
    `java-library`
}

description = "API-автотесты: RestApiBuilder -> basicApi-классы -> ApiAssert"

dependencies {
    api(project(":common"))
    api("io.rest-assured:rest-assured:5.5.6")
    api("io.qameta.allure:allure-java-commons")
    implementation("io.qameta.allure:allure-rest-assured")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.1")
    implementation("org.assertj:assertj-core:3.24.2")
}

tasks.register<Test>("smokeTest") {
    group = "verification"
    description = "Smoke-прогон API-автотестов (тег smoke)"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform {
        includeTags("smoke")
    }
}
