package com.dsfhdshdjtsb.ArmorAbilities.networking;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
public class ModPackets {

    public static final Identifier BOOT_ABILITY_ID = new Identifier(ArmorAbilities.modid, "boot_ability");
    public static final Identifier LEGGING_ABILITY_ID = new Identifier(ArmorAbilities.modid, "legging_ability");
    public static final Identifier VELOCITY_UPDATE_ID = new Identifier(ArmorAbilities.modid, "velocity_update");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(BOOT_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                PacketByteBuf buf, PacketSender responseSender) -> {

            double velY = buf.readDouble();
            double velX = buf.readDouble();
            double velZ = buf.readDouble();
            server.execute(() -> {
                player.setVelocity(velX, velY, velZ);
                ((ServerWorld) player.world).spawnParticles(ParticleTypes.POOF, player.getX(), player.getBodyY(0.5D), player.getZ(), 5, 0.3, 0.5, 0.3, 0.0D);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(LEGGING_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                      PacketByteBuf buf, PacketSender responseSender) -> {

            double posY = buf.readDouble();
            double posX = buf.readDouble();
            double posZ = buf.readDouble();

            double velY = buf.readDouble();
            double velX = buf.readDouble();
            double velZ = buf.readDouble();
            server.execute(() -> {
                ((ServerWorld) player.world).spawnParticles(ParticleTypes.POOF, player.getX(), player.getBodyY(0.5D), player.getZ(), 5, 0.3, 0.5, 0.3, 0.0D);
                player.setVelocity(velX, velY, velZ);
                player.setPos(posX, posY, posZ);
            });
        });

    }

    public static void registerS2CPackets(){

    }
}
