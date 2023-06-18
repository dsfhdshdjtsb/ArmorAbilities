package com.dsfhdshdjtsb.ArmorAbilities;

import com.dsfhdshdjtsb.ArmorAbilities.Enchantments.*;
import com.dsfhdshdjtsb.ArmorAbilities.StatusEffects.MindControllCooldownEffect;
import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ArmorAbilities implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final String modid = "aabilities";


	public static Enchantment FOCUS;
	public static Enchantment MIND_CONTROL;
	public static Enchantment TELEKINESIS;

	public static Enchantment CLEANSE;
	public static Enchantment EXPLODE;
	public static Enchantment SIPHON;

	public static Enchantment DASH;
	public static Enchantment RUSH;
	public static Enchantment BLINK;

	public static Enchantment FROST_STOMP;
	public static Enchantment FIRE_STOMP;
	public static Enchantment ANVIL_STOMP;

	public static final StatusEffect MIND_CONTROL_COOLDOWN_EFFECT = new MindControllCooldownEffect();



	@Override
	public void onInitialize() {

		ModConfigs.registerConfigs();

		FOCUS = new FocusEnchantment();
		MIND_CONTROL = new MindControlEnchantment();
		TELEKINESIS = new TelekinesisEnchantment();

		CLEANSE = new CleanseEnchantment();
		EXPLODE = new ExplodeEnchantment();
		SIPHON = new SiphonEnchantment();

		DASH = new DashEnchantment();
		RUSH = new RushEnchantment();
		BLINK = new BlinkEnchantment();

		FROST_STOMP = new FrostStompEnchantment();
		FIRE_STOMP = new FireStompEnchantment();
		ANVIL_STOMP = new AnvilStompEnchantment();


		Registry.register(Registries.STATUS_EFFECT, new Identifier("aabilities", "mind_control_cooldown"), MIND_CONTROL_COOLDOWN_EFFECT);
		ModPackets.registerC2SPackets();
	}
}
