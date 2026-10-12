/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.misc;

import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record PoppetData(ItemStack stack, Long longPos, Integer index) {
	public static final PoppetData EMPTY = new PoppetData(ItemStack.EMPTY, null, null);

	public void update(Level world, boolean sync) {
		if (longPos != null && world instanceof ServerLevel serverWorld) {
			for (ServerLevel foundWorld : serverWorld.getServer().getAllLevels()) {
				BWWorldState worldState = BWWorldState.get(foundWorld);
				NonNullList<ItemStack> shelf = worldState.poppetShelves.get(longPos);
				if (shelf != null) {
					for (int i = shelf.size() - 1; i >= 0; i--) {
						if (i == index) {
							worldState.poppetShelves.get(longPos).set(i, stack);
							boolean empty = true;
							for (ItemStack stack : worldState.poppetShelves.get(longPos)) {
								if (!stack.isEmpty()) {
									empty = false;
									break;
								}
							}
							if (empty) {
								worldState.poppetShelves.remove(longPos);
							}
							worldState.setDirty();
							if (sync) {
								BlockPos pos = BlockPos.of(longPos);
								BlockEntity blockEntity = foundWorld.getBlockEntity(pos);
								if (blockEntity instanceof PoppetShelfBlockEntity poppetShelfBlockEntity) {
									poppetShelfBlockEntity.sync();
								}
							}
							return;
						}
					}
				}
			}
		}
	}
}
