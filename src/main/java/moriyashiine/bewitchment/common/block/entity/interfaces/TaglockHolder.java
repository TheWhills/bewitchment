/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity.interfaces;

import moriyashiine.bewitchment.common.block.entity.JuniperChestBlockEntity;
import moriyashiine.bewitchment.common.block.entity.TaglockHolderBlockEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public interface TaglockHolder {
	NonNullList<ItemStack> getTaglockInventory();

	UUID getOwner();

	void setOwner(UUID owner);

	default void fromNbtTaglock(CompoundTag nbt) {
		ContainerHelper.loadAllItems(nbt.getCompound("TaglockInventory"), getTaglockInventory());
		if (nbt.contains("Owner")) {
			setOwner(nbt.getUUID("Owner"));
		}
	}

	default void toNbtTaglock(CompoundTag nbt) {
		nbt.put("TaglockInventory", ContainerHelper.saveAllItems(new CompoundTag(), getTaglockInventory()));
		if (getOwner() != null) {
			nbt.putUUID("Owner", getOwner());
		}
	}

	default void use(Level world, BlockPos pos, LivingEntity user) {
		if (!user.getUUID().equals(getOwner())) {
			addTaglock(world, pos, user);
		}
	}

	default void addTaglock(Level world, BlockPos pos, Entity entity) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		TaglockHolder taglockHolder = (TaglockHolder) blockEntity;
		boolean found = false;
		for (ItemStack stack : taglockHolder.getTaglockInventory()) {
			if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack) && entity.getUUID().equals(TaglockItem.getTaglockUUID(stack))) {
				found = true;
				break;
			}
		}
		if (!found) {
			for (int i = 0; i < taglockHolder.getTaglockInventory().size(); i++) {
				ItemStack stack = taglockHolder.getTaglockInventory().get(i);
				if (stack.getItem() instanceof TaglockItem && !TaglockItem.hasTaglock(stack)) {
					TaglockItem.putTaglock(stack, entity);
					syncTaglockHolder(blockEntity);
					blockEntity.setChanged();
					break;
				}
			}
		}
	}

	default int getFirstEmptySlot() {
		for (int i = 0; i < getTaglockInventory().size(); i++) {
			if (getTaglockInventory().get(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	default void syncTaglockHolder(BlockEntity blockEntity) {
		if (blockEntity instanceof TaglockHolderBlockEntity taglockHolderBlockEntity) {
			taglockHolderBlockEntity.sync();
		} else if (blockEntity instanceof JuniperChestBlockEntity juniperChestBlockEntity) {
			juniperChestBlockEntity.sync();
		}
	}

	static void onUse(Level world, BlockPos pos, LivingEntity user) {
		if (world.getBlockEntity(pos) instanceof TaglockHolder taglockHolder) {
			taglockHolder.use(world, pos, user);
		}
	}
}
