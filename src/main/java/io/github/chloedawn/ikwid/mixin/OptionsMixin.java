package io.github.chloedawn.ikwid.mixin;

import io.github.chloedawn.ikwid.IKnowWhatImDoing;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.tutorial.TutorialStep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import org.objectweb.asm.Opcodes;

@Mixin(GameOptions.class)
abstract class OptionsMixin {
  @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/client/tutorial/TutorialStep;MOVEMENT:Lnet/minecraft/client/tutorial/TutorialStep;"))
  private TutorialStep redirectTutorialStepAssignment() {
    return IKnowWhatImDoing.getNewTutorialStep();
  }
}
