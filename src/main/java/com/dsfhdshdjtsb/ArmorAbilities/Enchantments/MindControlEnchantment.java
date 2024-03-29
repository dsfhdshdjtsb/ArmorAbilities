package com.dsfhdshdjtsb.ArmorAbilities.Enchantments;

import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MindControlEnchantment extends Enchantment {
    public MindControlEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        if(ModConfigs.MIND_CONTROL)
            Registry.register(Registries.ENCHANTMENT, new Identifier("aabilities", "mind_control"), this);
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
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        super.onTargetDamaged(user, target, level);


    }

    @Override
    protected boolean canAccept(Enchantment other) {
        if(other instanceof FocusEnchantment || other instanceof TelekinesisEnchantment)
            return false;
        return super.canAccept(other);
    }
}
