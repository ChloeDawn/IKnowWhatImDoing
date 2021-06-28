package dev.sapphic.iknowwhatimdoing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod("iknowwhatimdoing")
public final class IKnowWhatImDoing {
  @SuppressWarnings({ "Convert2Lambda", "serial" })
  public IKnowWhatImDoing() {
    ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> {
      return Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (s, v) -> true);
    });
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
      @Override
      public void run() {
        FMLJavaModLoadingContext.get().getModEventBus().<FMLLoadCompleteEvent>addListener(e -> {
          e.enqueueWork(() -> Minecraft.getInstance().getTutorial().setStep(TutorialSteps.NONE));
        });
      }
    });
  }
}
