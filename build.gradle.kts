import java.time.Instant
import org.gradle.jvm.tasks.Jar

plugins {
  id("net.minecraftforge.gradle") version "3.0.184"
  id("signing")
}

group = "dev.sapphic"
version = "3.1.0"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = sourceCompatibility
}

minecraft {
  mappings("snapshot", "20200723-1.16.1")
  runs {
    with(create("client")) {
      workingDirectory = file("run").canonicalPath
      mods.create("iknowwhatimdoing").source(sourceSets["main"])
    }
  }
}

dependencies {
  minecraft("net.minecraftforge:forge:1.16.2-33.0.22")
}

tasks.withType<Jar> {
  from("LICENSE")
  archiveClassifier.set("forge")
  manifest.attributes(mapOf(
    "Specification-Title" to project.name,
    "Specification-Vendor" to project.group,
    "Specification-Version" to 1,
    "Implementation-Title" to project.name,
    "Implementation-Version" to project.version,
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
