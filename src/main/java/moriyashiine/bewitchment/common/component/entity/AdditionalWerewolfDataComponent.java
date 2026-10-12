/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class AdditionalWerewolfDataComponent implements AutoSyncedComponent {
	private final Player obj;
	private int variant = 0;
	private boolean forcedTransformation = false;

	public AdditionalWerewolfDataComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setForcedTransformation(tag.getBoolean("ForcedTransformation"));
		setVariant(tag.getInt("WerewolfVariant"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("ForcedTransformation", isForcedTransformation());
		tag.putInt("WerewolfVariant", getVariant());
	}

	public int getVariant() {
		return variant;
	}

	public void setVariant(int variant) {
		this.variant = variant;
		BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.sync(obj);
	}

	public boolean isForcedTransformation() {
		return forcedTransformation;
	}

	public void setForcedTransformation(boolean forcedTransformation) {
		this.forcedTransformation = forcedTransformation;
		BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.sync(obj);
	}
}
