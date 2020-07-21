import java.time.Instant
import org.gradle.jvm.tasks.Jar

plugins {
  id("net.minecraftforge.gradle") version "3.0.181"
  id("signing")
}

group = "dev.sapphic"
version = "3.0.0"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = sourceCompatibility
}

minecraft {
  mappings("snapshot", "20200721-1.15.1")
  runs {
    with(create("client")) {
      workingDirectory = file("run").canonicalPath
      mods.create("iknowwhatimdoing").source(sourceSets["main"])
    }
  }
}

dependencies {
  minecraft("net.minecraftforge:forge:1.15.2-31.2.31")
}

tasks.withType<Jar> {
  archiveClassifier.set("forge")
  manifest.attributes(mapOf(
    "Specification-Title" to project.name,
    "Specification-Vendor" to project.group,
    "Specification-Version" to "24.0",
    "Implementation-Title" to project.name,
    "Implementation-Version" to 1,
    "Implementation-Vendor" to project.group,
    "Implementation-Timestamp" to Instant.now().toString()
  ))
  finalizedBy("reobfJar")
}

tasks.withType<JavaCompile> {
  with(options) {
    isFork = true
    isDeprecation = true
    encoding = "UTF-8"
    compilerArgs.addAll(listOf(
      "-Xlint:all", "-parameters"
    ))
  }
}

signing {
  useGpgCmd()
  sign(configurations.archives.get())
}
