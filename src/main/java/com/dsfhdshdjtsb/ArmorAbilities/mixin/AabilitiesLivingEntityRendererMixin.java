package com.dsfhdshdjtsb.ArmorAbilities.mixin;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Timer;

@Mixin(LivingEntityRenderer.class)
public abstract class AabilitiesLivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {
    protected AabilitiesLivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", cancellable = true)
    private void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci)
    {
        if(livingEntity instanceof PlayerEntity) {
            TimerAccess timerAccess = (TimerAccess) livingEntity;
            int fuse = (int) timerAccess.aabilities_getFuse();
            boolean shouldRenderAnvil = timerAccess.aabilities_getShouldAnvilRender();
            if (fuse > 0) {
                matrixStack.push();
                matrixStack.translate(0.0f, 0.5f, 0.0f);
                if ((float) fuse - g + 1.0f < 10.0f) {
                    float h = 1.0f - ((float) fuse - g + 1.0f) / 10.0f;
                    h = MathHelper.clamp(h, 0.0f, 1.0f);
                    h *= h;
                    h *= h;
                    float k = 1.0f + h * 0.3f;
                    matrixStack.scale(k, k, k);
                }
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0f));
                matrixStack.translate(-0.5f, -0.5f, 0.5f);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
                TntMinecartEntityRenderer.renderFlashingBlock(MinecraftClient.getInstance().getBlockRenderManager(), Blocks.TNT.getDefaultState(), matrixStack, vertexConsumerProvider, i, fuse / 5 % 2 == 0);
                matrixStack.pop();
                ci.cancel();
            }
            else if(shouldRenderAnvil)
            {
                BlockState blockState = Blocks.ANVIL.getDefaultState();
                if (blockState.getRenderType() != BlockRenderType.MODEL) {
                    return;
                }
                World world = livingEntity.getWorld();
//                if (blockState == world.getBlockState(livingEntity.getBlockPos()) || blockState.getRenderType() == BlockRenderType.INVISIBLE) {
//                    return;
//                }
                matrixStack.push();
                BlockPos blockPos = BlockPos.ofFloored(livingEntity.getX(), livingEntity.getBoundingBox().maxY, livingEntity.getZ());
                matrixStack.translate(-0.5, 0.0, -0.5);
                BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
                blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockPos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, Random.create(), blockState.getRenderingSeed(livingEntity.getBlockPos()), OverlayTexture.DEFAULT_UV);
                matrixStack.pop();
                ci.cancel();
            }
        }

    }


}
