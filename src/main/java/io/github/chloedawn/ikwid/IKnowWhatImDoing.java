package io.github.chloedawn.ikwid;

import net.minecraft.client.tutorial.TutorialStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class IKnowWhatImDoing {
  private static final Logger LOGGER = LogManager.getLogger();

  private IKnowWhatImDoing() {
  }

  public static TutorialStep getNewTutorialStep() {
    LOGGER.debug("Redirecting options tutorial step assignment to {}", TutorialStep.NONE::getName);
    return TutorialStep.NONE;
  }
}
