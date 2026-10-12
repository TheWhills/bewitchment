/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import moriyashiine.bewitchment.common.component.base.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;

public class RespawnTimerComponent implements ServerTickingComponent {
	private int respawnTimer = 400;

	@Override
	public void readFromNbt(CompoundTag tag) {
		setRespawnTimer(tag.getInt("RespawnTimer"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("RespawnTimer", getRespawnTimer());
	}

	@Override
	public void serverTick() {
		if (getRespawnTimer() > 0) {
			setRespawnTimer(getRespawnTimer() - 1);
		}
	}

	public int getRespawnTimer() {
		return respawnTimer;
	}

	public void setRespawnTimer(int respawnTimer) {
		this.respawnTimer = respawnTimer;
	}
}
