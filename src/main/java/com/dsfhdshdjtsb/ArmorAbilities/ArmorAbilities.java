package com.dsfhdshdjtsb.ArmorAbilities;

import com.dsfhdshdjtsb.ArmorAbilities.Enchantments.*;
import com.dsfhdshdjtsb.ArmorAbilities.StatusEffects.DodgeEffect;
import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import com.dsfhdshdjtsb.ArmorAbilities.item.LaserProjectileItem;
import com.dsfhdshdjtsb.ArmorAbilities.networking.ModPackets;
import com.dsfhdshdjtsb.ArmorAbilities.projectile.LaserProjectile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArmorAbilities implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final String modid = "aabilities";

//	public static Enchantment TRANSCEND;

	public static Enchantment CLEANSE;
	public static Enchantment EXPLODE;

	public static Enchantment BLINK;
	public static Enchantment FROST_STOMP;
	public static Enchantment FIRE_STOMP;

	public static Enchantment DODGE;
	public static Enchantment DASH;
	public static Enchantment RUSH;

	public static final StatusEffect DODGE_EFFECT = new DodgeEffect();

	public static final EntityType<LaserProjectile> LASER_PROJECTILE_ENTITY_TYPE = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(modid, "laser"),
			FabricEntityTypeBuilder.<LaserProjectile>create(SpawnGroup.MISC, LaserProjectile::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
					.trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
					.build() // VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS
	);

	public static final Item LASER_PROJECTILE_ITEM = new LaserProjectileItem(new Item.Settings().maxCount(16));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModConfigs.registerConfigs();
		//TRANSCEND = new TranscendEnchantment();

		CLEANSE = new CleanseEnchantment();
		EXPLODE = new ExplodeEnchantment();

		BLINK = new BlinkEnchantment();
		FROST_STOMP = new FrostStompEnchantment();
		FIRE_STOMP = new FireStompEnchantment();

		DODGE = new DodgeEnchantment();
		DASH = new DashEnchantment();
		RUSH = new RushEnchantment();

		Registry.register(Registries.STATUS_EFFECT, new Identifier("aabilities", "dodge"), DODGE_EFFECT);
		Registry.register(Registries.ITEM, new Identifier("aabilities", "laser"), LASER_PROJECTILE_ITEM);
		ModPackets.registerC2SPackets();
		ModPackets.registerS2CPackets();
	}
}
