package com.redstoynmen.butterflies;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {
    public static final Block COMMON_HIVE = registerHive("common_hive", ButterflyHiveBlock.Variant.COMMON);
    public static final Block GREEN_HIVE = registerHive("green_hive", ButterflyHiveBlock.Variant.GREEN);
    public static final Block RED_HIVE = registerHive("red_hive", ButterflyHiveBlock.Variant.RED);
    public static final Block GREEN_POSITIVE_FLOWER = registerFlower("green_positive_flower");
    public static final Block RED_AGGRESSIVE_FLOWER = registerFlower("red_aggressive_flower");

    private ModBlocks() { }

    private static ResourceKey<Block> key(String name) {
        return ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, name));
    }

    private static Block registerHive(String name, ButterflyHiveBlock.Variant variant) {
        return register(name, properties -> new ButterflyHiveBlock(properties, variant),
                BlockBehaviour.Properties.of().setId(key(name)).strength(2.0f).sound(SoundType.WOOD));
    }

    private static Block registerFlower(String name) {
        return register(name, ModFlowerBlock::new,
                BlockBehaviour.Properties.of().setId(key(name)).noCollision().instabreak().sound(SoundType.GRASS));
    }

    private static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> factory,
                                                BlockBehaviour.Properties properties) {
        T block = factory.apply(properties);
        Registry.register(BuiltInRegistries.BLOCK, key(name), block);
        return block;
    }

    public static void initialize() { }
}