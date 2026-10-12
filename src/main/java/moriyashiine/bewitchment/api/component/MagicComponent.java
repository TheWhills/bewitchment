/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import moriyashiine.bewitchment.common.component.base.AutoSyncedComponent;
import moriyashiine.bewitchment.common.component.base.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class MagicComponent implements AutoSyncedComponent, ServerTickingComponent {
	public static final int MAX_MAGIC = 100;

	private final Player obj;
	private int magic = 0, magicTimer = 60;

	public MagicComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setMagic(tag.getInt("Magic"));
		setMagicTimer(tag.getInt("MagicTimer"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("Magic", getMagic());
		tag.putInt("MagicTimer", getMagicTimer());
	}

	@Override
	public void serverTick() {
		if (getMagicTimer() > 0) {
			setMagicTimer(getMagicTimer() - 1);
		}
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
		BWComponents.MAGIC_COMPONENT.sync(obj);
	}

	public int getMagicTimer() {
		return magicTimer;
	}

	public void setMagicTimer(int magicTimer) {
		this.magicTimer = magicTimer;
		BWComponents.MAGIC_COMPONENT.sync(obj);
	}

	public boolean fillMagic(int amount, boolean simulate) {
		if (getMagic() < MAX_MAGIC) {
			if (!simulate) {
				setMagic(Math.min(MAX_MAGIC, getMagic() + amount));
			}
			setMagicTimer(60);
			return true;
		}
		return false;
	}

	public boolean drainMagic(int amount, boolean simulate) {
		if (getMagic() - amount >= 0) {
			if (!simulate) {
				setMagic(getMagic() - amount);
			}
			setMagicTimer(60);
			return true;
		}
		return false;
	}
}
