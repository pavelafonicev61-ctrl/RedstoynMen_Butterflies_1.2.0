package com.redstoynmen.butterflies;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public final class ModEffects {
	public static final Holder<MobEffect> POSITIVE_HONEY = register("positive_honey", new MobEffect(MobEffectCategory.BENEFICIAL, 0x49E34F) { });

	private static Holder<MobEffect> register(String name, MobEffect effect) {
		return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, name), effect);
    }

    public static void initialize() { }
}