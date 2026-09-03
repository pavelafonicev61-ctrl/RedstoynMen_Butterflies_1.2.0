package com.redstoynmen.butterflies;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class ModItems {
	public static final Item POLLEN = register("pollen", Item::new,
				new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).build()));
	public static final Item COMMON_HONEY = register("common_honey", Item::new,
				new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).build()));
	public static final Item POSITIVE_HONEY = register("positive_honey",
			properties -> new HoneyItem(properties, HoneyItem.Kind.POSITIVE),
				new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).build()));
	public static final Item AGGRESSIVE_HONEY = register("aggressive_honey",
			properties -> new HoneyItem(properties, HoneyItem.Kind.AGGRESSIVE),
				new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).build()));

	public static final Item COMMON_BUTTERFLY_SPAWN_EGG = register("common_butterfly_spawn_egg", SpawnEggItem::new,
			new Item.Properties().spawnEgg(ModEntityTypes.COMMON_BUTTERFLY));
	public static final Item GREEN_BUTTERFLY_SPAWN_EGG = register("green_butterfly_spawn_egg", SpawnEggItem::new,
			new Item.Properties().spawnEgg(ModEntityTypes.GREEN_BUTTERFLY));
	public static final Item RED_BUTTERFLY_SPAWN_EGG = register("red_butterfly_spawn_egg", SpawnEggItem::new,
			new Item.Properties().spawnEgg(ModEntityTypes.RED_BUTTERFLY));

	private ModItems() {
	}

	private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		Identifier id = Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, name);
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	static void registerBlockItem(String name, net.minecraft.world.level.block.Block block) {
		Identifier id = Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, name);
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
		Registry.register(BuiltInRegistries.ITEM, key,
				new BlockItem(block, new Item.Properties().setId(key).useBlockDescriptionPrefix()));
	}

	public static void initialize() {
		registerBlockItem("common_hive", ModBlocks.COMMON_HIVE);
		registerBlockItem("green_hive", ModBlocks.GREEN_HIVE);
		registerBlockItem("red_hive", ModBlocks.RED_HIVE);
		registerBlockItem("green_positive_flower", ModBlocks.GREEN_POSITIVE_FLOWER);
		registerBlockItem("red_aggressive_flower", ModBlocks.RED_AGGRESSIVE_FLOWER);
	}
}