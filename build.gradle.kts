plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    // Keeps real parameter names available at runtime so Allure @Step message
    // placeholders like {productName} resolve instead of staying literal.
    options.compilerArgs.add("-parameters")
}

val allureVersion = "2.29.0"
val aspectjVersion = "1.9.22"

val allureCli: Configuration by configurations.creating

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
    // Source: https://mvnrepository.com/artifact/com.codeborne/selenide
    implementation("com.codeborne:selenide:7.18.0")

    // Allure reporting
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-junit5")
    testImplementation("io.qameta.allure:allure-selenide")
    testImplementation("io.qameta.allure:allure-rest-assured")
    // AspectJ weaver is required at runtime for @Step/@Attachment annotations to be picked up
    testImplementation("org.aspectj:aspectjweaver:$aspectjVersion")

    allureCli("io.qameta.allure:allure-commandline:$allureVersion@zip")
}

tasks.withType<Test> {
    useJUnitPlatform()
    group = "verification"

    doFirst {
        val weaverJar = configurations.testRuntimeClasspath.get()
            .files
            .first { it.name.startsWith("aspectjweaver") }
        jvmArgs("-javaagent:${weaverJar.absolutePath}")
    }

    systemProperty("allure.results.directory", layout.buildDirectory.dir("allure-results").get().asFile.absolutePath)
}

val unzipAllureCli by tasks.registering(Copy::class) {
    from(zipTree(allureCli.singleFile))
    into(layout.buildDirectory.dir("allure-cli"))
}

tasks.register<Exec>("allureReport") {
    dependsOn(unzipAllureCli)
    group = "verification"
    description = "Генерирует HTML Allure-отчёт из build/allure-results в build/allure-report"
    workingDir = projectDir
    commandLine(
        "cmd", "/c",
        layout.buildDirectory.dir("allure-cli/allure-$allureVersion/bin/allure.bat").get().asFile.absolutePath,
        "generate", "build/allure-results", "-o", "build/allure-report", "--clean"
    )
}

tasks.register<Exec>("allureServe") {
    dependsOn(unzipAllureCli)
    group = "verification"
    description = "Поднимает локальный сервер и открывает Allure-отчёт в браузере (Ctrl+C, чтобы остановить)"
    workingDir = projectDir
    commandLine(
        "cmd", "/c",
        layout.buildDirectory.dir("allure-cli/allure-$allureVersion/bin/allure.bat").get().asFile.absolutePath,
        "serve", "build/allure-results"
    )
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
tasks.register<Test>("runTheme62Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme6.2"

    useJUnitPlatform {
        includeTags("Theme6.2")
    }

    finalizedBy("printTestRunOver")
}
tasks.register<Test>("runTheme63Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme6.3"

    useJUnitPlatform {
        includeTags("Theme6.3")
    }

    finalizedBy("printTestRunOver")
}
tasks.register<Test>("runTheme7Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme7"

    useJUnitPlatform {
        includeTags("Theme7")
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
tasks.register<Test>("runTheme9Tests") {
    group = "verification"
    description = "Запускает тесты с тегом Theme9"

    useJUnitPlatform {
        includeTags("Theme9")
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