package dev.sapphic.iknowwhatimdoing;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.tutorial.TutorialManager;
import net.minecraft.client.tutorial.TutorialStep;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
abstract class IKnowWhatImDoing {
  @Shadow @Final private TutorialManager tutorialManager;

  @Inject(method = "<init>", at = @At("RETURN"))
  private void clearTutorial(final RunArgs args, final CallbackInfo ci) {
    this.tutorialManager.setStep(TutorialStep.NONE);
  }
}
