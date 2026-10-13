/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import moriyashiine.bewitchment.common.component.base.AutoSyncedComponent;
import moriyashiine.bewitchment.common.component.base.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class AdditionalWaterDataComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final Entity obj;
	private boolean submerged = false;
	private int wetTimer = 0;

	public AdditionalWaterDataComponent(Entity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		submerged = tag.getBoolean("Submerged");
		wetTimer = tag.getInt("WetTimer");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("Submerged", isSubmerged());
		tag.putInt("WetTimer", getWetTimer());
	}

	@Override
	public void serverTick() {
		if (isSubmerged()) {
			setSubmerged(false);
		}
		if (getWetTimer() > 0) {
			setWetTimer(getWetTimer() - 1);
		}
	}

	public boolean isSubmerged() {
		return submerged;
	}

	public void setSubmerged(boolean submerged) {
		if (this.submerged != submerged) {
			this.submerged = submerged;
			BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.sync(obj);
		}
	}

	public int getWetTimer() {
		return wetTimer;
	}

	/**
	 * The client only needs to know whether the timer is running (Entity.isInWaterOrRain), so sync on
	 * start, refresh and expiry rather than on every one-tick countdown step, which sent a packet per
	 * wet entity per tick for up to 15 minutes after a rain ritual.
	 */
	public void setWetTimer(int wetTimer) {
		boolean countdownStep = wetTimer == this.wetTimer - 1 && wetTimer > 0;
		this.wetTimer = wetTimer;
		if (!countdownStep) {
			BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.sync(obj);
		}
	}
}
