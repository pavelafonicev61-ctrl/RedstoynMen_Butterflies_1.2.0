package com.redstoynmen.butterflies;

import java.util.Random;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** Lightweight runtime world generation for the butterfly mod. */
public final class WorldFeatures {
    private static final Random RANDOM = new Random();
    private static final int CHECK_INTERVAL = 1200;

    private WorldFeatures() { }

    public static void initialize() {
        ServerTickEvents.END_LEVEL_TICK.register(WorldFeatures::onWorldTick);
        ServerChunkEvents.CHUNK_GENERATE.register(WorldFeatures::onChunkGenerate);
    }

    private static void onChunkGenerate(ServerLevel level, LevelChunk chunk) {
        if (level.isClientSide()) return;

        BlockPos c = chunk.getPos().getWorldPosition();

        // Small chance per newly generated chunk: abandoned apiary.
        if (RANDOM.nextInt(180) == 0) {
            tryGenerateApiary(level, c.offset(8, 0, 8));
        }

        // Butterfly hives can naturally appear on non-coniferous tree trunks.
        if (RANDOM.nextInt(18) == 0) {
            tryGenerateTreeHive(level, chunk);
        }

        // Rare giant birch in old-growth birch forest.
        if (level.getBiome(c).is(Biomes.OLD_GROWTH_BIRCH_FOREST)
                && RANDOM.nextInt(100) == 0) {
            tryGenerateGiantBirch(level, c);
        }
    }

    private static void tryGenerateTreeHive(ServerLevel level, LevelChunk chunk) {
        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();

        for (int attempt = 0; attempt < 8; attempt++) {
            int x = minX + RANDOM.nextInt(16);
            int z = minZ + RANDOM.nextInt(16);

            BlockPos ground = level.getHeightmapPos(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    new BlockPos(x, 0, z)
            );

            for (int y = ground.getY() + 1; y < ground.getY() + 18; y++) {
                BlockPos p = new BlockPos(x, y, z);
                BlockState state = level.getBlockState(p);

                if (isTreeLog(state) && level.getBlockState(p.east()).isAir()) {
                    ButterflyHiveBlock.Variant v = switch (RANDOM.nextInt(3)) {
                        case 0 -> ButterflyHiveBlock.Variant.COMMON;
                        case 1 -> ButterflyHiveBlock.Variant.GREEN;
                        default -> ButterflyHiveBlock.Variant.RED;
                    };

                    level.setBlock(
                            p.east(),
                            hiveBlock(v).defaultBlockState()
                                    .setValue(ButterflyHiveBlock.HONEY_LEVEL, 5),
                            3
                    );
                    return;
                }
            }
        }
    }

    private static boolean isTreeLog(BlockState state) {
        return state.is(Blocks.OAK_LOG)
                || state.is(Blocks.BIRCH_LOG)
                || state.is(Blocks.ACACIA_LOG)
                || state.is(Blocks.JUNGLE_LOG)
                || state.is(Blocks.DARK_OAK_LOG)
                || state.is(Blocks.MANGROVE_LOG)
                || state.is(Blocks.CHERRY_LOG);
    }

    private static void onWorldTick(ServerLevel level) {
        if (level.getGameTime() % CHECK_INTERVAL != 0L) return;

        level.players().forEach(player -> {
            BlockPos center = player.blockPosition();

            // Rare abandoned glass apiary.
            if (RANDOM.nextDouble() < 0.01D
                    && level.getRandom().nextInt(4) == 0) {
                tryGenerateApiary(level, center);
            }

            // 1% chance for the giant birch in the Old Growth Birch Forest.
            if (level.getBiome(center).is(Biomes.OLD_GROWTH_BIRCH_FOREST)
                    && RANDOM.nextDouble() < 0.01D) {
                tryGenerateGiantBirch(level, center);
            }
        });
    }

    private static void tryGenerateGiantBirch(ServerLevel level, BlockPos center) {
        int dx = RANDOM.nextInt(17) - 8;
        int dz = RANDOM.nextInt(17) - 8;

        BlockPos ground = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                center.offset(dx, 0, dz)
        );

        if (!level.getBiome(ground).is(Biomes.OLD_GROWTH_BIRCH_FOREST)) return;
        if (!level.getBlockState(ground.below()).is(Blocks.GRASS_BLOCK)) return;

        for (int y = 0; y < 20; y++) {
            BlockPos p = ground.above(y);
            level.setBlock(p, Blocks.BIRCH_LOG.defaultBlockState(), 3);
        }

        for (int y = 0; y < 20; y++) {
            int radius = y < 16 ? 1 : 2;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) + Math.abs(z) <= radius + 1
                            && RANDOM.nextDouble() < 0.78D) {

                        BlockPos p = ground.offset(x, y, z);

                        if (level.getBlockState(p).isAir()) {
                            level.setBlock(
                                    p,
                                    Blocks.BIRCH_LOG.defaultBlockState(),
                                    3
                            );
                        }
                    }
                }
            }
        }

        // Small canopy.
        BlockPos top = ground.above(19);

        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                for (int y = -1; y <= 2; y++) {
                    if (Math.abs(x) + Math.abs(z) <= 5
                            && RANDOM.nextDouble() < 0.82D) {

                        level.setBlock(
                                top.offset(x, y, z),
                                Blocks.BIRCH_LEAVES.defaultBlockState(),
                                3
                        );
                    }
                }
            }
        }

        // Hollow 1x1 butterfly maze inside the trunk.
        buildButterflyMaze(level, ground);

        // Hive at the very top.
        ButterflyHiveBlock.Variant variant = switch (RANDOM.nextInt(3)) {
            case 0 -> ButterflyHiveBlock.Variant.COMMON;
            case 1 -> ButterflyHiveBlock.Variant.GREEN;
            default -> ButterflyHiveBlock.Variant.RED;
        };

        BlockPos hivePos = top.above(1);

        level.setBlock(
                hivePos,
                hiveBlock(variant).defaultBlockState(),
                3
        );

        // One butterfly inside.
        spawnButterfly(
                level,
                switch (RANDOM.nextInt(3)) {
                    case 0 -> ButterflyHiveBlock.Variant.COMMON;
                    case 1 -> ButterflyHiveBlock.Variant.GREEN;
                    default -> ButterflyHiveBlock.Variant.RED;
                },
                hivePos.below(1)
        );
    }

    private static void buildButterflyMaze(ServerLevel level, BlockPos ground) {
        int[][] path = {
                {0, 0}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},
                {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {0, 0}, {0, 1},
                {0, 0}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0},
                {1, -1}, {0, -1}
        };

        for (int y = 1; y < 20; y++) {
            int[] step = path[Math.min(y, path.length - 1)];

            BlockPos air = ground.offset(
                    step[0],
                    y,
                    step[1]
            );

            level.setBlock(
                    air,
                    Blocks.AIR.defaultBlockState(),
                    3
            );

            if (y % 3 == 0) {
                BlockPos side = air.east();

                if (!side.equals(ground.offset(0, y, 0))) {
                    level.setBlock(
                            side,
                            Blocks.AIR.defaultBlockState(),
                            3
                    );
                }
            }
        }
    }

    private static void tryGenerateApiary(ServerLevel level, BlockPos center) {
        int dx = RANDOM.nextInt(25) - 12;
        int dz = RANDOM.nextInt(25) - 12;

        BlockPos ground = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                center.offset(dx, 0, dz)
        );

        if (!level.getBlockState(ground.below()).is(Blocks.GRASS_BLOCK)) return;
        if (!level.getBlockState(ground).isAir()) return;

        if (!level.getEntitiesOfClass(
                net.minecraft.world.entity.Entity.class,
                new net.minecraft.world.phys.AABB(ground).inflate(12),
                e -> true
        ).isEmpty()) return;

        int w = 9;
        int h = 6;
        int d = 9;

        BlockPos base = ground;

        for (int x = -w / 2; x <= w / 2; x++) {
            for (int z = -d / 2; z <= d / 2; z++) {

                level.setBlock(
                        base.offset(x, 0, z),
                        Blocks.STONE_BRICKS.defaultBlockState(),
                        3
                );

                for (int y = 1; y <= h; y++) {
                    boolean wall =
                            Math.abs(x) == w / 2
                            || Math.abs(z) == d / 2
                            || y == h;

                    if (wall) {
                        level.setBlock(
                                base.offset(x, y, z),
                                Blocks.GLASS.defaultBlockState(),
                                3
                        );
                    } else {
                        level.setBlock(
                                base.offset(x, y, z),
                                Blocks.AIR.defaultBlockState(),
                                3
                        );
                    }
                }
            }
        }

        // Cobwebs.
        for (int i = 0; i < 14; i++) {
            int x = RANDOM.nextInt(w) - w / 2;
            int z = RANDOM.nextInt(d) - d / 2;
            int y = 1 + RANDOM.nextInt(h - 1);

            if (Math.abs(x) == w / 2 || Math.abs(z) == d / 2) {
                level.setBlock(
                        base.offset(x, y, z),
                        Blocks.COBWEB.defaultBlockState(),
                        3
                );
            }
        }

        // Hives.
        for (int i = 0; i < 7; i++) {
            int x = RANDOM.nextInt(5) - 2;
            int z = RANDOM.nextInt(5) - 2;
            int y = 1 + (i % 3);

            ButterflyHiveBlock.Variant v = switch (RANDOM.nextInt(3)) {
                case 0 -> ButterflyHiveBlock.Variant.COMMON;
                case 1 -> ButterflyHiveBlock.Variant.GREEN;
                default -> ButterflyHiveBlock.Variant.RED;
            };

            level.setBlock(
                    base.offset(x, y, z),
                    hiveBlock(v).defaultBlockState()
                            .setValue(ButterflyHiveBlock.HONEY_LEVEL, 5),
                    3
            );
        }

        // Small entrance.
        level.setBlock(
                base.offset(0, 1, -w / 2),
                Blocks.AIR.defaultBlockState(),
                3
        );

        spawnRandomButterflySet(
                level,
                base.above(2)
        );
    }

    private static void spawnRandomButterflySet(
            ServerLevel level,
            BlockPos pos
    ) {
        // 40% peaceful, 59% aggressive, 1% common.
        int roll = RANDOM.nextInt(100);

        ButterflyHiveBlock.Variant v =
                roll == 99
                        ? ButterflyHiveBlock.Variant.COMMON
                        : roll < 40
                        ? ButterflyHiveBlock.Variant.GREEN
                        : ButterflyHiveBlock.Variant.RED;

        spawnButterfly(level, v, pos);
    }

    private static void spawnButterfly(
            ServerLevel level,
            ButterflyHiveBlock.Variant variant,
            BlockPos pos
    ) {
        EntitySpawnReason reason = EntitySpawnReason.COMMAND;

        var type = switch (variant) {
            case COMMON -> ModEntityTypes.COMMON_BUTTERFLY;
            case GREEN -> ModEntityTypes.GREEN_BUTTERFLY;
            case RED -> ModEntityTypes.RED_BUTTERFLY;
        };

        var butterfly = type.create(level, reason);

        if (butterfly != null) {
            butterfly.setPos(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5
            );

            butterfly.setYRot(
                    RANDOM.nextFloat() * 360f
            );

            butterfly.setXRot(0f);

            level.addFreshEntity(butterfly);
        }
    }

    private static net.minecraft.world.level.block.Block hiveBlock(
            ButterflyHiveBlock.Variant variant
    ) {
        return switch (variant) {
            case COMMON -> ModBlocks.COMMON_HIVE;
            case GREEN -> ModBlocks.GREEN_HIVE;
            case RED -> ModBlocks.RED_HIVE;
        };
    }
}
