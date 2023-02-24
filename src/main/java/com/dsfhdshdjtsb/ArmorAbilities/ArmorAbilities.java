package com.dsfhdshdjtsb.ArmorAbilities;

import com.dsfhdshdjtsb.ArmorAbilities.Enchantments.*;
import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArmorAbilities implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final String modid = "aabilities";

	public static Enchantment BLINK;
	public static Enchantment FROST_STOMP;
	public static Enchantment FIRE_STOMP;

	public static Enchantment DODGE;
	public static Enchantment DASH;
	public static Enchantment RUSH;
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModConfigs.registerConfigs();
		BLINK = new BlinkEnchantment();
		FROST_STOMP = new FrostStompEnchantment();
		FIRE_STOMP = new FireStompEnchantment();

		DODGE = new DodgeEnchantment();
		DASH = new DashEnchantment();
		RUSH = new RushEnchantment();

		LOGGER.info("Hello Fabric world!");
		ModPackets.registerC2SPackets();

	}
}
