package com.redstoynmen.butterflies;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class HoneyItem extends Item {
	private final Kind kind;

	public HoneyItem(Properties properties, Kind kind) {
		super(properties);
		this.kind = kind;
	}

	public void consume(LivingEntity user, ItemStack stack) {
		finishUsingItem(stack, user.level(), user);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
		ItemStack result = super.finishUsingItem(stack, level, user);
		if (!level.isClientSide()) {
			if (kind == Kind.POSITIVE) {
					user.addEffect(new MobEffectInstance(ModEffects.POSITIVE_HONEY, 20 * 60, 0, false, true, true));
			} else if (kind == Kind.AGGRESSIVE) {
					user.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 20 * 60, 0, false, true, true));
				user.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20 * 60, 0, false, true, true));
			}
		}
		return result;
	}

	public enum Kind {
		COMMON,
		POSITIVE,
		AGGRESSIVE
	}
}