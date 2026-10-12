/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.juniper;

import moriyashiine.bewitchment.common.block.BWChestBlock;
import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import moriyashiine.bewitchment.common.block.entity.JuniperChestBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class JuniperChestBlock extends BWChestBlock {
	public JuniperChestBlock(Properties settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, boolean trapped) {
		super(settings, supplier, trapped);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new JuniperChestBlockEntity(BWBlockEntityTypes.JUNIPER_CHEST, pos, state, BWChestBlockEntity.Type.JUNIPER, trapped);
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
}
