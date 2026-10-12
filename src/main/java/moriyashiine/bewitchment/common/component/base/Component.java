/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

import net.minecraft.nbt.CompoundTag;

/**
 * Per-entity data attached through {@link ComponentContainer}. Same shape as the Cardinal
 * Components API this mod used on Fabric, so the component classes ported unchanged.
 */
public interface Component {
	void readFromNbt(CompoundTag tag);

	void writeToNbt(CompoundTag tag);
}
