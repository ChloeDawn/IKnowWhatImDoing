package dev.sapphic.iknowwhatimdoing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

@Mod("iknowwhatimdoing")
public final class IKnowWhatImDoing {
  @SuppressWarnings("serial")
  public IKnowWhatImDoing() {
    ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> {
      return Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (s, v) -> true);
    });
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
      @Override
      public void run() {
        MinecraftForge.EVENT_BUS.addListener(new Consumer<GuiOpenEvent>() {
          @Override
          public void accept(final GuiOpenEvent event) {
            if (event.getGui() instanceof MainMenuScreen) {
              Minecraft.getInstance().getTutorial().setStep(TutorialSteps.NONE);
              MinecraftForge.EVENT_BUS.unregister(this);
            }
          }
        });
      }
    });
  }
}
