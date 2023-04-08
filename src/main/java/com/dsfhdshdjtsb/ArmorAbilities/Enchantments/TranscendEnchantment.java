//package com.dsfhdshdjtsb.ArmorAbilities.Enchantments;
//
//import com.dsfhdshdjtsb.ArmorAbilities.config.ModConfigs;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.EnchantmentTarget;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.registry.Registries;
//import net.minecraft.registry.Registry;
//import net.minecraft.util.Identifier;
//
//public class TranscendEnchantment extends Enchantment {
//    public TranscendEnchantment() {
//        super(Rarity.VERY_RARE, EnchantmentTarget.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
//        if(ModConfigs.TRANSCEND)
//            Registry.register(Registries.ENCHANTMENT, new Identifier("aabilities", "transcend"), this);
//    }
//
//    @Override
//    public int getMinPower(int level) {
//        return 20;
//    }
//
//    @Override
//    public int getMaxPower(int level) {
//        return 50;
//    }
//
//    @Override
//    public int getMaxLevel() {
//        return 1;
//    }
//
//    @Override
//    protected boolean canAccept(Enchantment other) {
//        return super.canAccept(other);
//    }
//}
