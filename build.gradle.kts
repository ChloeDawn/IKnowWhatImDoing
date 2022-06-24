import java.time.Instant
import net.minecraftforge.gradle.common.tasks.SignJar

plugins {
  id("net.minecraftforge.gradle") version "5.1.48"
  id("net.nemerosa.versioning") version "3.0.0"
  id("org.gradle.signing")
}

group = "dev.sapphic"
version = "5.1.0"

java {
  withSourcesJar()
}

minecraft {
  mappings("official", "1.19")
  runs {
    listOf("client", "server").forEach {
      create(it) {
        mods.create("iknowwhatimdoing").source(sourceSets["main"])
        property("forge.logging.console.level", "debug")
        property("forge.logging.markers", "SCAN")
      }
    }
  }
}

dependencies {
  minecraft("net.minecraftforge:forge:1.19-41.0.45")
  implementation("org.checkerframework:checker-qual:3.22.1")
}

tasks {
  compileJava {
    with(options) {
      isDeprecation = true
      encoding = "UTF-8"
      isFork = true
      compilerArgs.addAll(
        listOf(
          "-Xlint:all", "-Xlint:-processing",
          // Enable parameter name class metadata 
          // https://openjdk.java.net/jeps/118
          "-parameters"
        )
      )
      release.set(17)
    }
  }

  jar {
    archiveClassifier.set("forge")

    from("/LICENSE")

    manifest.attributes(
      "Build-Timestamp" to Instant.now(),
      "Build-Revision" to versioning.info.commit,
      "Build-Jvm" to "${
        System.getProperty("java.version")
      } (${
        System.getProperty("java.vendor")
      } ${
        System.getProperty("java.vm.version")
      })",
      "Built-By" to GradleVersion.current(),

      "Implementation-Title" to project.name,
      "Implementation-Version" to project.version,
      "Implementation-Vendor" to project.group,

      "Specification-Title" to "ForgeMod",
      "Specification-Version" to "1.1.0",
      "Specification-Vendor" to project.group,
      
      "Sealed" to true
    )

    finalizedBy(reobf)
  }

  val sourcesJar by getting(Jar::class) {
    archiveClassifier.set("forge-${archiveClassifier.get()}")
  }

  if (project.hasProperty("signing.mods.keyalias")) {
    val keyalias = project.property("signing.mods.keyalias") as String
    val keystore = project.property("signing.mods.keystore") as String
    val password = project.property("signing.mods.password") as String

    val signJar by creating(SignJar::class) {
      dependsOn(reobf)

      alias.set(keyalias)
      keyStore.set(keystore)
      keyPass.set(password)
      storePass.set(password)
      inputFile.set(jar.get().archiveFile)
      outputFile.set(inputFile)

      doLast {
        signing.sign(outputFile.get().asFile)
      }
    }

    val signSourcesJar by creating(SignJar::class) {
      dependsOn(sourcesJar)

      alias.set(keyalias)
      keyStore.set(keystore)
      keyPass.set(password)
      storePass.set(password)
      inputFile.set(sourcesJar.archiveFile)
      outputFile.set(inputFile)

      doLast {
        signing.sign(outputFile.get().asFile)
      }
    }

    assemble {
      dependsOn(signJar, signSourcesJar)
    }
  }
}
