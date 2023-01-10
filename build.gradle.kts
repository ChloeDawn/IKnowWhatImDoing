import java.time.Instant

plugins {
  id(/*net.fabricmc.*/ "fabric-loom") version "1.0.17"
  id("net.nemerosa.versioning") version "3.0.0"
  id("org.gradle.signing")
}

group = "dev.sapphic"
version = "4.1.1"

java {
  withSourcesJar()
}

loom {
  runs {
    configureEach {
      vmArgs("-Xmx4G", "-XX:+UseZGC")
    }
  }
}

repositories {
  maven("https://maven.terraformersmc.com/releases") {
    content {
      includeGroup("com.terraformersmc")
    }
  }
}

dependencies {
  minecraft("com.mojang:minecraft:1.19.3")
  mappings(loom.layered {
    officialMojangMappings {
      nameSyntheticMembers = true
    }
  })

  modImplementation("net.fabricmc:fabric-loader:0.14.12")

  implementation("org.jetbrains:annotations:23.1.0")
  implementation("org.checkerframework:checker-qual:3.29.0")

  modRuntimeOnly("com.terraformersmc:modmenu:5.0.2")
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
      release.set(8)
    }
  }

  processResources {
    filesMatching("/fabric.mod.json") {
      expand("version" to project.version)
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

      "Specification-Title" to "FabricMod",
      "Specification-Version" to "1.0.0",
      "Specification-Vendor" to project.group,

      "Sealed" to "true"
    )
  }

  remapJar {
    archiveClassifier.set("fabric")
  }

  remapSourcesJar {
    archiveClassifier.set("fabric-sources")
  }

  if (hasProperty("signing.mods.keyalias")) {
    val alias = property("signing.mods.keyalias")
    val keystore = property("signing.mods.keystore")
    val password = property("signing.mods.password")

    fun Sign.antSignJar(task: Task) = task.outputs.files.forEach { file ->
      ant.invokeMethod(
        "signjar", mapOf(
          "jar" to file,
          "alias" to alias,
          "storepass" to password,
          "keystore" to keystore,
          "verbose" to true,
          "preservelastmodified" to true
        ))
    }

    val signJar by creating(Sign::class) {
      dependsOn(remapJar)

      doFirst {
        antSignJar(remapJar.get())
      }

      sign(remapJar.get())
    }

    val signSourcesJar by creating(Sign::class) {
      dependsOn(remapSourcesJar)

      doFirst {
        antSignJar(remapSourcesJar.get())
      }

      sign(remapSourcesJar.get())
    }

    assemble {
      dependsOn(signJar, signSourcesJar)
    }
  }
}
