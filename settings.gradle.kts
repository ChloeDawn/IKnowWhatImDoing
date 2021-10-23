pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://files.minecraftforge.net/maven")
  }

  resolutionStrategy {
    eachPlugin {
      if ("net.minecraftforge.gradle" == requested.id.id) {
        useModule("net.minecraftforge.gradle:ForgeGradle:${requested.version}")
      }
    }
  }
}

rootProject.name = "IKnowWhatImDoing"
