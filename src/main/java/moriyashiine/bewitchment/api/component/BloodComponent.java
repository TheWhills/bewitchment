/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import moriyashiine.bewitchment.common.component.base.AutoSyncedComponent;
import net.minecraftforge.common.MinecraftForge;
import moriyashiine.bewitchment.api.event.BloodEvent;
import moriyashiine.bewitchment.common.component.base.ServerTickingComponent;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class BloodComponent implements AutoSyncedComponent, ServerTickingComponent {
	public static final int MAX_BLOOD = 100;

	private final LivingEntity obj;
	private int blood = MAX_BLOOD;

	public BloodComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (tag.contains("Blood")) {
			setBlood(tag.getInt("Blood"));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("Blood", getBlood());
	}

	@Override
	public void serverTick() {
		if (obj.getType().is(BWTags.HAS_BLOOD) && !BewitchmentAPI.isVampire(obj, true) && obj.getRandom().nextFloat() < (obj.isSleeping() ? 1 / 50f : 1 / 500f)) {
			fillBlood(1, false);
		}
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		MinecraftForge.EVENT_BUS.post(new BloodEvent.Set(obj, blood));
		this.blood = blood;
		BWComponents.BLOOD_COMPONENT.sync(obj);
	}

	public boolean fillBlood(int amount, boolean simulate) {
		MinecraftForge.EVENT_BUS.post(new BloodEvent.Fill(obj, amount, simulate));
		if (getBlood() < MAX_BLOOD) {
			if (!simulate) {
				setBlood(Math.min(MAX_BLOOD, getBlood() + amount));
			}
			return true;
		}
		return false;
	}

	public boolean drainBlood(int amount, boolean simulate) {
		MinecraftForge.EVENT_BUS.post(new BloodEvent.Drain(obj, amount, simulate));
		if (getBlood() - amount >= 0) {
			if (!simulate) {
				setBlood(getBlood() - amount);
			}
			return true;
		}
		return false;
	}
}
