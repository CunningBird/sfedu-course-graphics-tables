plugins {
    java
}

group = "com.cunningbird.sfedu.graphics"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.6")
    implementation("org.apache.poi:poi-ooxml:5.2.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}