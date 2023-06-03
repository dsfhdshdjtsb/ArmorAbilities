package com.dsfhdshdjtsb.ArmorAbilities.Enchantments;

import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AnvilStompEnchantment extends Enchantment {
    public AnvilStompEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        if(ModConfigs.ANVIL_STOMP)
            Registry.register(Registries.ENCHANTMENT, new Identifier("aabilities", "anvil_stomp"), this);
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
        if(other instanceof FrostStompEnchantment|| other instanceof BlinkEnchantment)
            return false;
        return super.canAccept(other);
    }
}
