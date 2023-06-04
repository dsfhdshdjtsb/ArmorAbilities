package com.dsfhdshdjtsb.ArmorAbilities.networking;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
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

import java.util.List;
import java.util.UUID;

public class ModPackets {

    public static final Identifier BOOT_ABILITY_ID = new Identifier(ArmorAbilities.modid, "boot_ability");
    public static final Identifier LEGGING_ABILITY_ID = new Identifier(ArmorAbilities.modid, "legging_ability");
    public static final Identifier CHEST_ABILITY_ID = new Identifier(ArmorAbilities.modid, "chest_ability");
    public static final Identifier HELMET_ABILITY_ID = new Identifier(ArmorAbilities.modid, "helmet_ability");
    public static final Identifier VELOCITY_UPDATE_ID = new Identifier(ArmorAbilities.modid, "velocity_update");
    public static final Identifier TIMER_UPDATE_ID = new Identifier(ArmorAbilities.modid, "timer_update");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(HELMET_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                       PacketByteBuf buf, PacketSender responseSender) -> {
            String name = buf.readString();

            if(name.equals("focus"))
            {
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
            if(name.equals("mind_control"))
            {
                int level = buf.readInt();
                List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                        .expand(level + 5 , level + 5, level + 5));

                if(!(list.size() <= 1)) {
                    for (int i = 0; i < list.size(); i++) {
                        LivingEntity e = list.get(i);
                        if(e instanceof MobEntity && e.getMaxHealth() < player.getMaxHealth() * 2 && !e.hasStatusEffect(ArmorAbilities.MIND_CONTROL_COOLDOWN_EFFECT) )
                        {
                            if( i + 1 < list.size())
                            {
                                ((MobEntity) e).setTarget(list.get(i + 1));
                            }
                            else
                            {
                                ((MobEntity) e).setTarget(list.get(0));
                            }
//                            e.addStatusEffect(new StatusEffectInstance(ArmorAbilities.MIND_CONTROLLED_EFFECT, 160, 0));
                            e.addStatusEffect(new StatusEffectInstance(ArmorAbilities.MIND_CONTROL_COOLDOWN_EFFECT, 1200, 0, false, false));
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
            if(name.equals("telekinesis"))
            {
                int level = buf.readInt();
                List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                        .expand(level + 5, 2, level + 5));

                list.remove(player);
                for(LivingEntity e : list)
                {
                    float str = player.distanceTo(e) / 7;
                    e.takeKnockback(str, e.getX() - player.getX(), e.getZ() - player.getZ());
                    if(e instanceof PlayerEntity && !player.world.isClient)
                    {
                        PacketByteBuf newBuf = PacketByteBufs.create();
                        newBuf.writeDouble(e.getVelocity().x);
                        newBuf.writeDouble(e.getVelocity().y);
                        newBuf.writeDouble(e.getVelocity().z);
                        ServerPlayNetworking.send((ServerPlayerEntity) e, VELOCITY_UPDATE_ID, newBuf);
                    }
                    server.execute(() -> ((ServerWorld) player.world).spawnParticles(ParticleTypes.POOF, e.getX(), e.getBodyY(0.1D), e.getZ(), 5, 0.3, 0.5, 0.3, 0.0D));
                }
                server.execute(() -> ((ServerWorld) player.world).spawnParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getBodyY(1D) + 0.25, player.getZ(), 5, 0.1, 0.1, 0.1, 0.0D));
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
        });
        ServerPlayNetworking.registerGlobalReceiver(CHEST_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                         PacketByteBuf buf, PacketSender responseSender) -> {
            String name = buf.readString();
            TimerAccess timerAccess =  ((TimerAccess) player);

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

                server.execute(() -> ((ServerWorld) player.world).spawnParticles(ParticleTypes.ENTITY_EFFECT, player.getX(), player.getBodyY(0.5D), player.getZ(), 20, 0.7, 0.5, 0.7, 2.0D));

            }

            if(name.equals("explode"))
            {
                int ticks = 80;
                timerAccess.aabiliites_setFuse(ticks);
                player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                //                player.world.createExplosion(player, player.getX(), player.getY(), player.getZ(), 2.0f, World.ExplosionSourceType.NONE);

                PacketByteBuf newBuf = PacketByteBufs.create();
                newBuf.writeString("explode");
                newBuf.writeInt(ticks);
                newBuf.writeString(player.getUuidAsString());
                newBuf.writeBoolean(false);

                for (ServerPlayerEntity player1 : PlayerLookup.tracking((ServerWorld) player.world, player.getBlockPos())) {
                    ServerPlayNetworking.send(player1, TIMER_UPDATE_ID, newBuf);
                }
            }

            if(name.equals("siphon"))
            {
                int level = buf.readInt();
                List<LivingEntity> list = player.world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox()
                        .expand(3 + level, 1.0D, 3 + level));
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
                player.world.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ENTITY_ARROW_HIT_PLAYER,
                        SoundCategory.PLAYERS,
                        1f,
                        1f
                );
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(LEGGING_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                PacketByteBuf buf, PacketSender responseSender) -> {

            String name = buf.readString();

            if(name.equals("dash")) {

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
                    case 2, 3 -> speedLevel = 1;
                    case 4 -> speedLevel = 2;
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

        });

        ServerPlayNetworking.registerGlobalReceiver(BOOT_ABILITY_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                                                      PacketByteBuf buf, PacketSender responseSender) -> {

            String name = buf.readString();
            TimerAccess timerAccess =  ((TimerAccess) player);



            if(name.equals("fire_stomp"))
            {
                if(player.isOnGround())
                    player.jump();
                timerAccess.aabilities_setFireStompTimer(200);

            }
            if(name.equals("frost_stomp"))
            {
                if(player.isOnGround())
                    player.jump();
                timerAccess.aabilities_setFrostStompTimer(200);

            }
            if(name.equals("anvil_stomp"))
            {
                int ticks = 200;
                if(player.isOnGround())
                    player.jump();
                timerAccess.aabilities_setAnvilStompTimer(ticks);
                timerAccess.aabilities_setShouldAnvilRender(true);
                player.world.playSound(
                        null,
                        new BlockPos((int)player.getX(), (int)player.getY(), (int)player.getZ()),
                        SoundEvents.BLOCK_COPPER_PLACE,
                        SoundCategory.PLAYERS,
                        1f,
                        1f
                );
                PacketByteBuf newBuf = PacketByteBufs.create();
                newBuf.writeString("anvil_stomp");
                newBuf.writeInt(ticks);
                newBuf.writeString(player.getUuidAsString());
                newBuf.writeBoolean(true);

                for (ServerPlayerEntity player1 : PlayerLookup.tracking((ServerWorld) player.world, player.getBlockPos())) {
                    ServerPlayNetworking.send(player1, TIMER_UPDATE_ID, newBuf);
                }
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
        });
        ClientPlayNetworking.registerGlobalReceiver(TIMER_UPDATE_ID, (client, handler, buf, responseSender) -> {
            String name = buf.readString();
            int ticks = buf.readInt();
            String uuid = buf.readString();
            boolean shouldRenderAnvil = buf.readBoolean();

            if(name.equals("explode"))
            {
                if(client.world != null)
                {
                    TimerAccess timerAccess = (TimerAccess) client.world.getPlayerByUuid((UUID.fromString(uuid)));
                    if(timerAccess != null)
                        timerAccess.aabiliites_setFuse(ticks);
                }
            }
            else if(name.equals("anvil_stomp"))
            {
                if(client.world != null)
                {
                    TimerAccess timerAccess = (TimerAccess) client.world.getPlayerByUuid((UUID.fromString(uuid)));
                    if(timerAccess != null)
                    {
                        timerAccess.aabilities_setShouldAnvilRender(shouldRenderAnvil);

                    }

                }
            }
        });
    }

}
