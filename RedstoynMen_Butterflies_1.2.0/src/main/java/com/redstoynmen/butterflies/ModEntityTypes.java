package com.redstoynmen.butterflies;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ModEntityTypes {
    public static final EntityType<ButterflyEntity> COMMON_BUTTERFLY = register(
            "common_butterfly",
            EntityType.Builder.<ButterflyEntity>of(ButterflyEntity::new, MobCategory.CREATURE)
                    .sized(0.55f, 0.35f)
                    .clientTrackingRange(8));

    public static final EntityType<ButterflyEntity> GREEN_BUTTERFLY = register(
            "green_butterfly",
            EntityType.Builder.<ButterflyEntity>of(ButterflyEntity::new, MobCategory.CREATURE)
                    .sized(0.55f, 0.35f)
                    .clientTrackingRange(8));

    public static final EntityType<ButterflyEntity> RED_BUTTERFLY = register(
            "red_butterfly",
            EntityType.Builder.<ButterflyEntity>of(ButterflyEntity::new, MobCategory.CREATURE)
                    .sized(0.55f, 0.35f)
                    .clientTrackingRange(8));

    private ModEntityTypes() {
    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(
                Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void initialize() {
        FabricDefaultAttributeRegistry.register(COMMON_BUTTERFLY, ButterflyEntity.createButterflyAttributes());
        FabricDefaultAttributeRegistry.register(GREEN_BUTTERFLY, ButterflyEntity.createButterflyAttributes());
        FabricDefaultAttributeRegistry.register(RED_BUTTERFLY, ButterflyEntity.createButterflyAttributes());

        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), MobCategory.CREATURE, COMMON_BUTTERFLY, 5, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), MobCategory.CREATURE, GREEN_BUTTERFLY, 7, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), MobCategory.CREATURE, RED_BUTTERFLY, 4, 1, 2);

        SpawnPlacements.register(COMMON_BUTTERFLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(GREEN_BUTTERFLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(RED_BUTTERFLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules);
    }
}
