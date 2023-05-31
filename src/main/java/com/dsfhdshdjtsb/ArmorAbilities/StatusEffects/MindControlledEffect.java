package com.dsfhdshdjtsb.ArmorAbilities.StatusEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;

public class MindControlledEffect extends StatusEffect {
    public MindControlledEffect() {
        super(StatusEffectCategory.HARMFUL, 0);
    }


    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof MobEntity)
        {
            ((MobEntity) entity).setTarget(null);
            System.out.println(((MobEntity) entity).getTarget());
            ((MobEntity) entity).setAttacking(null);
            entity.setAttacker(null);

        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration <= 60;
    }
}
