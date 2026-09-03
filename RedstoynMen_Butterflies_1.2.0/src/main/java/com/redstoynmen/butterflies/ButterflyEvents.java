package com.redstoynmen.butterflies;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.player.Player;

public final class ButterflyEvents {
    private ButterflyEvents() { }

    public static void initialize() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof Player player && player.hasEffect(ModEffects.POSITIVE_HONEY)) return false;
            if (source.getEntity() instanceof Player player && player.hasEffect(ModEffects.POSITIVE_HONEY)) return false;
            return true;
        });
    }
}