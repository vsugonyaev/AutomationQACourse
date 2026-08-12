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

tasks.register("printTestRunOver") {
        group = "verification"
        doLast {
            println()
            println("======================================")
            println("         TEST RUN IS OVER")
            println("======================================")
        }
}