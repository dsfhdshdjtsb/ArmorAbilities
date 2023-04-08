package com.dsfhdshdjtsb.ArmorAbilities.mixin;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.event.KeyInputHandler;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public  class AabilitiesLivingEntityMixin implements TimerAccess {

    @Unique
    private long ticksUntilFireStomp;
    private long ticksFireStompAnim = -1;
    private long ticksUntilFrostStomp;
    private long ticksFrostStompAnim = -1;
    private long ticksTranscend;

    public long helmetCooldown = 0;
    public long chestCooldown = 0;
    public long leggingCooldown = 0;
    public long bootCooldown = 0;


    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if(player.hasStatusEffect(ArmorAbilities.DODGE_EFFECT))
        {
            ((ServerWorld) player.world).spawnParticles(ParticleTypes.POOF, player.getX(), player.getBodyY(0.5D), player.getZ(), 5, 0.3, 0.5, 0.3, 0.0D);
            cir.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo c1)
    {
        helmetCooldown--;
        chestCooldown--;
        leggingCooldown--;
        bootCooldown--;
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if(--this.ticksTranscend >= 0L)
        {
            if(this.ticksTranscend < 185)
            {
                if(this.ticksTranscend > 180)
                {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 600, 4));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 180, 2));
                }
                player.setVelocity(0, 0, 0);
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeDouble(0);
                buf.writeDouble(0);
                buf.writeDouble(0);
                ServerPlayNetworking.send((ServerPlayerEntity) player, ModPackets.VELOCITY_UPDATE_ID, buf);
                BeaconBlockEntity.BeamSegment beamSegment = new BeaconBlockEntity.BeamSegment(new float[]{255, 255, 255});
                BeaconBlockEntity test = new BeaconBlockEntity(new BlockPos((int)player.getX(), (int)player.getY(), (int)player.getZ()), null);
                BeaconBlockEntityRenderer renderer = new BeaconBlockEntityRenderer(null);
            }
        }
        if(--this.ticksUntilFireStomp >= 0L && (player).isOnGround())
        {
            this.ticksUntilFireStomp = 0;
            System.out.println("stomp");
            List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                    .expand(7, 2, 7));

            list.remove(player);
            if(!list.isEmpty()) {
                for (LivingEntity e : list) {
                    e.setFireTicks(60);
                    e.damage(player.world.getDamageSources().magic(), 4);
                    World world = e.world;
                    BlockPos pos = e.getBlockPos();
                    if (world.getBlockState(pos) == Blocks.AIR.getDefaultState()) {
                        BlockState fire = Blocks.FIRE.getDefaultState();
                        world.setBlockState(pos, fire);
                    }
                }
            }
            player.world.playSound(
                    null,
                    new BlockPos((int)player.getX(), (int)player.getY(), (int)player.getZ()),
                    SoundEvents.ENTITY_GENERIC_SMALL_FALL,
                    SoundCategory.PLAYERS,
                    1f,
                    1f
            );
            ((TimerAccess) player).aabilities_setFireStompAnimTimer(5);
        }
        if(--ticksFireStompAnim >= 0)
        {
            for (double i = 0; i <= Math.PI * 2; i += Math.PI / 6) {
//                for (double j = player.getZ() - 5 + ticksFireStompAnim; j <= player.getZ() + 5- ticksFireStompAnim; j++) {
////                    int x = MathHelper.floor(i);
////                    int y = MathHelper.floor(player.getY() - 0.2);
////                    int z = MathHelper.floor(j);
//
//                }
                double x = player.getX() + Math.sin(i) * (7 - ticksFireStompAnim * 1.5);
                double y = MathHelper.floor(player.getY() - 0.2);
                double z = player.getZ() + Math.cos(i) * (7 - ticksFireStompAnim * 1.5);

                BlockPos blockPos = new BlockPos((int)x, (int)y, (int)z);
                BlockState blockState = player.world.getBlockState(blockPos);
                ((ServerWorld) player.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), x,
                        y + 1, z, 4, 1, 0.0D, 1, 0.0D);

            }
            for(int j = 0; j < (5-ticksFireStompAnim); j++) {
                if(Math.random() > 0.7) {
                    double randX = player.getX() + (Math.random() - 0.5) * 2 * (7 - ticksFireStompAnim * 1.5);
                    double randY = MathHelper.floor(player.getY() - 0.2);
                    double randZ = player.getZ() + (Math.random() - 0.5) * 2 * (7 - ticksFireStompAnim * 1.5);

                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.FLAME, randX,
                            randY + 1, randZ, 1, 0.5, 0.0D, 0.5, 0.0D);
                }
            }
        }

        if(--this.ticksUntilFrostStomp >= 0L && (player).isOnGround())
        {
            this.ticksUntilFrostStomp = 0;
            System.out.println("stomp");
            List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                    .expand(7, 2, 7));

            list.remove(player);
            if(!list.isEmpty()) {
                for (LivingEntity e : list) {
                    e.setFrozenTicks(155 + 300);
                    e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 0));
                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.SNOWFLAKE, e.getX(),
                            e.getBodyY(0.5D) - 1, e.getZ(), 10, 0.5, 0.0D, 0.5, 0.0D);
                }
            }
            player.world.playSound(
                    null,
                    new BlockPos((int)player.getX(), (int)player.getY(), (int)player.getZ()),
                    SoundEvents.ENTITY_GENERIC_SMALL_FALL,
                    SoundCategory.PLAYERS,
                    1f,
                    1f
            );
            ((TimerAccess) player).aabilities_setFrostStompAnimTimer(5);
        }

        if(--ticksFrostStompAnim >= 0)
        {
            for (double i = 0; i <= Math.PI * 2; i += Math.PI / 6) {
//                for (double j = player.getZ() - 5 + ticksFireStompAnim; j <= player.getZ() + 5- ticksFireStompAnim; j++) {
////                    int x = MathHelper.floor(i);
////                    int y = MathHelper.floor(player.getY() - 0.2);
////                    int z = MathHelper.floor(j);
//
//                }
                double x = player.getX() + Math.sin(i) * (7 - ticksFrostStompAnim * 1.5);
                double y = MathHelper.floor(player.getY() - 0.2);
                double z = player.getZ() + Math.cos(i) * (7 - ticksFrostStompAnim * 1.5);

                BlockPos blockPos = new BlockPos((int)x, (int)y, (int)z);
                BlockState blockState = player.world.getBlockState(blockPos);
                ((ServerWorld) player.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), x,
                        y + 1, z, 4, 1, 0.0D, 1, 0.0D);

            }
            for(int j = 0; j < (5-ticksFrostStompAnim); j++) {
                if(Math.random() > 0.7) {
                    double randX = player.getX() + (Math.random() - 0.5) * 2 * (7 - ticksFrostStompAnim * 1.5);
                    double randY = MathHelper.floor(player.getY() - 0.2);
                    double randZ = player.getZ() + (Math.random() - 0.5) * 2 * (7 - ticksFrostStompAnim * 1.5);

                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.SNOWFLAKE, randX,
                            randY + 1, randZ, 1, 0.5, 0.0D, 0.5, 0.0D);
                }
            }
        }
    }
    @Override
    public void aabilities_setFireStompTimer(long ticksUntilFireStomp) {
        this.ticksUntilFireStomp = ticksUntilFireStomp;
    }
    public void aabilities_setFireStompAnimTimer(long ticks) {
        this.ticksFireStompAnim = ticks;
    }

    @Override
    public void aabilities_setFrostStompTimer(long ticksUntilFrostStomp) {
        this.ticksUntilFrostStomp = ticksUntilFrostStomp;
    }

    @Override
    public void aabilities_setFrostStompAnimTimer(long ticks) {
        this.ticksFrostStompAnim = ticks;
    }

    @Override
    public void aabilities_setTranscendTimer(long ticks) {
        this.ticksTranscend = ticks;
    }

    @Override
    public void aabilities_setHelmetCooldown(long ticks) {
        this.helmetCooldown = ticks;
    }

    @Override
    public void aabilities_setChestCooldown(long ticks) {
        this.chestCooldown = ticks;
    }

    @Override
    public void aabilities_setLeggingCooldown(long ticks) {
        this.leggingCooldown = ticks;
    }

    @Override
    public void aabilities_setBootCooldown(long ticks) {
        this.bootCooldown = ticks;
    }

    @Override
    public long aabilities_getHelmetCooldown() {
        return this.helmetCooldown;
    }

    @Override
    public long aabilities_getChestCooldown() {
        return this.chestCooldown;
    }

    @Override
    public long aabilities_getLeggingCooldown() {
        return this.leggingCooldown;
    }

    @Override
    public long aabilities_getBootCooldown() {
        return this.bootCooldown;
    }
}
