package com.redstoynmen.butterflies;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
    public static final BlockEntityType<ButterflyHiveBlockEntity> BUTTERFLY_HIVE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, "butterfly_hive"),
            FabricBlockEntityTypeBuilder.create(
                    (pos, state) -> new ButterflyHiveBlockEntity(pos, state,
                            state.getBlock() instanceof ButterflyHiveBlock hive
                                    ? hive.variant()
                                    : ButterflyHiveBlock.Variant.COMMON),
                    ModBlocks.COMMON_HIVE, ModBlocks.GREEN_HIVE, ModBlocks.RED_HIVE
            ).build()
    );

    private ModBlockEntities() { }

    public static void initialize() { }
}
