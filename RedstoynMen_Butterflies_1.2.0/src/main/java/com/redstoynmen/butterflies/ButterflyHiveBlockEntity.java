package com.redstoynmen.butterflies;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public final class ButterflyHiveBlockEntity extends BlockEntity {
    private final ButterflyHiveBlock.Variant variant;
    private int honey = 0;
    private long lastProductionDay = -1;
    private long lastHarvestDay = -1;

    public ButterflyHiveBlockEntity(BlockPos pos, BlockState state, ButterflyHiveBlock.Variant variant) {
        super(ModBlockEntities.BUTTERFLY_HIVE, pos, state);
        this.variant = variant;
    }

    public ButterflyHiveBlock.Variant variant() { return variant; }

    public void addNectar() {
        if (honey < 5) {
            honey = Math.min(5, honey + 1);
            setChanged();
            if (level != null) {
                level.setBlock(worldPosition, getBlockState().setValue(ButterflyHiveBlock.HONEY_LEVEL, honey), 3);
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        honey = input.getIntOr("honey", 0);
        lastProductionDay = input.getLongOr("production_day", -1L);
        lastHarvestDay = input.getLongOr("harvest_day", -1L);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putInt("honey", honey);
        output.putLong("production_day", lastProductionDay);
        output.putLong("harvest_day", lastHarvestDay);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ButterflyHiveBlockEntity hive) {
        if (level.isClientSide()) return;
        long day = level.getGameTime() / 24000L;
        if (hive.honey < 5 && hive.lastProductionDay != day && level.getGameTime() % 20L == 0L) {
            hive.honey = Math.min(5, hive.honey + 1);
            hive.lastProductionDay = day;
            level.setBlock(pos, state.setValue(ButterflyHiveBlock.HONEY_LEVEL, hive.honey), 3);
            hive.setChanged();
        }
    }

    public ItemStack harvest(Player player) {
        long day = level.getGameTime() / 24000L;
        long cooldown = variant == ButterflyHiveBlock.Variant.RED ? 2 : variant == ButterflyHiveBlock.Variant.GREEN ? 1 : 0;
        if (honey <= 0 || lastHarvestDay >= 0 && day < lastHarvestDay + cooldown) return ItemStack.EMPTY;
        honey--;
        lastHarvestDay = day;
        level.setBlock(worldPosition, getBlockState().setValue(ButterflyHiveBlock.HONEY_LEVEL, honey), 3);
        setChanged();
        return new ItemStack(switch (variant) {
            case COMMON -> ModItems.COMMON_HONEY;
            case GREEN -> ModItems.POSITIVE_HONEY;
            case RED -> ModItems.AGGRESSIVE_HONEY;
        });
    }
}