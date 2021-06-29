package dev.sapphic.iknowwhatimdoing;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModWorkManager;
import net.minecraftforge.fml.client.ClientModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@Mod("iknowwhatimdoing")
public final class IKnowWhatImDoing implements Serializable {
  private static final long serialVersionUID = -8646731113218545455L;

  /**
   * Tutorial clearing is performed during the screen opening event as Forge disallows modification of the
   * options.txt file during startup, and the load completion event is posted before the options are loaded
   * from file by the mod loader. As such, we need to ensure that the tutorial step is set after loading.
   *
   * @see GameSettings#save()
   * @see ClientModLoader#finishModLoading(ModWorkManager.DrivenExecutor, Executor)
   */
  public IKnowWhatImDoing() {
    ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> {
      return Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (s, v) -> true);
    });
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
      private static final long serialVersionUID = 2962366856317629997L;

      @Override
      public void run() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, true, new Consumer<GuiOpenEvent>() {
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
