package com.redstoynmen.butterflies;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RedstoynMenButterflies implements ModInitializer {
    public static final String MOD_ID = "redstoynmen_butterflies";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModEffects.initialize();
        ModBlocks.initialize();
        ModBlockEntities.initialize();
        ModEntityTypes.initialize();
        ModItems.initialize();
        ModCreativeTab.initialize();
        ButterflyEvents.initialize();
        WorldFeatures.initialize();
    }
}