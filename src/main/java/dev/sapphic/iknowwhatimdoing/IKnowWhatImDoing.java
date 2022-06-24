/*
 * Copyright 2022 Chloe Dawn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.sapphic.iknowwhatimdoing;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.tutorial.TutorialSteps;

import java.io.Serializable;

public final class IKnowWhatImDoing implements ClientModInitializer, Serializable {
  private static final long serialVersionUID = -6194609073673544528L;

  @Override
  public void onInitializeClient() {
    Minecraft.getInstance().execute(() -> {
      Minecraft.getInstance().getTutorial().setStep(TutorialSteps.NONE);
    });
  }

  @Override
  public String toString() {
    return "IKnowWhatImDoing";
  }
}
