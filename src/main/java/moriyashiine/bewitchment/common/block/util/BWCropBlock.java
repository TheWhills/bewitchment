/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.util;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BWCropBlock extends CropBlock {
	private static final VoxelShape[] AGE_TO_SHAPE = {Block.box(0, 0, 0, 16, 2, 16), Block.box(0, 0, 0, 16, 4, 16), Block.box(0, 0, 0, 16, 6, 16), Block.box(0, 0, 0, 16, 8, 16), Block.box(0, 0, 0, 16, 10, 16), Block.box(0, 0, 0, 16, 12, 16), Block.box(0, 0, 0, 16, 14, 16), Block.box(0, 0, 0, 16, 16, 16)};

	public BWCropBlock(Properties settings) {
		super(settings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		int age = getAge(state);
		int modelAge = 0;
		if (this == BWObjects.ACONITE_CROP.get()) {
			modelAge = age == 0 ? 1 : age == 1 ? 3 : age == 2 ? 5 : 7;
		}
		if (this == BWObjects.BELLADONNA_CROP.get()) {
			modelAge = age == 0 ? 1 : age == 1 ? 3 : age == 2 ? 5 : 6;
		}
		if (this == BWObjects.GARLIC_CROP.get()) {
			modelAge = age == 0 ? 2 : age == 1 ? 3 : age == 2 ? 5 : 6;
		}
		if (this == BWObjects.MANDRAKE_CROP.get()) {
			modelAge = age == 0 ? 1 : age == 1 ? 2 : 3;
		}
		return AGE_TO_SHAPE[modelAge];
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return this == BWObjects.ACONITE_CROP.get() ? BWObjects.ACONITE_SEEDS.get() : this == BWObjects.BELLADONNA_CROP.get() ? BWObjects.BELLADONNA_SEEDS.get() : this == BWObjects.GARLIC_CROP.get() ? BWObjects.GARLIC.get() : BWObjects.MANDRAKE_SEEDS.get();
	}

	@Override
	public IntegerProperty getAgeProperty() {
		return BlockStateProperties.AGE_3;
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	protected int getBonemealAgeIncrease(Level world) {
		return Mth.nextInt(world.random, 1, 2);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.entityInside(state, world, pos, entity);
		if (entity instanceof LivingEntity livingEntity) {
			boolean damage = false;
			if (this == BWObjects.GARLIC_CROP.get() && BewitchmentAPI.isVampire(entity, true)) {
				damage = true;
			} else if (this == BWObjects.ACONITE_CROP.get() && BewitchmentAPI.isWerewolf(entity, true)) {
				damage = true;
			}
			if (damage) {
				entity.hurt(BWDamageSources.create(world, BWDamageSources.MAGIC_COPY), livingEntity.getMaxHealth() * 1 / 4f);
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty());
	}
}
