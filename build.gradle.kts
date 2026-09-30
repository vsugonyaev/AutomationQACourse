plugins {
    base
}

val allureVersion = "2.29.0"
val aspectjVersion = "1.9.22"
val junitVersion = "5.9.1"

val allureResultsDir = layout.buildDirectory.dir("allure-results")
val allureReportDir = layout.buildDirectory.dir("allure-report")
val allureCliDir = layout.buildDirectory.dir("allure-cli")

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    pluginManager.withPlugin("java") {
        val aspectjAgent = configurations.create("aspectjAgent")

        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            // Keeps real parameter names at runtime so @Step placeholders like {name} resolve.
            options.compilerArgs.add("-parameters")
        }

        dependencies {
            "implementation"(platform("io.qameta.allure:allure-bom:$allureVersion"))
            "testImplementation"(platform("org.junit:junit-bom:$junitVersion"))
            "testImplementation"("org.junit.jupiter:junit-jupiter")
            "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
            "testImplementation"("io.qameta.allure:allure-junit5")
            aspectjAgent("org.aspectj:aspectjweaver:$aspectjVersion")
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            // AspectJ weaver must be a javaagent, otherwise @Step annotations are ignored.
            jvmArgumentProviders.add(CommandLineArgumentProvider {
                listOf("-javaagent:${aspectjAgent.singleFile.absolutePath}")
            })
            systemProperty("allure.results.directory", allureResultsDir.get().asFile.absolutePath)
            testLogging {
                events("passed", "skipped", "failed")
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
            }
        }
    }
}

// ---------- Smoke ----------

tasks.register("apiSmoke") {
    group = "verification"
    description = "Smoke-прогон API-автотестов"
    dependsOn(":api-tests:smokeTest")
}

tasks.register("uiSmoke") {
    group = "verification"
    description = "Smoke-прогон UI-автотестов"
    dependsOn(":ui-tests:smokeTest")
}

tasks.register("smoke") {
    group = "verification"
    description = "Smoke-прогон API- и UI-автотестов"
    dependsOn("apiSmoke", "uiSmoke")
}

// ---------- Allure ----------

val allureCli: Configuration by configurations.creating

dependencies {
    allureCli("io.qameta.allure:allure-commandline:$allureVersion@zip")
}

val unzipAllureCli by tasks.registering(Sync::class) {
    from({ zipTree(allureCli.singleFile) })
    into(allureCliDir)
}

fun allureCommand(vararg args: String): List<String> {
    val binDir = allureCliDir.get().asFile.resolve("allure-$allureVersion/bin")
    val isWindows = System.getProperty("os.name").lowercase().contains("windows")
    return if (isWindows) {
        listOf("cmd", "/c", binDir.resolve("allure.bat").absolutePath) + args
    } else {
        listOf(binDir.resolve("allure").absolutePath) + args
    }
}

tasks.register<Delete>("cleanAllureResults") {
    group = "reporting"
    description = "Удаляет результаты предыдущих прогонов из build/allure-results"
    delete(allureResultsDir)
}

tasks.register<Exec>("allureReport") {
    group = "reporting"
    description = "Генерирует HTML Allure-отчёт в build/allure-report"
    dependsOn(unzipAllureCli)
    commandLine(allureCommand(
        "generate", allureResultsDir.get().asFile.absolutePath,
        "-o", allureReportDir.get().asFile.absolutePath, "--clean"
    ))
}

tasks.register<Exec>("allureServe") {
    group = "reporting"
    description = "Открывает Allure-отчёт в браузере (Ctrl+C, чтобы остановить)"
    dependsOn(unzipAllureCli)
    commandLine(allureCommand("serve", allureResultsDir.get().asFile.absolutePath))
}
