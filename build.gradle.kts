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
}

tasks.test {
    useJUnitPlatform()
    group = "tests"
}

tasks.register("runAllTests") {
    group = "tests"
    description = "Запускает все тесты проекта"
    dependsOn(tasks.test)
    finalizedBy("printTestRunOver")
}

tasks.register("printTestRunOver") {
        group = "tests"
        doLast {
            println()
            println("======================================")
            println("         TEST RUN IS OVER")
            println("======================================")
        }
}