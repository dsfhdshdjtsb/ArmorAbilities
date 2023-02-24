package com.dsfhdshdjtsb.ArmorAbilities.Enchantments;

import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlinkEnchantment  extends Enchantment {
    public BlinkEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        if(ModConfigs.BLINK)
            Registry.register(Registries.ENCHANTMENT, new Identifier("aabilities", "blink"), this);
    }

    @Override
    public int getMinPower(int level) {
        return 20;
    }

    @Override
    public int getMaxPower(int level) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other);
    }
}
