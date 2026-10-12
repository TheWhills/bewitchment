/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

import net.minecraft.nbt.CompoundTag;

/**
 * A component whose full NBT is sent to the owning player and every player tracking the
 * entity whenever {@link ComponentKey#sync} is called, when tracking starts, and on login,
 * respawn and dimension change.
 */
public interface AutoSyncedComponent extends Component {
	default CompoundTag writeSyncData() {
		CompoundTag tag = new CompoundTag();
		writeToNbt(tag);
		return tag;
	}

	default void applySyncData(CompoundTag tag) {
		readFromNbt(tag);
	}
}
