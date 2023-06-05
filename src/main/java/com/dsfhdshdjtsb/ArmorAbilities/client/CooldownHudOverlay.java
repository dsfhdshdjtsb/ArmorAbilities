package com.dsfhdshdjtsb.ArmorAbilities.client;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class CooldownHudOverlay implements HudRenderCallback {

    private static final Identifier COOLDOWN_HELMET =  new Identifier(ArmorAbilities.modid, "/textures/gui/helmet2.png");
    private static final Identifier COOLDOWN_CHESTPLATE =  new Identifier(ArmorAbilities.modid, "/textures/gui/chestplate.png");
    private static final Identifier COOLDOWN_LEGGINGS =  new Identifier(ArmorAbilities.modid, "/textures/gui/leggings.png");
    private static final Identifier COOLDOWN_BOOTS =  new Identifier(ArmorAbilities.modid, "/textures/gui/boots2.png");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {

        int x;
        int y;
        MinecraftClient client = MinecraftClient.getInstance();


        if(client != null)
        {
            TimerAccess timerAccess =  ((TimerAccess) client.player);
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;
            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);


            if(timerAccess != null && client.world != null) {
                float color;

                double curCooldown = timerAccess.aabilities_getHelmetCooldown();
                if(curCooldown > 0 ) {
                    if (curCooldown < 40) {
                        color = (float) Math.abs(Math.sin(client.world.getTime() / 2.0));
                    } else if (curCooldown < 100) {
                        color = (float) Math.abs(Math.sin(client.world.getTime() / 10.0));
                    } else {
                        color = 1.0f;
                    }
                    context.setShaderColor(color, color, color, 1.0f);
                    context.drawTexture(COOLDOWN_HELMET, x - 187, y - 23, 0, 0, 20, 20, 20, 20);
                }

                curCooldown = timerAccess.aabilities_getChestCooldown();
                if(curCooldown > 0) {
                    if (curCooldown < 40) {
                        color = (float) Math.abs(Math.sin(client.world.getTime() / 2.0));
                    } else if (curCooldown < 100) {
                        color = (float) Math.abs(Math.sin(client.world.getTime() / 10.0));
                    } else {
                        color = 1.0f;
                    }
                    context.setShaderColor(color, color, color, 1.0f);
                    context.drawTexture(COOLDOWN_CHESTPLATE, x - 172, y - 23, 0, 0, 20, 20, 20, 20);
                }

                curCooldown = timerAccess.aabilities_getLeggingCooldown();
                if(curCooldown > 0) {
                    if (curCooldown < 40) {
                        color = (float) Math.abs(Math.sin(client.world.getTime() / 2.0));
                    } else if (curCooldown < 100) {
                        color = (float) Math.abs(Math.sin(client.world.getTime() / 10.0));
                    } else {
                        color = 1.0f;
                    }
                    context.setShaderColor(color, color, color, 1.0f);
                    context.drawTexture(COOLDOWN_LEGGINGS, x - 157, y - 23, 0, 0, 20, 20, 20, 20);
                }

                curCooldown = timerAccess.aabilities_getBootCooldown();
                if(curCooldown > 0) {
                    if (curCooldown < 40) {
                        color = (float) Math.abs(Math.sin(client.world.getTime() / 2.0));
                    } else if (curCooldown < 100) {
                        color = (float) Math.abs(Math.sin(client.world.getTime() / 10.0));
                    } else {
                        color = 1.0f;
                    }
                    context.setShaderColor(color, color, color, 1.0f);
                    context.drawTexture(COOLDOWN_BOOTS, x - 141, y - 23, 0, 0, 20, 20, 20, 20);

                }
            }

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        }


    }
}
