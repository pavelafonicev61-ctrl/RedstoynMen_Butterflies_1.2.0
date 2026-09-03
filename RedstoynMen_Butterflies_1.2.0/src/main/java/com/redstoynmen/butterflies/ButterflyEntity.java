package com.redstoynmen.butterflies;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;

public class ButterflyEntity extends Animal {
	private int nectarTimer;
	private int targetTimer;
	private int flowerTimer;

	public ButterflyEntity(EntityType<? extends ButterflyEntity> type, Level level) {
		super(type, level);
		this.setNoGravity(true);
	}

	public ButterflyEntity(Level level) {
		this(ModEntityTypes.COMMON_BUTTERFLY, level);
	}

	public static AttributeSupplier.Builder createButterflyAttributes() {
		return PathfinderMob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 6.0)
				.add(Attributes.MOVEMENT_SPEED, 0.22)
				.add(Attributes.FLYING_SPEED, 0.35)
				.add(Attributes.ATTACK_DAMAGE, 1.0)
				.add(Attributes.FOLLOW_RANGE, 12.0);
	}

	public boolean isCommon() {
		return getType() == ModEntityTypes.COMMON_BUTTERFLY;
	}

	public boolean isGreen() {
		return getType() == ModEntityTypes.GREEN_BUTTERFLY;
	}

	public boolean isRed() {
		return getType() == ModEntityTypes.RED_BUTTERFLY;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
		if (isRed()) {
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.35, false));
		}
		this.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.BreedGoal(this, 1.0));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return isCommon() && stack.is(ModItems.POLLEN);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
		if (!(otherParent instanceof ButterflyEntity other) || !other.isCommon() || !isCommon()) {
			return null;
		}
			boolean greenMutation = getRandom().nextDouble() < 0.001D;
			boolean redMutation = getRandom().nextDouble() < 0.0001D;
		EntityType<ButterflyEntity> offspringType = redMutation
				? ModEntityTypes.RED_BUTTERFLY
				: greenMutation ? ModEntityTypes.GREEN_BUTTERFLY : ModEntityTypes.COMMON_BUTTERFLY;
		return offspringType.create(level, EntitySpawnReason.BREEDING);
	}

	@Override
	public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
		return isGreen() || super.isInvulnerableTo(level, source);
	}

	@Override
	public boolean removeWhenFarAway(double distanceSquared) {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide()) {
			return;
		}
		if (isRed() && --targetTimer <= 0) {
			targetTimer = 10;
			List<LivingEntity> visible = level().getEntitiesOfClass(LivingEntity.class,
					getBoundingBox().inflate(12.0D), entity -> entity != this && entity.isAlive());
			if (!visible.isEmpty()) {
				setTarget(visible.get(0));
			}
		}
			if (--nectarTimer <= 0) {
			nectarTimer = 40;
			collectNectar();
		}
		Vec3 drift = getDeltaMovement().scale(0.78D).add(
				(random.nextDouble() - 0.5D) * 0.015D,
				(random.nextDouble() - 0.5D) * 0.012D,
				(random.nextDouble() - 0.5D) * 0.015D);
		setDeltaMovement(drift);
		move(MoverType.SELF, drift);
	}

	private void collectNectar() {
		BlockPos flower = findFlower();
		if (flower == null) {
			return;
		}
		for (BlockPos pos : BlockPos.withinManhattan(blockPosition(), 8, 4, 8)) {
			BlockEntity blockEntity = level().getBlockEntity(pos);
				if (blockEntity instanceof ButterflyHiveBlockEntity hive && hive.variant() == hiveForThisButterflyVariant()) {
					hive.addNectar();
				return;
			}
		}
			if (--flowerTimer <= 0) {
				flowerTimer = 2400;
			BlockPos hivePos = flower.above();
			if (level().getBlockState(hivePos).isAir()) {
				level().setBlock(hivePos, hiveForThisButterfly().defaultBlockState(), 3);
			}
		}
	}

	private BlockPos findFlower() {
		for (BlockPos pos : BlockPos.withinManhattan(blockPosition(), 5, 3, 5)) {
			BlockState state = level().getBlockState(pos);
			if (isSuitableFlower(state)) {
				return pos;
			}
		}
		return null;
	}

	private boolean isSuitableFlower(BlockState state) {
		if (isGreen()) {
				return state.is(ModBlocks.GREEN_POSITIVE_FLOWER);
		}
		if (isRed()) {
				return state.is(Blocks.POPPY) || state.is(ModBlocks.RED_AGGRESSIVE_FLOWER);
		}
		return state.is(Blocks.DANDELION) || state.is(Blocks.POPPY)
					|| state.is(ModBlocks.GREEN_POSITIVE_FLOWER) || state.is(ModBlocks.RED_AGGRESSIVE_FLOWER);
	}

	private ButterflyHiveBlock.Variant hiveForThisButterflyVariant() {
			return isGreen() ? ButterflyHiveBlock.Variant.GREEN : isRed() ? ButterflyHiveBlock.Variant.RED : ButterflyHiveBlock.Variant.COMMON;
		}

	private net.minecraft.world.level.block.Block hiveForThisButterfly() {
		if (isGreen()) {
			return ModBlocks.GREEN_HIVE;
		}
		if (isRed()) {
			return ModBlocks.RED_HIVE;
		}
		return ModBlocks.COMMON_HIVE;
	}
}