package com.dsfhdshdjtsb.ArmorAbilities.client;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.mixin.AabilitiesLivingEntityMixin;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class CooldownHud extends DrawableHelper {
    private static final Identifier COOLDOWN_BAR_TEXTURE = new Identifier(ArmorAbilities.modid, "icon.png");

    private final int barWidth = 182;   // Width of the cooldown bar texture
    private final int barHeight = 5;    // Height of the cooldown bar texture

    private final int x = 100;            // X position of the cooldown bar UI
    private final int y = 100;            // Y position of the cooldown bar UI

    public void render(MatrixStack matrixStack, float cooldownPercent1) {
        MinecraftClient client = MinecraftClient.getInstance();
        TimerAccess timerAccess =  ((TimerAccess) client.player);

        if(timerAccess != null) {
            System.out.println(timerAccess.aabilities_getLeggingCooldown());
            long cooldown = timerAccess.aabilities_getLeggingCooldown();
            float cooldownPercent = (100 - cooldown) / 100.0f;
            Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            buffer.vertex(positionMatrix, 20, 20, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
            buffer.vertex(positionMatrix, 20, 60, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f).next();
            buffer.vertex(positionMatrix, 60, 60, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f).next();
            buffer.vertex(positionMatrix, 60, 20, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f).next();

            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
            RenderSystem.setShaderTexture(0, new Identifier(ArmorAbilities.modid, "/textures/gui/helmet.png"));
            RenderSystem.setShaderColor(Math.min(cooldownPercent, 1f), Math.min(cooldownPercent, 1f), Math.min(cooldownPercent, 1f), Math.min(cooldownPercent, 1f));


            tessellator.draw();
        }
    }
}