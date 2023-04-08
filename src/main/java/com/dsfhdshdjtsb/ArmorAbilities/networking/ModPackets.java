package com.dsfhdshdjtsb.ArmorAbilities.networking;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class ModPackets {

    public static final Identifier BOOT_ABILITY_ID = new Identifier(ArmorAbilities.modid, "boot_ability");
    public static final Identifier LEGGING_ABILITY_ID = new Identifier(ArmorAbilities.modid, "legging_ability");
    public static final Identifier CHEST_ABILITY_ID = new Identifier(ArmorAbilities.modid, "chest_ability");
    public static final Identifier HELMET_ABILITY_ID = new Identifier(ArmorAbilities.modid, "helmet_ability");
    public static final Identifier VELOCITY_UPDATE_ID = new Identifier(ArmorAbilities.modid, "velocity_update");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(CHEST_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                         PacketByteBuf buf, PacketSender responseSender) -> {
            String name = buf.readString();

            if(name.equals("transcend"))
            {
                player.addVelocity(new Vec3d(0, 1.5, 0));

                ((TimerAccess) player).aabilities_setTranscendTimer(200);
            }

            if(name.equals("cleanse"))
            {
                player.clearStatusEffects();
                player.setFireTicks(0);
                player.setFrozenTicks(0);

                player.world.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                        SoundCategory.PLAYERS,
                        1.5f,
                        1f
                );

                server.execute(() -> {
                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.ENTITY_EFFECT, player.getX(), player.getBodyY(0.5D), player.getZ(), 20, 0.7, 0.5, 0.7, 2.0D);
                });

            }

            if(name.equals("explode"))
            {
                player.world.createExplosion(player, player.getX(), player.getY(), player.getZ(), 2.0f, World.ExplosionSourceType.NONE);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(LEGGING_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                PacketByteBuf buf, PacketSender responseSender) -> {

            String name = buf.readString();

            if(name.equals("dash")) {
                System.out.println("dash");
                double velY = buf.readDouble();
                double velX = buf.readDouble();
                double velZ = buf.readDouble();
                server.execute(() -> {
                    player.setVelocity(velX, velY, velZ);
                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.POOF, player.getX(), player.getBodyY(0.5D), player.getZ(), 5, 0.3, 0.5, 0.3, 0.0D);
                });
                player.world.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_PHANTOM_FLAP,
                        SoundCategory.PLAYERS,
                        1.5f,
                        1f
                );

            }
            if(name.equals("rush"))
            {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 2));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 0));

                player.world.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL,
                        SoundCategory.PLAYERS,
                        1.5f,
                        1f
                );
            }
            if(name.equals("dodge"))
            {
                player.addStatusEffect(new StatusEffectInstance(ArmorAbilities.DODGE_EFFECT, 40, 0));
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(BOOT_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                      PacketByteBuf buf, PacketSender responseSender) -> {

            String name = buf.readString();

            if(name.equals("blink")) {
                System.out.println("blink");
                double posY = buf.readDouble();
                double posX = buf.readDouble();
                double posZ = buf.readDouble();

                double velY = buf.readDouble();
                double velX = buf.readDouble();
                double velZ = buf.readDouble();

                server.execute(() -> {
                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getBodyY(0.5D), player.getZ(), 15, 0.3, 0.5, 0.3, 0.0D);
                    player.setVelocity(velX, velY, velZ);
                    player.setPos(posX, posY, posZ);
                });

                player.world.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                        SoundCategory.PLAYERS,
                        0.8f,
                        1f
                );
            }

            if(name.equals("fire_stomp"))
            {
                ((TimerAccess) player).aabilities_setFireStompTimer(100);
//                if(player.isOnGround())
//                    player.addVelocity(new Vec3d(0, 0.5, 0));
            }
            if(name.equals("frost_stomp"))
            {
                ((TimerAccess) player).aabilities_setFrostStompTimer(100);
//                if(player.isOnGround())
//                    player.addVelocity(new Vec3d(0, 0.5, 0));
            }
        });

    }

    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(VELOCITY_UPDATE_ID, (client, handler, buf, responseSender) -> {
            double velX = buf.readDouble();
            double velY = buf.readDouble();
            double velZ = buf.readDouble();
            if(client.player != null)
                client.player.setVelocity(velX, velY, velZ);
            System.out.println("vel update recieved");
        });
    }
}
