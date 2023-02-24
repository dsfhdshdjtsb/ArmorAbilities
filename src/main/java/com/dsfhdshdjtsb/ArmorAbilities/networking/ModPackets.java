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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ModPackets {

    public static final Identifier BOOT_ABILITY_ID = new Identifier(ArmorAbilities.modid, "boot_ability");
    public static final Identifier LEGGING_ABILITY_ID = new Identifier(ArmorAbilities.modid, "legging_ability");
    public static final Identifier VELOCITY_UPDATE_ID = new Identifier(ArmorAbilities.modid, "velocity_update");

    public static void registerC2SPackets(){
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
                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.POOF, player.getX(), player.getBodyY(0.5D), player.getZ(), 5, 0.3, 0.5, 0.3, 0.0D);
                    player.setVelocity(velX, velY, velZ);
                    player.setPos(posX, posY, posZ);
                });
            }

            if(name.equals("fire_stomp"))
            {
                ((TimerAccess) player).aabilites_setTimer(100);
//                if(player.isOnGround())
//                    player.addVelocity(new Vec3d(0, 0.5, 0));
            }
        });

    }

    public static void registerS2CPackets(){

    }
}
