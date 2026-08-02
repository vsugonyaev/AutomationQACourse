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
    dependsOn("printTestRunOver")
    dependsOn("test")
}
tasks.register("printTestRunOver") {
    group = "tests"
    doLast {
        println("Test Run is Over")
    }
}