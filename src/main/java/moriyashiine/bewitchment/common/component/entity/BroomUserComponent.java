/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class BroomUserComponent implements AutoSyncedComponent {
	private final Player obj;
	private boolean pressingForward = false;

	public BroomUserComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setPressingForward(tag.getBoolean("PressingForward"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("PressingForward", isPressingForward());
	}

	public boolean isPressingForward() {
		return pressingForward;
	}

	public void setPressingForward(boolean pressingForward) {
		this.pressingForward = pressingForward;
		BWComponents.BROOM_USER_COMPONENT.sync(obj);
	}
}
