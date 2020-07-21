package dev.sapphic.iknowwhatimdoing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("iknowwhatimdoing")
public final class IKnowWhatImDoing {
  public IKnowWhatImDoing() {
    FMLJavaModLoadingContext.get().getModEventBus().<FMLClientSetupEvent>addListener(e ->
      Minecraft.getInstance().getTutorial().setStep(TutorialSteps.NONE));
  }
}
