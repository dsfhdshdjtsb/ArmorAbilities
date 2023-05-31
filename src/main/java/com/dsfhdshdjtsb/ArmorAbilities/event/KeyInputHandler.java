package com.dsfhdshdjtsb.ArmorAbilities.event;

import com.dsfhdshdjtsb.ArmorAbilities.ArmorAbilities;
import com.dsfhdshdjtsb.ArmorAbilities.mixin.AabilitiesLivingEntityMixin;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import com.dsfhdshdjtsb.ArmorAbilities.util.TimerAccess;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.launch.GlobalProperties;

public class KeyInputHandler {
    public static KeyBinding KEY_BOOT_ABILITY;
    public static KeyBinding KEY_LEGGING_ABILITY;
    public static KeyBinding KEY_CHEST_ABILITY;
    public static KeyBinding KEY_HELMET_ABILITY;

    public static void register(){
        KEY_BOOT_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aabilities.boot_ability", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_V, // The keycode of the key
                "category.aabilities.test" // The translation key of the keybinding's category.
        ));
        KEY_LEGGING_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aabilities.legging_ability", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.aabilities.test" // The translation key of the keybinding's category.
        ));
        KEY_CHEST_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aabilities.chest_ability", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_X, // The keycode of the key
                "category.aabilities.test" // The translation key of the keybinding's category.
        ));
        KEY_HELMET_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aabilities.helmet_ability", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_Z, // The keycode of the key
                "category.aabilities.test" // The translation key of the keybinding's category.
        ));

        regKeyInputs();
    }

    public static void regKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            while(KEY_HELMET_ABILITY.wasPressed()) {
                if (client.player != null) {
                    TimerAccess timerAccess =  ((TimerAccess) client.player);
                    if(timerAccess.aabilities_getHelmetCooldown() <= 0) {
                        timerAccess.aabilities_setHelmetCooldown(100);
                        int pulverizeLevel = 0;
                        int telekinesisLevel = 0;
                        for (ItemStack i : client.player.getArmorItems()) {
                            pulverizeLevel += EnchantmentHelper.getLevel(ArmorAbilities.PULVERIZE, i);
                            telekinesisLevel += EnchantmentHelper.getLevel(ArmorAbilities.TELEKINESIS, i);
                        }
                        if(pulverizeLevel > 0)
                        {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("pulverize");
                            ClientPlayNetworking.send(ModPackets.HELMET_ABILITY_ID, buf);
                        }
                        if(telekinesisLevel > 0)
                        {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("telekinesis");
                            ClientPlayNetworking.send(ModPackets.HELMET_ABILITY_ID, buf);
                        }
                    }
                }
            }
        }));

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            while(KEY_CHEST_ABILITY.wasPressed()){
                if(client.player != null) {
                    TimerAccess timerAccess =  ((TimerAccess) client.player);
                    if(timerAccess.aabilities_getChestCooldown() <= 0) {
                        timerAccess.aabilities_setChestCooldown(100);
//                    int transcendLevel = 0;
//
//                    for (ItemStack i : client.player.getArmorItems()) {
//                        transcendLevel += EnchantmentHelper.getLevel(ArmorAbilities.TRANSCEND, i);
//                    }
//
//                    if(transcendLevel > 0){
//                        client.player.addVelocity(new Vec3d(0, 1.5, 0));
//                        PacketByteBuf buf = PacketByteBufs.create();
//                        buf.writeString("transcend");
//                        ClientPlayNetworking.send(ModPackets.CHEST_ABILITY_ID, buf);
//                    }

                        int cleanseLevel = 0;
                        int explodeLevel = 0;
                        for (ItemStack i : client.player.getArmorItems()) {
                            cleanseLevel += EnchantmentHelper.getLevel(ArmorAbilities.CLEANSE, i);
                            explodeLevel += EnchantmentHelper.getLevel(ArmorAbilities.EXPLODE, i);
                        }

                        if (cleanseLevel > 0) {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("cleanse");
                            ClientPlayNetworking.send(ModPackets.CHEST_ABILITY_ID, buf);

                        }

                        if (explodeLevel > 0) {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("explode");
                            ClientPlayNetworking.send(ModPackets.CHEST_ABILITY_ID, buf);
                        }
                    }
                }
            }
        }));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (KEY_LEGGING_ABILITY.wasPressed()) {
                if(client.player != null){
                    TimerAccess timerAccess = ((TimerAccess) client.player);
                    if(timerAccess.aabilities_getLeggingCooldown() <= 0) {
                        timerAccess.aabilities_setLeggingCooldown(100);
                        int dashLevel = 0;
                        int dodgeLevel = 0;
                        int rushLevel = 0;

                        for (ItemStack i : client.player.getArmorItems()) {
                            dashLevel += EnchantmentHelper.getLevel(ArmorAbilities.DASH, i);
                            dodgeLevel += EnchantmentHelper.getLevel(ArmorAbilities.DODGE, i);
                            rushLevel += EnchantmentHelper.getLevel(ArmorAbilities.RUSH, i);
                        }

                        if (dashLevel > 0) {
                            double pitch = client.player.getPitch() * Math.PI / 180;
                            double velY = -Math.sin(pitch);
                            double mult = Math.cos(pitch);

                            double yaw = client.player.getYaw() * Math.PI / 180;
                            double velX = -Math.sin(yaw) * mult + client.player.getVelocity().x;
                            double velZ = Math.cos(yaw) * mult + client.player.getVelocity().z;


                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("dash");
                            buf.writeDouble(velY);
                            buf.writeDouble(velX);
                            buf.writeDouble(velZ);

                            client.player.setVelocity(velX, velY, velZ);
                            ClientPlayNetworking.send(ModPackets.LEGGING_ABILITY_ID, buf);
                        }
                        if (rushLevel > 0) {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("rush");
                            ClientPlayNetworking.send(ModPackets.LEGGING_ABILITY_ID, buf);
                        }
                        if (dodgeLevel > 0) {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("dodge");
                            ClientPlayNetworking.send(ModPackets.LEGGING_ABILITY_ID, buf);
                        }
                    }
                }
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KEY_BOOT_ABILITY.wasPressed()) {
                System.out.println("Boot ability pressed");
                if(client.player != null){
                    TimerAccess timerAccess = ((TimerAccess) client.player);
                    if(timerAccess.aabilities_getBootCooldown() <= 0) {
                        timerAccess.aabilities_setBootCooldown(100);
                        int blinkLevel = 0;
                        int fireStompLevel = 0;
                        int frostStompLevel = 0;


                        for (ItemStack i : client.player.getArmorItems()) {
                            blinkLevel += EnchantmentHelper.getLevel(ArmorAbilities.BLINK, i);
                            fireStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.FIRE_STOMP, i);
                            frostStompLevel += EnchantmentHelper.getLevel(ArmorAbilities.FROST_STOMP, i);

                        }

                        if (blinkLevel > 0) {
                            double rads = client.player.getYaw() * Math.PI / 180;
                            double posX = -Math.sin(rads) * 4 + client.player.getX();
                            double posZ = Math.cos(rads) * 4 + client.player.getZ();
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

                            BlockState blockState = client.player.world.getBlockState(new BlockPos((int) posX, (int) posY, (int) posZ));
                            if (!blockState.getMaterial().blocksMovement()) {
                                client.player.setPos(posX, posY, posZ);
                                client.player.setVelocity(velX, velY, velZ);
                                ClientPlayNetworking.send(ModPackets.BOOT_ABILITY_ID, buf);
                            }
                        }
                        if (fireStompLevel > 0) {
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("fire_stomp");
//                        if(client.player.isOnGround())
//                            client.player.addVelocity(new Vec3d(0, 0.5, 0));
                            ClientPlayNetworking.send(ModPackets.BOOT_ABILITY_ID, buf);

                        }
                        if (frostStompLevel > 0) {
                            System.out.println("frost_stimp");
                            PacketByteBuf buf = PacketByteBufs.create();
                            buf.writeString("frost_stomp");
//                        if(client.player.isOnGround())
//                            client.player.addVelocity(new Vec3d(0, 0.5, 0));
                            ClientPlayNetworking.send(ModPackets.BOOT_ABILITY_ID, buf);
                        }
                    }
                }
            }
        });
    }
}
