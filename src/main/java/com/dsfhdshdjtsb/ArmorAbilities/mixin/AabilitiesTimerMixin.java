package com.dsfhdshdjtsb.ArmorAbilities.mixin;

import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public class AabilitiesTimerMixin implements TimerAccess {

    @Unique
    private long ticksUntilFireStomp;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo c1)
    {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if(--this.ticksUntilFireStomp >= 0L && (player).isOnGround())
        {
            this.ticksUntilFireStomp = 0;
            System.out.println("stomp");
            List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                    .expand(7, 2, 7));

            list.remove(player);
            if(!list.isEmpty()) {
                for (LivingEntity e : list) {
                    e.setFireTicks(100);
                    e.damage(DamageSource.MAGIC, 4);
                    World world = e.world;
                    BlockPos pos = e.getBlockPos();
                    if (world.getBlockState(pos) == Blocks.AIR.getDefaultState()) {
                        BlockState fire = Blocks.FIRE.getDefaultState();
                        world.setBlockState(pos, fire);
                    }
                }
                for (double i = player.getX() - 5; i <= player.getX() + 5; i++) {
                    for (double j = player.getZ() - 5; j <= player.getZ() + 5; j++) {
                        int x = MathHelper.floor(i);
                        int y = MathHelper.floor(player.getY() - 0.2);
                        int z = MathHelper.floor(j);

                        BlockPos blockPos = new BlockPos(x, y, z);
                        BlockState blockState = player.world.getBlockState(blockPos);
                        ((ServerWorld) player.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), x,
                                y + 1, z, 4, 1, 0.0D, 1, 0.0D);
                    }
                }
            }
        }
    }
    @Override
    public void aabilites_setTimer(long ticksUntilFireStomp) {
        this.ticksUntilFireStomp = ticksUntilFireStomp;
    }
}
