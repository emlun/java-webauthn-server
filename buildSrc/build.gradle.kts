plugins {
  groovy
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("commons-io:commons-io:2.11.0")
  implementation("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.5.1")
}
