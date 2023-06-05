package com.dsfhdshdjtsb.ArmorAbilities.mixin;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public  class AabilitiesPlayerEntityMixin implements TimerAccess {

    @Unique
    private long ticksUntilFireStomp;
    private long ticksFireStompAnim = -1;
    private long ticksUntilFrostStomp;
    private long ticksFrostStompAnim = -1;
    private long ticksAnvilStomp = -5;
    private long ticksAnvilStompAnim = 0;
    public long helmetCooldown = 0;
    public long chestCooldown = 0;
    public long leggingCooldown = 0;
    public long bootCooldown = 0;
    public boolean shouldRenderAnvil = false;

    private long fuse = 0;

    @Inject(at = @At("HEAD"), method = "damage",  cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) ((Object)this);
        if(((TimerAccess) player).aabilities_getAnvilStompTimer() > -5)
        {
            cir.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "attack", cancellable = true)
    private void attack(Entity target, CallbackInfo ci)
    {
        TimerAccess timerAccess = (this);
        if(timerAccess.aabilities_getAnvilStompTimer() > -5 || timerAccess.aabilities_getFuse() > 0)
        {
            ci.cancel();
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
                float explodeLevel = 0;
                for (ItemStack i : player.getArmorItems()) {
                    explodeLevel += EnchantmentHelper.getLevel(ArmorAbilities.EXPLODE, i);
                }
                if(!player.world.isClient())
                    player.world.createExplosion(player, player.getX(), player.getBodyY(0.0625D), player.getZ(), 1.5f + 0.5f * explodeLevel, Explosion.DestructionType.NONE);
            }
            if(player.isOnGround())
            {
                player.slowMovement(player.getBlockStateAtPos(), new Vec3d(0.001,0.001,0.001));
            }
        }

        if(--this.ticksAnvilStomp >= 0L && player.isOnGround())
        {
            this.ticksAnvilStomp = 0;

            int anvilStompLevel = 0;
            for (ItemStack i : player.getArmorItems()) {
                anvilStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.ANVIL_STOMP, i);
            }

            if(!player.world.isClient) {
                List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                        .expand(7, 1, 7));


                list.remove(player);
                if (!list.isEmpty()) {
                    for (LivingEntity e : list) {
                        double x = 0, z = 0;
                        double y = 0.8 + anvilStompLevel * .1;
                        if (e instanceof PlayerEntity) {
                            PacketByteBuf newBuf = PacketByteBufs.create();
                            newBuf.writeDouble(x);
                            newBuf.writeDouble(y);
                            newBuf.writeDouble(z);
                            ServerPlayNetworking.send((ServerPlayerEntity) e, ModPackets.VELOCITY_UPDATE_ID, newBuf);
                        }
                        e.setVelocity(x, y, z);
                    }
                }
                player.world.playSound(
                        null,
                        new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()),
                        SoundEvents.BLOCK_ANVIL_LAND,
                        SoundCategory.PLAYERS,
                        1f,
                        1f
                );
                ((TimerAccess) player).aabilities_setAnvilStompAnimTimer(5);
            }
        }
        else if(ticksAnvilStomp >= -5)
        {
            if(player.isOnGround())
            {
                player.slowMovement(player.getBlockStateAtPos(), new Vec3d(0.001,0.001,0.001));
            }
            else if(player.isTouchingWater())
            {
                ticksAnvilStomp = Math.max(ticksAnvilStomp - 2, -5);
            }
            if(ticksAnvilStomp == -5 && !player.world.isClient)
            {
                ((TimerAccess)player).aabilities_setShouldAnvilRender(false);
                PacketByteBuf newBuf = PacketByteBufs.create();
                newBuf.writeString("anvil_stomp");
                newBuf.writeInt(0);
                newBuf.writeString(player.getUuidAsString());
                newBuf.writeBoolean(false);

                for (ServerPlayerEntity player1 : PlayerLookup.tracking((ServerWorld) player.world, player.getBlockPos())) {
                    ServerPlayNetworking.send(player1, ModPackets.TIMER_UPDATE_ID, newBuf);
                }
            }
        }

        if(--ticksAnvilStompAnim >= 0 && !player.world.isClient) {
            for (double i = 0; i <= Math.PI * 2; i += Math.PI / 6) {

                double x = player.getX() + Math.sin(i) * (7 - ticksAnvilStompAnim * 1.5);
                double y = MathHelper.floor(player.getY() - 0.2);
                double z = player.getZ() + Math.cos(i) * (7 - ticksAnvilStompAnim * 1.5);

                BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
                BlockState blockState = player.world.getBlockState(blockPos);
                ((ServerWorld) player.world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), x,
                        y + 1, z, 4, 1, 0.0D, 1, 0.0D);

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
                    .expand(7, 1, 7));

            list.remove(player);
            if(!list.isEmpty()) {
                for (LivingEntity e : list) {
                    e.setFireTicks(20 * fireStompLevel);
                    e.damage(DamageSource.MAGIC, 2 + fireStompLevel);
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
        if(--ticksFireStompAnim >= 0 && !player.world.isClient)
        {
            for (double i = 0; i <= Math.PI * 2; i += Math.PI / 6) {

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

        }

        if(--this.ticksUntilFrostStomp >= 0L)
        {

            if(player.isOnGround()) {
                this.ticksUntilFrostStomp = 0;
                int frostStompLevel = 0;
                for (ItemStack i : player.getArmorItems()) {
                    frostStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.FROST_STOMP, i);
                }

                List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                        .expand(7, 1, 7));

                list.remove(player);
                if (!list.isEmpty()) {
                    for (LivingEntity e : list) {
                        e.setFrozenTicks(140 + frostStompLevel * 80);
                        int amp = 0;
                        if (frostStompLevel >= 4)
                            amp++;
                        e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, amp));
                        ((ServerWorld) player.world).spawnParticles(ParticleTypes.SNOWFLAKE, e.getX(),
                                e.getBodyY(0.5D) - 1, e.getZ(), 10, 0.5, 0.0D, 0.5, 0.0D);
                    }
                }
                player.world.playSound(
                        null,
                        new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()),
                        SoundEvents.ENTITY_GENERIC_SMALL_FALL,
                        SoundCategory.PLAYERS,
                        1f,
                        1f
                );
                ((TimerAccess) player).aabilities_setFrostStompAnimTimer(5);
            }
        }


        if(--ticksFrostStompAnim >= 0 && !player.world.isClient)
        {
            for (double i = 0; i <= Math.PI * 2; i += Math.PI / 6) {
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
        }


    }


    @Override
    public void aabilities_setFireStompTimer(long ticksUntilFireStomp) {
        this.ticksUntilFireStomp = ticksUntilFireStomp;
    }
    @Override
    public void aabilities_setFireStompAnimTimer(long ticks) {
        this.ticksFireStompAnim = ticks;
    }
    @Override
    public void aabilities_setAnvilStompAnimTimer(long ticks) {
        this.ticksAnvilStompAnim = ticks;
    }

    @Override
    public void aabilities_setShouldAnvilRender(boolean bool) {
        this.shouldRenderAnvil = bool;
    }

    @Override
    public boolean aabilities_getShouldAnvilRender() {
        return shouldRenderAnvil;
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
    public void aabiliites_setFuse(long ticks) {
        this.fuse = ticks;
    }

    @Override
    public void aabilities_setAnvilStompTimer(long ticks) {
        this.ticksAnvilStomp = ticks;
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

    @Override
    public long aabilities_getAnvilStompTimer() {
        return this.ticksAnvilStomp;
    }
}
