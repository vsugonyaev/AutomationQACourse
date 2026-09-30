plugins {
    `java-library`
}

description = "UI-автотесты на PageObject-модели с Allure-шагами"

dependencies {
    api(project(":common"))
    api("com.codeborne:selenide:7.18.0")
    api("io.qameta.allure:allure-java-commons")
    implementation("org.seleniumhq.selenium:selenium-java:4.47.0")
    implementation("io.qameta.allure:allure-selenide")
    implementation("org.assertj:assertj-core:3.24.2")

    // API client is used only to prepare and clean up test data
    testImplementation(project(":api-tests"))
}

tasks.register<Test>("smokeTest") {
    group = "verification"
    description = "Smoke-прогон UI-автотестов (тег smoke)"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform {
        includeTags("smoke")
    }
}
