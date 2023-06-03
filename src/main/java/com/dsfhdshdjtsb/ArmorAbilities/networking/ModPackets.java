package com.dsfhdshdjtsb.ArmorAbilities.networking;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ModPackets {

    public static final Identifier BOOT_ABILITY_ID = new Identifier(ArmorAbilities.modid, "boot_ability");
    public static final Identifier LEGGING_ABILITY_ID = new Identifier(ArmorAbilities.modid, "legging_ability");
    public static final Identifier CHEST_ABILITY_ID = new Identifier(ArmorAbilities.modid, "chest_ability");
    public static final Identifier HELMET_ABILITY_ID = new Identifier(ArmorAbilities.modid, "helmet_ability");
    public static final Identifier VELOCITY_UPDATE_ID = new Identifier(ArmorAbilities.modid, "velocity_update");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(HELMET_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                       PacketByteBuf buf, PacketSender responseSender) -> {
            String name = buf.readString();
            TimerAccess timerAccess =  ((TimerAccess) player);

            if(name.equals("focus"))
            {
                System.out.println("focus");
            }
            if(name.equals("pulverize"))
            {
                timerAccess.aabilities_setPulverizeTimer(2);
            }
            if(name.equals("telekinesis"))
            {
                List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                        .expand(7, 7, 7));
                list.remove(player);

                if(!(list.size() <= 1)) {
                    for (int i = 0; i < list.size(); i++) {
                        LivingEntity e = list.get(i);
                        if(e instanceof MobEntity )
                        {
                            System.out.println(((MobEntity) e).getTarget());
                            if( i + 1 < list.size())
                            {
                                ((MobEntity) e).setTarget(list.get(i + 1));
                            }
                            else
                            {
                                ((MobEntity) e).setTarget(list.get(0));
                            }
//                            e.addStatusEffect(new StatusEffectInstance(ArmorAbilities.MIND_CONTROLLED_EFFECT, 160, 0));

                            double xdif = e.getX() - player.getX();
                            double ydif = e.getBodyY(0.5D) - player.getBodyY(0.5D);
                            double zdif = e.getZ() - player.getZ();

                            int particleNumConstant = 20; //number of particles
                            double x = 0;
                            double y = 0;
                            double z = 0;
                            while(Math.abs(x) < Math.abs(xdif))
                            {
                                ((ServerWorld) player.world).spawnParticles(ParticleTypes.ELECTRIC_SPARK, player.getX() + x,
                                        player.getBodyY(1D) + y, player.getZ() + z, 0, 1, 0.0D, 1, 0.0D);
                                x = x + xdif/particleNumConstant;
                                y = y + ydif/particleNumConstant;
                                z = z + zdif/particleNumConstant;
                            }
                        }
                    }

                    player.world.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL,
                            SoundCategory.PLAYERS,
                            1f,
                            1f
                    );

                }
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(CHEST_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                         PacketByteBuf buf, PacketSender responseSender) -> {
            String name = buf.readString();
            TimerAccess timerAccess =  ((TimerAccess) player);
            if(name.equals("transcend"))
            {
                player.addVelocity(new Vec3d(0, 1.5, 0));
                timerAccess.aabilities_setTranscendTimer(200);
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
                timerAccess.aabiliites_setFuse(80);
                player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                //                player.world.createExplosion(player, player.getX(), player.getY(), player.getZ(), 2.0f, World.ExplosionSourceType.NONE);
            }

            if(name.equals("siphon"))
            {
                int level = buf.readInt();
                List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                        .expand(7, 1.0D, 7));
                int counter = 0;

                list.remove(player);
                for (LivingEntity e : list) {

                    counter++;
                    e.damage(player.world.getDamageSources().magic(), 1.0f);

                    if (player.world instanceof ServerWorld) {
                        double xdif = e.getX() - player.getX();
                        double ydif = e.getBodyY(0.5D) - player.getBodyY(0.5D);
                        double zdif = e.getZ() - player.getZ();

                        int particleNumConstant = 20; //number of particles
                        double x = 0;
                        double y = 0;
                        double z = 0;
                        while(Math.abs(x) < Math.abs(xdif))
                        {
                            ((ServerWorld) player.world).spawnParticles(ParticleTypes.COMPOSTER, player.getX() + x,
                                    player.getBodyY(0.5D) + y, player.getZ() + z, 0, 1, 0.0D, 1, 0.0D);
                            x = x + xdif/particleNumConstant;
                            y = y + ydif/particleNumConstant;
                            z = z + zdif/particleNumConstant;
                        }
                        ((ServerWorld) player.world).spawnParticles(ParticleTypes.HEART, player.getX(), player.getBodyY(0.5D), player.getZ(), 2, 0.4, 0.5, 0.4, 0.0D);

                    }
                }
                    player.heal(counter + (level - 1));
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(LEGGING_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                PacketByteBuf buf, PacketSender responseSender) -> {

            String name = buf.readString();
            TimerAccess timerAccess =  ((TimerAccess) player);

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

            if(name.equals("rush"))
            {
                int rushLevel = buf.readInt();
                int speedLevel = 0;
                int strengthLevel = 0;

                switch (rushLevel) {
                    case 2, 3 -> {
                        speedLevel = 1;
                    }
                    case 4 -> {
                        speedLevel = 2;
                    }
                    case 5 -> {
                        speedLevel = 2;
                        strengthLevel = 1;
                    }
                }
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, speedLevel));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, strengthLevel));

                player.world.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS,
                        SoundCategory.PLAYERS,
                        1.5f,
                        1f
                );
                if(player.world instanceof ServerWorld)
                {
                    ((ServerWorld) player.world).spawnParticles(ParticleTypes.ANGRY_VILLAGER, player.getX(), player.getBodyY(0.5D), player.getZ(), 3, 0.4, 0.5, 0.4, 0.0D);
                }
            }

            if(name.equals("dodge"))
            {

                player.addStatusEffect(new StatusEffectInstance(ArmorAbilities.DODGE_EFFECT, 40, 0));
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(BOOT_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                      PacketByteBuf buf, PacketSender responseSender) -> {

            String name = buf.readString();
            TimerAccess timerAccess =  ((TimerAccess) player);



            if(name.equals("fire_stomp"))
            {
                if(player.isOnGround())
                    player.jump();
                timerAccess.aabilities_setFireStompTimer(100);
//                if(player.isOnGround())
//                    player.addVelocity(new Vec3d(0, 0.5, 0));

            }
            if(name.equals("frost_stomp"))
            {
                if(player.isOnGround())
                    player.jump();
                timerAccess.aabilities_setFrostStompTimer(100);
//                if(player.isOnGround())
//                    player.addVelocity(new Vec3d(0, 0.5, 0));

            }
            if(name.equals("anvil_stomp"))
            {
                if(player.isOnGround())
                    player.jump();
                timerAccess.aabilities_setAnvilStompTimer(200);
                player.world.playSound(
                        null,
                        new BlockPos((int)player.getX(), (int)player.getY(), (int)player.getZ()),
                        SoundEvents.BLOCK_COPPER_PLACE,
                        SoundCategory.PLAYERS,
                        1f,
                        1f
                );

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
