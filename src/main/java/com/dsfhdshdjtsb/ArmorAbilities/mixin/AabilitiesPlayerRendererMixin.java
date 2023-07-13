package com.dsfhdshdjtsb.ArmorAbilities.mixin;

import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class AabilitiesPlayerRendererMixin {

    @Inject(at = @At("HEAD"), method ="renderArm", cancellable = true)
    private void renderHandMixin(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci)
    {
        TimerAccess timerAccess = (TimerAccess) MinecraftClient.getInstance().player;
        if(timerAccess.aabilities_getFuse() > 0 || timerAccess.aabilities_getShouldAnvilRender())
        {
            ci.cancel();
        }
    }
}
