/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.dragonsblood;

import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class DragonsBloodLogBlock extends RotatedPillarBlock {
	public DragonsBloodLogBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(BWProperties.NATURAL, false).setValue(BWProperties.CUT, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (state.getValue(BWProperties.NATURAL) && !state.getValue(BWProperties.CUT) && stack.getItem() instanceof AthameItem) {
			boolean client = world.isClientSide;
			if (!client) {
				world.setBlockAndUpdate(pos, state.setValue(BWProperties.CUT, true));
				world.playSound(null, pos, BWSoundEvents.ITEM_ATHAME_STRIP, SoundSource.BLOCKS, 1, 1);
				stack.hurtAndBreak(1, player, stackUser -> stackUser.broadcastBreakEvent(hand));
			}
			return InteractionResult.sidedSuccess(client);
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (state.getValue(BWProperties.CUT)) {
			if (random.nextInt(10) == 0) {
				for (int i = 0; i < 8; i++) {
					BlockPos offset = pos.relative(Direction.Plane.HORIZONTAL.getRandomDirection(random));
					if (world.getBlockState(offset).isAir()) {
						Containers.dropItemStack(world, offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5, new ItemStack(BWObjects.DRAGONS_BLOOD_RESIN));
						world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1, 1);
						break;
					}
				}
			}
			if (random.nextInt(50) == 0) {
				world.setBlockAndUpdate(pos, state.setValue(BWProperties.CUT, false));
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(BWProperties.NATURAL, BWProperties.CUT));
	}
}
