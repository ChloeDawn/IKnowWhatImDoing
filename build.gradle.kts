import java.time.Instant

plugins {
  id("fabric-loom") version "0.4.33"
  id("signing")
}

group = "dev.sapphic"
version = "3.0.1"

dependencies {
  minecraft("com.mojang:minecraft:1.16.1")
  mappings("net.fabricmc:yarn:1.16.1+build.21:v2")
  modImplementation("net.fabricmc:fabric-loader:0.9.0+build.204")
  implementation("org.jetbrains:annotations:20.1.0")
  implementation("org.checkerframework:checker-qual:3.6.1")
}

minecraft {
  refmapName = "mixins/iknowwhatimdoing/refmap.json"
  runDir = "run"
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = sourceCompatibility
}

signing {
  useGpgCmd()
  sign(configurations.archives.get())
}

tasks {
  named<JavaCompile>("compileJava") {
    with(options) {
      isFork = true
      isDeprecation = true
      encoding = "UTF-8"
      compilerArgs.addAll(listOf(
        "-Xlint:all", "-parameters"
      ))
    }
  }

  named<ProcessResources>("processResources") {
    filesMatching("/fabric.mod.json") {
      expand("version" to version)
    }
  }

  named<Jar>("jar") {
    from("/LICENSE")
    afterEvaluate {
      archiveClassifier.set("fabric")
    }
    manifest.attributes(mapOf(
      "Specification-Title" to project.name,
      "Specification-Vendor" to project.group,
      "Specification-Version" to 1,
      "Implementation-Title" to project.name,
      "Implementation-Version" to project.version,
      "Implementation-Vendor" to project.group,
      "Implementation-Timestamp" to "${Instant.now()}"
    ))
  }
}
