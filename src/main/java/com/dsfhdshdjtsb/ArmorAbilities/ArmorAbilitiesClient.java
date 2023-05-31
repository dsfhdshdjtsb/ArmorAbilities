package com.dsfhdshdjtsb.ArmorAbilities;

import com.dsfhdshdjtsb.ArmorAbilities.client.CooldownHud;
import com.dsfhdshdjtsb.ArmorAbilities.client.CooldownHudOverlay;
import com.dsfhdshdjtsb.ArmorAbilities.event.KeyInputHandler;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class ArmorAbilitiesClient implements ClientModInitializer {

    private static KeyBinding keyBinding;
    private static final Identifier TNT_TEXTURE = new Identifier("minecraft", "textures/entity/tnt/tnt.png");

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        // Event registration will be executed inside this method
        ModPackets.registerS2CPackets();
        EntityRendererRegistry.register(ArmorAbilities.LASER_PROJECTILE_ENTITY_TYPE, FlyingItemEntityRenderer::new);

//        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
//            CooldownHud cooldownHud = new CooldownHud();
//            cooldownHud.render(matrixStack, tickDelta);
//        });
        HudRenderCallback.EVENT.register(new CooldownHudOverlay());


    }
}
