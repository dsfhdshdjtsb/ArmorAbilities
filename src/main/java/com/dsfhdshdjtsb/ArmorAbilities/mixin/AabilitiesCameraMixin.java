package com.dsfhdshdjtsb.ArmorAbilities.mixin;

import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class AabilitiesCameraMixin {

    @Inject(at = @At("HEAD"), method = "isThirdPerson", cancellable = true)
    private void isThirdPersonInject(CallbackInfoReturnable<Boolean> cir)
    {
        TimerAccess timerAccess = (TimerAccess) MinecraftClient.getInstance().player;
        if(timerAccess.aabilities_getFuse() >= 0|| timerAccess.aabilities_getShouldAnvilRender())
        {
            cir.setReturnValue(true);
        }
    }
}
