package com.dsfhdshdjtsb.ArmorAbilities;

import com.dsfhdshdjtsb.ArmorAbilities.event.KeyInputHandler;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ArmorAbilitiesClient implements ClientModInitializer {

    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        // Event registration will be executed inside this method
        ModPackets.registerS2CPackets();
    }
}
