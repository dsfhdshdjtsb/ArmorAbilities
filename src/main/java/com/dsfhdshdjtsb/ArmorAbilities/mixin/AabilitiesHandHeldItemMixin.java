package com.dsfhdshdjtsb.ArmorAbilities.mixin;

import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class AabilitiesHandHeldItemMixin {

    @Inject(at = @At("HEAD"), method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", cancellable = true)
    private void renderInject(float tickDelta, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci)
    {
        TimerAccess timerAccess = (TimerAccess) MinecraftClient.getInstance().player;
        if(timerAccess.aabilities_getFuse() >= 0|| timerAccess.aabilities_getShouldAnvilRender())
        {
            ci.cancel();
        }
    }
}
