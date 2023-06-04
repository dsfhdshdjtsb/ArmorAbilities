package com.dsfhdshdjtsb.ArmorAbilities.StatusEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class MindControllCooldownEffect extends StatusEffect {
    public MindControllCooldownEffect() {
        super(StatusEffectCategory.HARMFUL, 0);
    }


    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration <= 60;
    }
}
