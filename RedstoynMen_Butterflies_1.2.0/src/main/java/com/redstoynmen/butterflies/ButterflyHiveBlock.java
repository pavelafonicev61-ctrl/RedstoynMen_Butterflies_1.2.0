package com.redstoynmen.butterflies;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import com.mojang.serialization.MapCodec;

public final class ButterflyHiveBlock extends BaseEntityBlock {
    public enum Variant { COMMON, GREEN, RED }

    private final Variant variant;

    public static final net.minecraft.world.level.block.state.properties.IntegerProperty HONEY_LEVEL =
            net.minecraft.world.level.block.state.properties.BlockStateProperties.LEVEL_HONEY;

    public ButterflyHiveBlock(BlockBehaviour.Properties properties, Variant variant) {
        super(properties);
        this.variant = variant;
        this.registerDefaultState(this.stateDefinition.any().setValue(HONEY_LEVEL, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(properties -> new ButterflyHiveBlock(properties, variant));
    }

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HONEY_LEVEL);
    }

    public Variant variant() { return variant; }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ButterflyHiveBlockEntity(pos, state, variant);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BUTTERFLY_HIVE, ButterflyHiveBlockEntity::tick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ButterflyHiveBlockEntity hive)) return InteractionResult.PASS;
        ItemStack result = hive.harvest(player);
        if (result.isEmpty()) return InteractionResult.PASS;
        if (!player.getInventory().add(result)) player.drop(result, false);
        return InteractionResult.SUCCESS;
    }
}