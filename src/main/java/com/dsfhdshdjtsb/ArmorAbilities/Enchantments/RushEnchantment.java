package com.dsfhdshdjtsb.ArmorAbilities.Enchantments;

import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

public class RushEnchantment  extends Enchantment {
    public RushEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        if(ModConfigs.RUSH)
            Registry.register(Registry.ENCHANTMENT, new Identifier("aabilities", "rush"), this);
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
        if(other instanceof BlinkEnchantment || other instanceof DashEnchantment)
            return false;
        return super.canAccept(other);
    }
}
