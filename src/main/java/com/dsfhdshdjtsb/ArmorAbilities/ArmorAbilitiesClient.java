package com.dsfhdshdjtsb.ArmorAbilities;

import com.dsfhdshdjtsb.ArmorAbilities.client.CooldownHudOverlay;
import com.dsfhdshdjtsb.ArmorAbilities.event.KeyInputHandler;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ArmorAbilitiesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        // Event registration will be executed inside this method
        ModPackets.registerS2CPackets();

//        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
//            CooldownHud cooldownHud = new CooldownHud();
//            cooldownHud.render(matrixStack, tickDelta);
//        });
        HudRenderCallback.EVENT.register(new CooldownHudOverlay());


    }
}
