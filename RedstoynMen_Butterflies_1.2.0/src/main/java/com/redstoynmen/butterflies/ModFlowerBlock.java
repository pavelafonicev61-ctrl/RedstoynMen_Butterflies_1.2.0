package com.redstoynmen.butterflies;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Butterfly flowers behave like normal small flowers: they need a supporting block below them. */
public class ModFlowerBlock extends Block {
    public ModFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties.noCollision().instabreak());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportFlower(level, pos.below());
    }

    private static boolean canSupportFlower(BlockGetter level, BlockPos below) {
        BlockState support = level.getBlockState(below);
        return support.isFaceSturdy(level, below, Direction.UP)
                && !support.isAir();
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level,
                                     net.minecraft.world.level.ScheduledTickAccess ticks,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, net.minecraft.util.RandomSource random) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }
}
