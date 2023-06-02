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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
    private long ticksPulverize;

    public long helmetCooldown = 0;
    public long chestCooldown = 0;
    public long leggingCooldown = 0;
    public long bootCooldown = 0;

    private long fuse = 0;

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
        PlayerEntity player = (PlayerEntity) ((Object)this);
        helmetCooldown--;
        chestCooldown--;
        leggingCooldown--;
        bootCooldown--;
        fuse-=2;

        if(fuse >= 0 )
        {

            if(fuse == 0)
            {
                float explodeLevel = 2.5f;
                for (ItemStack i : player.getArmorItems()) {
                    explodeLevel += 0.5 * EnchantmentHelper.getLevel(ArmorAbilities.EXPLODE, i);
                }
                if(!player.world.isClient())
                    player.world.createExplosion(player, player.getX(), player.getBodyY(0.0625D), player.getZ(), explodeLevel, World.ExplosionSourceType.NONE);
            }
            if(player.isOnGround())
            {
                player.slowMovement(player.getBlockStateAtPos(), new Vec3d(0.001,0.001,0.001));
            }
        }


        if(--this.ticksPulverize >= 0L)
        {
            List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                    .expand(7, 7, 7));

            double playerX = player.getX();
            double playerY = player.getY();
            double playerZ = player.getZ();
            double playerYaw = player.getYaw();
            double playerPitch = player.getPitch();


            double maxPitchRads = Math.min(Math.PI / 2, (playerPitch + 45) * Math.PI / 180);
            double minPitchRads = Math.max(Math.PI / -2, (playerPitch - 45) * Math.PI / 180);
            double length = 7;

            double maxY = Math.min(playerY, -Math.sin(maxPitchRads) * length + playerY);
            double minY = Math.max(playerY, -Math.sin(minPitchRads) * length + playerY);
            double mult = Math.cos(playerPitch) * length + 1;

            double maxRads = ((playerYaw - 45));
            if(maxRads > 180)
            {
                maxRads = -180 + maxRads % 180;
            }
            double minRads = ((playerYaw + 45));
            if(minRads < -180)
            {
                minRads = 180 + minRads % 180;
            }

            double maxX = Math.max(playerX, Math.sin(maxRads) * mult + playerX);
            double minX = Math.min(playerX, Math.sin(minRads) * mult + playerX);

            double maxZ = Math.max(playerZ, Math.cos(maxRads) * mult + playerZ);
            double minZ = Math.min(playerZ, Math.cos(minRads) * mult + playerZ);

            System.out.println("pitch: " + playerPitch);
            System.out.println("Max pitch: " + maxPitchRads);
            System.out.println("Min pitch: " + minPitchRads);
            System.out.println("mult: " + mult);
            System.out.println("yaw: " + playerYaw);
            System.out.println("max yaw: " + maxRads);
            System.out.println("min yaw: " + minRads);
            System.out.println("maxY: " + maxY);
            System.out.println("minY: " + minY);
            System.out.println("maxX: " + maxX);
            System.out.println("minX: " + minX);
            System.out.println("maxZ: " + maxZ);
            System.out.println("minZ: " + minZ);
            System.out.println(-Math.sin(maxRads) * mult + playerX);
            System.out.println(-Math.sin(minRads) * mult + playerX);
            System.out.println("sin: " + -Math.sin(maxRads));

//            for(LivingEntity e: list )
//            {
//                double posX = e.getX();
//            double posY = e.getY();
//            double posZ = e.getZ();
//            }
        }
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
            int fireStompLevel = 0;
            for (ItemStack i : player.getArmorItems()) {
                fireStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.FIRE_STOMP, i);
            }

            List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                    .expand(7, 2, 7));

            list.remove(player);
            if(!list.isEmpty()) {
                for (LivingEntity e : list) {
                    e.setFireTicks(60);
                    e.damage(player.world.getDamageSources().magic(), 2 + fireStompLevel);
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
                if(Math.random() < .10)
                {
                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.FLAME, x,
                            y + 1, z, 4, 1, 0.0D, 1, 0.0D);
                }
                else
                {
                    ((ServerWorld) player.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), x,
                            y + 1, z, 4, 1, 0.0D, 1, 0.0D);
                }


            }
//            for(int j = 0; j < (5-ticksFireStompAnim); j++) {
//                if(Math.random() > 0.7) {
//                    double randX = player.getX() + (Math.random() - 0.5) * 2 * (7 - ticksFireStompAnim * 1.5);
//                    double randY = MathHelper.floor(player.getY() - 0.2);
//                    double randZ = player.getZ() + (Math.random() - 0.5) * 2 * (7 - ticksFireStompAnim * 1.5);
//
//                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.FLAME, randX,
//                            randY + 1, randZ, 1, 0.5, 0.0D, 0.5, 0.0D);
//                }
//            }
        }

        if(--this.ticksUntilFrostStomp >= 0L && (player).isOnGround())
        {
            this.ticksUntilFrostStomp = 0;
            int frostStompLevel = 0;
            for (ItemStack i : player.getArmorItems()) {
                frostStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.FROST_STOMP, i);
            }

            System.out.println("stomp");
            List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                    .expand(7, 2, 7));

            list.remove(player);
            if(!list.isEmpty()) {
                for (LivingEntity e : list) {
                    e.setFrozenTicks(140 + frostStompLevel * 80);
                    int amp = 0;
                    if(frostStompLevel >= 4)
                        amp ++;
                    e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, amp));
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
                if(Math.random() < .10)
                {
                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.SNOWFLAKE, x,
                            y + 1, z, 4, 1, 0.0D, 1, 0.0D);
                }
                else
                {
                    ((ServerWorld) player.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), x,
                            y + 1, z, 4, 1, 0.0D, 1, 0.0D);
                }
            }
//            for(int j = 0; j < (5-ticksFrostStompAnim); j++) {
//                if(Math.random() > 0.7) {
//                    double randX = player.getX() + (Math.random() - 0.5) * 2 * (7 - ticksFrostStompAnim * 1.5);
//                    double randY = MathHelper.floor(player.getY() - 0.2);
//                    double randZ = player.getZ() + (Math.random() - 0.5) * 2 * (7 - ticksFrostStompAnim * 1.5);
//
//                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.SNOWFLAKE, randX,
//                            randY + 1, randZ, 1, 0.5, 0.0D, 0.5, 0.0D);
//                }
//            }
        }
    }

    @Override
    public void aabilities_setPulverizeTimer(long ticks) {
        this.ticksPulverize = ticks;
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
    public void aabiliites_setFuse(long ticks) {
        this.fuse = ticks;
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

    @Override
    public long aabilities_getFuse() {
        return fuse;
    }
}
