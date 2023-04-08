package com.dsfhdshdjtsb.ArmorAbilities.Enchantments;

import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DodgeEnchantment  extends Enchantment {
    public DodgeEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        if(ModConfigs.DODGE)
            Registry.register(Registries.ENCHANTMENT, new Identifier("aabilities", "dodge"), this);
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
        return super.canAccept(other);
    }
}
