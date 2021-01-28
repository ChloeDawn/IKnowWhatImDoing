package dev.sapphic.iknowwhatimdoing;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.tutorial.TutorialSteps;

public final class IKnowWhatImDoing implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    Minecraft.getInstance().execute(() -> {
      Minecraft.getInstance().getTutorial().setStep(TutorialSteps.NONE);
    });
  }
}
