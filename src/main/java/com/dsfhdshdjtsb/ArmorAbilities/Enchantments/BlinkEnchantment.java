package com.dsfhdshdjtsb.ArmorAbilities.Enchantments;

import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlinkEnchantment  extends Enchantment {
    public BlinkEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        if(ModConfigs.BLINK)
            Registry.register(Registry.ENCHANTMENT, new Identifier("aabilities", "blink"), this);
    }

    @Override
    public int getMinPower(int level) {
        return 1 + (level - 1) * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        if(other instanceof DashEnchantment|| other instanceof RushEnchantment)
            return false;
        return super.canAccept(other);
    }
}
