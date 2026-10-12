/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.client.packet.SyncPoppetShelfPacket;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PoppetShelfBlockEntity extends BlockEntity {
	public NonNullList<ItemStack> clientInventory = NonNullList.withSize(9, ItemStack.EMPTY);

	public PoppetShelfBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.POPPET_SHELF.get(), pos, state);
	}

	public void onUse(Level world, BlockPos pos, Player player, InteractionHand hand) {
		BWWorldState worldState = BWWorldState.get(world);
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof PoppetItem) {
			int firstEmpty = getFirstEmptySlot();
			if (firstEmpty != -1) {
				NonNullList<ItemStack> inventory = worldState.poppetShelves.get(pos.asLong());
				inventory.set(firstEmpty, stack.split(1));
				sync();
				worldState.setDirty();
			}
		} else {
			NonNullList<ItemStack> inventory = worldState.poppetShelves.get(pos.asLong());
			if (inventory != null) {
				Containers.dropContents(world, pos, inventory);
				worldState.poppetShelves.remove(pos.asLong());
				sync();
				worldState.setDirty();
			}
		}
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		saveAdditional(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		clientInventory.clear();
		ContainerHelper.loadAllItems(nbt, clientInventory);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if (level != null && !level.isClientSide) {
			NonNullList<ItemStack> inventory = BWWorldState.get(level).poppetShelves.get(worldPosition.asLong());
			if (inventory != null) {
				ContainerHelper.saveAllItems(nbt, inventory);
			}
		}
	}

	public void sync() {
		if (level != null && !level.isClientSide) {
			BWNetwork.sendToTracking(this, SyncPoppetShelfPacket.of(level, worldPosition));
		}
	}

	private int getFirstEmptySlot() {
		BWWorldState worldState = BWWorldState.get(level);
		NonNullList<ItemStack> inventory = worldState.poppetShelves.get(worldPosition.asLong());
		if (inventory != null) {
			for (int i = 0; i < inventory.size(); i++) {
				if (inventory.get(i).isEmpty()) {
					return i;
				}
			}
		} else {
			worldState.poppetShelves.put(worldPosition.asLong(), NonNullList.withSize(9, ItemStack.EMPTY));
			worldState.setDirty();
			return 0;
		}
		return -1;
	}
}
