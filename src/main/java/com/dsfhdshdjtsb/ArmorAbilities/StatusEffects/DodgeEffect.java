package com.dsfhdshdjtsb.ArmorAbilities.StatusEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class DodgeEffect extends StatusEffect {
    public DodgeEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
