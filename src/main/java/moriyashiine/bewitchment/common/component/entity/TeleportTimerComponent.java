/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import moriyashiine.bewitchment.common.component.base.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;

public class TeleportTimerComponent implements ServerTickingComponent {
	private int teleportTimer = 0;

	@Override
	public void readFromNbt(CompoundTag tag) {
		setTeleportTimer(tag.getInt("TeleportTimer"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("TeleportTimer", getTeleportTimer());
	}

	@Override
	public void serverTick() {
		if (getTeleportTimer() > 0) {
			setTeleportTimer(getTeleportTimer() - 1);
		}
	}

	public int getTeleportTimer() {
		return teleportTimer;
	}

	public void setTeleportTimer(int teleportTimer) {
		this.teleportTimer = teleportTimer;
	}
}
