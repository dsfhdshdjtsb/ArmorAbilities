package com.dsfhdshdjtsb.ArmorAbilities.event;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyInputHandler {
    public static KeyBinding KEY_BOOT_ABILITY;
    public static KeyBinding KEY_LEGGING_ABILITY;
    public static KeyBinding KEY_CHEST_ABILITY;
    public static KeyBinding KEY_HELMET_ABILITY;

    public static void register(){
        KEY_HELMET_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aabilities.helmet_ability", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_Z, // The keycode of the key
                "category.aabilities.keybindings" // The translation key of the keybinding's category.
        ));
        KEY_CHEST_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aabilities.chest_ability", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_X, // The keycode of the key
                "category.aabilities.keybindings" // The translation key of the keybinding's category.
        ));
        KEY_LEGGING_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aabilities.legging_ability", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.aabilities.keybindings" // The translation key of the keybinding's category.
        ));
        KEY_BOOT_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aabilities.boot_ability", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_V, // The keycode of the key
                "category.aabilities.keybindings" // The translation key of the keybinding's category.
        ));



        regKeyInputs();
    }

    public static void regKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            while(KEY_HELMET_ABILITY.wasPressed()) {
                if (client.player != null) {
                    TimerAccess timerAccess =  ((TimerAccess) client.player);
                    if(timerAccess.aabilities_getHelmetCooldown() <= 0) {
                        timerAccess.aabilities_setHelmetCooldown(200);
                        int cooldown = 0;
                        int mindControlLevel = 0;
                        int focusLevel = 0;
                        int telekinesisLevel = 0;

                        for (ItemStack i : client.player.getArmorItems()) {
                            mindControlLevel += EnchantmentHelper.getLevel(ArmorAbilities.MIND_CONTROL, i);
                            focusLevel += EnchantmentHelper.getLevel(ArmorAbilities.FOCUS, i);
                            telekinesisLevel += EnchantmentHelper.getLevel(ArmorAbilities.TELEKINESIS, i);
                        }
                        if(focusLevel > 0)
                        {
                            cooldown = 1100 - focusLevel * 100;
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("focus");
                            ClientPlayNetworking.send(ModPackets.HELMET_ABILITY_ID, buf);

                            timerAccess.aabilities_setChestCooldown(0);
                            timerAccess.aabilities_setLeggingCooldown(0);
                            timerAccess.aabilities_setBootCooldown(0);
                        }
                        if(mindControlLevel > 0)
                        {
                            List<MobEntity> list = client.player.getWorld().getNonSpectatingEntities(MobEntity.class, client.player.getBoundingBox()
                                    .expand(mindControlLevel + 5, mindControlLevel + 5, mindControlLevel + 5));
                            if(list.size() > 0) {
                                cooldown = 600 - mindControlLevel * 40;
                                PacketByteBuf buf = PacketByteBufs.create();
                                buf.writeString("mind_control");
                                buf.writeInt(mindControlLevel);
                                ClientPlayNetworking.send(ModPackets.HELMET_ABILITY_ID, buf);
                            }
                        }
                        if(telekinesisLevel > 0)
                        {
                            List<LivingEntity> list = client.player.getWorld().getNonSpectatingEntities(LivingEntity.class, client.player.getBoundingBox()
                                    .expand(telekinesisLevel + 5, 2, telekinesisLevel + 5));

                            System.out.println(list.size());
                            if(list.size() > 1) {
                                cooldown = 300 - telekinesisLevel * 20;
                                PacketByteBuf buf = PacketByteBufs.create();
                                buf.writeString("telekinesis");
                                buf.writeInt(telekinesisLevel);
                                ClientPlayNetworking.send(ModPackets.HELMET_ABILITY_ID, buf);
                            }
                        }
                        timerAccess.aabilities_setHelmetCooldown(cooldown);
                    }
                }
            }
        }));

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            while(KEY_CHEST_ABILITY.wasPressed()){
                if(client.player != null) {
                    TimerAccess timerAccess =  ((TimerAccess) client.player);
                    if(timerAccess.aabilities_getChestCooldown() <= 0) {


                        int cleanseLevel = 0;
                        int explodeLevel = 0;
                        int siphonLevel = 0;
                        int cooldown = 0;
                        for (ItemStack i : client.player.getArmorItems()) {
                            cleanseLevel += EnchantmentHelper.getLevel(ArmorAbilities.CLEANSE, i);
                            explodeLevel += EnchantmentHelper.getLevel(ArmorAbilities.EXPLODE, i);
                            siphonLevel += EnchantmentHelper.getLevel(ArmorAbilities.SIPHON, i);
                        }

                        if (cleanseLevel > 0) {
                            cooldown = 400 - cleanseLevel * 40;
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("cleanse");
                            ClientPlayNetworking.send(ModPackets.CHEST_ABILITY_ID, buf);

                        }

                        if (explodeLevel > 0) {
                            if(timerAccess.aabilities_getAnvilStompTimer() < 0) {
                                cooldown = 400 - explodeLevel * 20;
                                PacketByteBuf buf = PacketByteBufs.create();
                                timerAccess.aabiliites_setFuse(80);
                                buf.writeString("explode");
                                ClientPlayNetworking.send(ModPackets.CHEST_ABILITY_ID, buf);
                            }
                        }
                        if (siphonLevel > 0){
                            List<LivingEntity> list = client.player.getWorld().getNonSpectatingEntities(LivingEntity.class, client.player.getBoundingBox()
                                    .expand(siphonLevel + 3, 1.0D, siphonLevel + 3));
                            if(list.size() > 1) {
                                cooldown = 400 - siphonLevel * 20;
                                PacketByteBuf buf = PacketByteBufs.create();
                                buf.writeString("siphon");
                                buf.writeInt(siphonLevel);
                                ClientPlayNetworking.send(ModPackets.CHEST_ABILITY_ID, buf);
                            }
                        }
                        timerAccess.aabilities_setChestCooldown(cooldown);

                    }
                }
            }
        }));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (KEY_LEGGING_ABILITY.wasPressed()) {
                if(client.player != null){
                    TimerAccess timerAccess = ((TimerAccess) client.player);
                    if(timerAccess.aabilities_getLeggingCooldown() <= 0) {
                        int cooldown = 0;
                        int dashLevel = 0;
                        int rushLevel = 0;
                        int blinkLevel = 0;

                        for (ItemStack i : client.player.getArmorItems()) {
                            blinkLevel += EnchantmentHelper.getLevel(ArmorAbilities.BLINK, i);
                            dashLevel += EnchantmentHelper.getLevel(ArmorAbilities.DASH, i);
                            rushLevel += EnchantmentHelper.getLevel(ArmorAbilities.RUSH, i);
                        }

                        if (dashLevel > 0) {
                            cooldown = 300 - dashLevel * 20;
                            double distanceMult = .80 + dashLevel * .1;

                            double pitch = client.player.getPitch() * Math.PI / 180;
                            double velY = -Math.sin(pitch) * distanceMult;
                            double mult = Math.cos(pitch);

                            double yaw = client.player.getYaw() * Math.PI / 180;
                            double velX = (-Math.sin(yaw) * mult + client.player.getVelocity().x) * distanceMult;
                            double velZ = (Math.cos(yaw) * mult + client.player.getVelocity().z) * distanceMult;


                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("dash");
                            buf.writeDouble(velY);
                            buf.writeDouble(velX);
                            buf.writeDouble(velZ);

                            client.player.setVelocity(velX, velY, velZ);
                            ClientPlayNetworking.send(ModPackets.LEGGING_ABILITY_ID, buf);
                        }
                        if (rushLevel > 0) {
                            cooldown = 300 - rushLevel * 20;
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("rush");
                            buf.writeInt(rushLevel);
                            ClientPlayNetworking.send(ModPackets.LEGGING_ABILITY_ID, buf);

                        }
                        if (blinkLevel > 0) {

                            double rads = client.player.getYaw() * Math.PI / 180;
                            double posX = -Math.sin(rads) * (2+blinkLevel) + client.player.getX();
                            double posZ = Math.cos(rads) * (2+blinkLevel) + client.player.getZ();
                            double posY = client.player.getY();

                            double velX = -Math.sin(rads) * 0.2;
                            double velZ = Math.cos(rads) * 0.2;
                            double velY = 0;
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("blink");
                            buf.writeDouble(posY);
                            buf.writeDouble(posX);
                            buf.writeDouble(posZ);

                            buf.writeDouble(velY);
                            buf.writeDouble(velX);
                            buf.writeDouble(velZ);

                            BlockState blockState = client.player.getWorld().getBlockState(new BlockPos((int) posX, (int) posY, (int) posZ));
                            if (!blockState.blocksMovement()) {
                                client.player.setPos(posX, posY, posZ);
                                client.player.setVelocity(velX, velY, velZ);
                                ClientPlayNetworking.send(ModPackets.LEGGING_ABILITY_ID, buf);
                                cooldown = 300 - blinkLevel * 20;
                            }
                        }

                        timerAccess.aabilities_setLeggingCooldown(cooldown);

                    }
                }
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KEY_BOOT_ABILITY.wasPressed()) {
                if(client.player != null){
                    TimerAccess timerAccess = ((TimerAccess) client.player);
                    if(timerAccess.aabilities_getBootCooldown() <= 0) {
                        int cooldown = 0;
                        int fireStompLevel = 0;
                        int frostStompLevel = 0;
                        int anvilStompLevel = 0;

                        for (ItemStack i : client.player.getArmorItems()) {

                            fireStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.FIRE_STOMP, i);
                            frostStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.FROST_STOMP, i);
                            anvilStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.ANVIL_STOMP, i);
                        }

                        if (fireStompLevel > 0) {
                            cooldown = 300 - fireStompLevel * 20;
                            if (client.player.isOnGround())
                                client.player.jump();

                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("fire_stomp");
                            buf.writeInt(fireStompLevel);
                            ClientPlayNetworking.send(ModPackets.BOOT_ABILITY_ID, buf);

                        }
                        if (frostStompLevel > 0) {
                            cooldown = 300 - frostStompLevel * 20;
                            if (client.player.isOnGround())
                                client.player.jump();

                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("frost_stomp");
                            buf.writeInt(frostStompLevel);
                            ClientPlayNetworking.send(ModPackets.BOOT_ABILITY_ID, buf);
                        }
                        if(anvilStompLevel > 0)
                        {
                            if(timerAccess.aabilities_getFuse() < 0 && !client.player.isFallFlying() && !client.player.isRiding()) {
                                cooldown = 300 - anvilStompLevel * 20;
                                PacketByteBuf buf = PacketByteBufs.create();
                                buf.writeString("anvil_stomp");
                                buf.writeInt(anvilStompLevel);
                                if (client.player.isOnGround())
                                    client.player.jump();
                                ClientPlayNetworking.send(ModPackets.BOOT_ABILITY_ID, buf);
                                timerAccess.aabilities_setAnvilStompTimer(200);
                            }
                        }
                        timerAccess.aabilities_setBootCooldown(cooldown);
                    }
                }
            }
        });
    }
}
