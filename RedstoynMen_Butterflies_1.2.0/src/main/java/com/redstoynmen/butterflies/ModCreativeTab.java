package com.redstoynmen.butterflies;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTab {
	private static final ResourceKey<CreativeModeTab> KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
			Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, "butterflies"));

	public static final CreativeModeTab TAB = FabricCreativeModeTab.builder()
			.title(Component.translatable("itemGroup.redstoynmen_butterflies.butterflies"))
			.icon(() -> new ItemStack(ModItems.COMMON_BUTTERFLY_SPAWN_EGG))
			.displayItems((parameters, output) -> {
				output.accept(ModItems.COMMON_BUTTERFLY_SPAWN_EGG);
				output.accept(ModItems.GREEN_BUTTERFLY_SPAWN_EGG);
				output.accept(ModItems.RED_BUTTERFLY_SPAWN_EGG);
				output.accept(ModBlocks.COMMON_HIVE);
				output.accept(ModBlocks.GREEN_HIVE);
				output.accept(ModBlocks.RED_HIVE);
				output.accept(ModItems.COMMON_HONEY);
				output.accept(ModItems.POSITIVE_HONEY);
				output.accept(ModItems.AGGRESSIVE_HONEY);
					output.accept(ModBlocks.GREEN_POSITIVE_FLOWER);
					output.accept(ModBlocks.RED_AGGRESSIVE_FLOWER);
				output.accept(ModItems.POLLEN);
			})
			.build();

	private ModCreativeTab() {
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, KEY, TAB);
	}
}