import net.minecraftforge.gradle.common.task.SignJar
import org.gradle.util.GradleVersion
import java.time.Instant

plugins {
  id("net.minecraftforge.gradle") version "4.1.12"
  id("net.nemerosa.versioning") version "2.14.0"
  id("signing")
}

group = "dev.sapphic"
version = "3.2.0"

java {
  withSourcesJar()
}

minecraft {
  mappings("official", "1.16.5")
  runs {
    create("client") {
      workingDirectory = file("run").canonicalPath
      mods.create("iknowwhatimdoing").source(sourceSets["main"])
      property("forge.logging.console.level", "debug")
    }
    create("server") {
      workingDirectory = file("run").canonicalPath
      mods.create("iknowwhatimdoing").source(sourceSets["main"])
      property("forge.logging.console.level", "debug")
    }
  }
}

dependencies {
  minecraft("net.minecraftforge:forge:1.16.5-36.1.32")
  implementation("org.checkerframework:checker-qual:3.14.0")
}

tasks {
  compileJava {
    with(options) {
      release.set(8)
      isFork = true
      isDeprecation = true
      encoding = "UTF-8"
      compilerArgs.addAll(listOf("-Xlint:all", "-parameters"))
    }
  }

  jar {
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

  if (project.hasProperty("signing.mods.keyalias")) {
    val keyalias = project.property("signing.mods.keyalias")
    val keystore = project.property("signing.mods.keystore")
    val password = project.property("signing.mods.password")

    val signJar by creating(SignJar::class) {
      dependsOn(reobf)

      setAlias(keyalias)
      setKeyStore(keystore)
      setKeyPass(password)
      setStorePass(password)
      setInputFile(jar.get().archiveFile)
      setOutputFile(inputFile)

      doLast {
        signing.sign(inputFile)
      }
    }

    val signSourcesJar by creating(SignJar::class) {
      val sourcesJar by getting(Jar::class)

      dependsOn(sourcesJar)

      setAlias(keyalias)
      setKeyStore(keystore)
      setKeyPass(password)
      setStorePass(password)
      setInputFile(sourcesJar.archiveFile)
      setOutputFile(inputFile)

      doLast {
        signing.sign(inputFile)
      }
    }

    assemble {
      dependsOn(signJar, signSourcesJar)
    }
  }
}
