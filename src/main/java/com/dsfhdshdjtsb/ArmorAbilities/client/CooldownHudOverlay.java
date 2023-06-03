package com.dsfhdshdjtsb.ArmorAbilities.client;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CooldownHudOverlay implements HudRenderCallback {

    private static final Identifier COOLDOWN_HELMET =  new Identifier(ArmorAbilities.modid, "/textures/gui/helmet2.png");
    private static final Identifier COOLDOWN_CHESTPLATE =  new Identifier(ArmorAbilities.modid, "/textures/gui/chestplate.png");
    private static final Identifier COOLDOWN_LEGGINGS =  new Identifier(ArmorAbilities.modid, "/textures/gui/leggings.png");
    private static final Identifier COOLDOWN_BOOTS =  new Identifier(ArmorAbilities.modid, "/textures/gui/boots2.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {

        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();


        if(client != null)
        {
            TextRenderer textRenderer = client.textRenderer;
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
                    RenderSystem.setShaderColor(color, color, color, 1.0f);
                    RenderSystem.setShaderTexture(0, COOLDOWN_HELMET);

                    DrawableHelper.drawTexture(matrixStack, x - 187, y - 23, 0, 0, 20, 20, 20, 20);
//                    String cooldownText = Integer.toString((int) (curCooldown / 20));

//                    textRenderer.draw(matrixStack, cooldownText, x - 180, y - 20, 0xFFFFFFFF);
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
                    RenderSystem.setShaderColor(color, color, color, 1.0f);
                    RenderSystem.setShaderTexture(0, COOLDOWN_CHESTPLATE);
                    DrawableHelper.drawTexture(matrixStack, x - 172, y - 23, 0, 0, 20, 20, 20, 20);
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
                    RenderSystem.setShaderColor(color, color, color, 1.0f);
                    RenderSystem.setShaderTexture(0, COOLDOWN_LEGGINGS);
                    DrawableHelper.drawTexture(matrixStack, x - 157, y - 23, 0, 0, 20, 20, 20, 20);
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
                    RenderSystem.setShaderColor(color, color, color, 1.0f);
                    RenderSystem.setShaderTexture(0, COOLDOWN_BOOTS);
                    DrawableHelper.drawTexture(matrixStack, x - 141, y - 23, 0, 0, 20, 20, 20, 20);

                }
            }

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        }


    }
}
