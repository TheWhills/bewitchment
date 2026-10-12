/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWPledges;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class PledgeComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final Player obj;
	private String pledge = BWPledges.NONE, pledgeNextTick = "";

	public PledgeComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		String pledge = tag.getString("Pledge");
		if (pledge.isEmpty()) {
			pledge = BWPledges.NONE;
		}
		setPledge(pledge);
		setPledgeNextTick(tag.getString("PledgeNextTick"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putString("Pledge", getPledge());
		tag.putString("PledgeNextTick", getPledgeNextTick());
	}

	@Override
	public void serverTick() {
		if (!getPledgeNextTick().isEmpty()) {
			setPledge(getPledgeNextTick());
			setPledgeNextTick("");
		}
	}

	public String getPledge() {
		return pledge;
	}

	public void setPledge(String pledge) {
		this.pledge = pledge;
		BWComponents.PLEDGE_COMPONENT.sync(obj);
		BWComponents.TRANSFORMATION_COMPONENT.get(obj).updateAttributes();
	}

	public String getPledgeNextTick() {
		return pledgeNextTick;
	}

	public void setPledgeNextTick(String pledgeNextTick) {
		this.pledgeNextTick = pledgeNextTick;
	}
}
