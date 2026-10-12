/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.juniper;

import moriyashiine.bewitchment.common.block.entity.TaglockHolderBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class JuniperPressurePlateBlock extends PressurePlateBlock implements EntityBlock {
	public JuniperPressurePlateBlock(Properties settings) {
		super(Sensitivity.EVERYTHING, settings, BlockSetType.OAK);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TaglockHolderBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		TaglockHolder.onUse(world, pos, player);
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		if (!world.isClientSide && placer != null) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			TaglockHolder taglockHolder = (TaglockHolder) blockEntity;
			taglockHolder.setOwner(placer.getUUID());
			taglockHolder.syncTaglockHolder(blockEntity);
			blockEntity.setChanged();
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClientSide && state.getBlock() != newState.getBlock() && world.getBlockEntity(pos) instanceof TaglockHolder taglockHolder) {
			Containers.dropContents(world, pos, taglockHolder.getTaglockInventory());
		}
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity) {
			TaglockHolder taglockHolder = ((TaglockHolder) world.getBlockEntity(pos));
			if (!entity.getUUID().equals(taglockHolder.getOwner())) {
				taglockHolder.addTaglock(world, pos, entity);
			}
		}
		super.entityInside(state, world, pos, entity);
	}
}
