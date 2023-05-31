package com.dsfhdshdjtsb.ArmorAbilities.client;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CooldownHudOverlay implements HudRenderCallback {

    private static final Identifier COOLDOWN_HELMET =  new Identifier(ArmorAbilities.modid, "/textures/gui/helmet2.png");
    private static final Identifier COOLDOWN_CHESTPLATE =  new Identifier(ArmorAbilities.modid, "/textures/gui/chestplate.png");
    private static final Identifier COOLDOWN_LEGGINGS =  new Identifier(ArmorAbilities.modid, "/textures/gui/leggings.png");
    private static final Identifier COOLDOWN_BOOTS =  new Identifier(ArmorAbilities.modid, "/textures/gui/boots.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {

        int x = 0;
        int y = 0;
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

                if (timerAccess.aabilities_getHelmetCooldown() < 30) {
                    color = (float) Math.abs(Math.sin(client.world.getTime()/2.0));
                }
                else if (timerAccess.aabilities_getHelmetCooldown() < 60 )
                {
                    color = (float) Math.abs(Math.sin(client.world.getTime()/10.0));
                }
                else
                {
                    color = 1.0f;
                }
                RenderSystem.setShaderColor(color, color, color, 1.0f);
                RenderSystem.setShaderTexture(0, COOLDOWN_HELMET);
                if(timerAccess.aabilities_getHelmetCooldown() > 0 )
                    DrawableHelper.drawTexture(matrixStack, x - 180, y - 25, 0, 0, 20, 20, 20, 20);


                if (timerAccess.aabilities_getChestCooldown() < 30) {
                    color = (float) Math.abs(Math.sin(client.world.getTime()/2.0));
                }
                else if (timerAccess.aabilities_getChestCooldown() < 60 )
                {
                    color = (float) Math.abs(Math.sin(client.world.getTime()/10.0));
                }
                else
                {
                    color = 1.0f;
                }
                RenderSystem.setShaderColor(color, color, color, 1.0f);
                RenderSystem.setShaderTexture(0, COOLDOWN_CHESTPLATE);
                if(timerAccess.aabilities_getChestCooldown() > 0 )
                    DrawableHelper.drawTexture(matrixStack, x - 165, y - 25, 0, 0, 20, 20, 20, 20);


                if (timerAccess.aabilities_getLeggingCooldown() < 30) {
                    color = (float) Math.abs(Math.sin(client.world.getTime()/2.0));
                }
                else if (timerAccess.aabilities_getLeggingCooldown() < 60 )
                {
                    color = (float) Math.abs(Math.sin(client.world.getTime()/10.0));
                }
                else
                {
                    color = 1.0f;
                }
                RenderSystem.setShaderColor(color, color, color, 1.0f);
                RenderSystem.setShaderTexture(0, COOLDOWN_LEGGINGS);
                if(timerAccess.aabilities_getLeggingCooldown() > 0 )
                    DrawableHelper.drawTexture(matrixStack, x - 150, y - 25, 0, 0, 20, 20, 20, 20);


                if (timerAccess.aabilities_getBootCooldown() < 30) {
                    color = (float) Math.abs(Math.sin(client.world.getTime()/2.0));
                }
                else if (timerAccess.aabilities_getBootCooldown() < 60 )
                {
                    color = (float) Math.abs(Math.sin(client.world.getTime()/10.0));
                }
                else
                {
                    color = 1.0f;
                }
                RenderSystem.setShaderColor(color, color, color, 1.0f);
                RenderSystem.setShaderTexture(0, COOLDOWN_BOOTS);
                if(timerAccess.aabilities_getBootCooldown() > 0 )
                    DrawableHelper.drawTexture(matrixStack, x - 135, y - 25, 0, 0, 20, 20, 20, 20);

                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }


        }


    }
}
