plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.jetbrains:annotations:24.1.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    // Source: https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    implementation("io.rest-assured:rest-assured:5.5.6")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.22.1")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.1")
    // Source: https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:4.47.0")

}

tasks.test {
    useJUnitPlatform()
    group = "verification"
}

tasks.register( "runAllTests") {
    group = "verification"
    description = "Запускает все тесты проекта"
    dependsOn(tasks.test)
    finalizedBy("printTestRunOver")
}
tasks.register<Test>("runTheme31Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme3.1"

    useJUnitPlatform {
        includeTags("Theme3.1")
    }

    finalizedBy("printTestRunOver")
}
tasks.register<Test>("runTheme61Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme6.1"

    useJUnitPlatform {
        includeTags("Theme6.1")
    }

    finalizedBy("printTestRunOver")
}

tasks.register<Test>("runTheme32Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme3.2"

    useJUnitPlatform {
        includeTags("Theme3.2")
    }

    finalizedBy("printTestRunOver")
}

tasks.register<Test>("runTheme42Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme4.2"

    useJUnitPlatform {
        includeTags("Theme4.2")
    }

    finalizedBy("printTestRunOver")
}

tasks.register<Test>("runTheme41Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme4.1"

    useJUnitPlatform {
        includeTags("Theme4.1")
    }

    finalizedBy("printTestRunOver")
}

tasks.register<Test>("runTheme21Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme2.1"

    useJUnitPlatform {
        includeTags("Theme2.1")
    }

    finalizedBy("printTestRunOver")
}
tasks.register<Test>("runTheme22Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme2.2"

    useJUnitPlatform {
        includeTags("Theme2.2")
    }

    finalizedBy("printTestRunOver")
}
tasks.register("printTestRunOver") {
        group = "verification"
        doLast {
            println()
            println("======================================")
            println("         TEST RUN IS OVER")
            println("======================================")
        }
}