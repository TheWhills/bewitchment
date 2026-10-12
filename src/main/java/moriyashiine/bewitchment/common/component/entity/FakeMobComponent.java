/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import java.util.UUID;

public class FakeMobComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final Mob obj;
	private UUID target = null;

	public FakeMobComponent(Mob obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setTarget(tag.getString("TargetUUID").isEmpty() ? null : UUID.fromString(tag.getString("TargetUUID")));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putString("TargetUUID", getTarget() == null ? "" : getTarget().toString());
	}

	@Override
	public void serverTick() {
		if (getTarget() != null) {
			LivingEntity entity = (LivingEntity) ((ServerLevel) obj.level()).getEntity(getTarget());
			if (entity == null || (obj.tickCount % 20 == 0 && (obj.getRandom().nextFloat() < 1 / 100f || !BWComponents.CURSES_COMPONENT.get(entity).hasCurse(BWCurses.INSANITY.get())))) {
				obj.remove(Entity.RemovalReason.DISCARDED);
			} else if (obj.getTarget() == null || !obj.getTarget().getUUID().equals(getTarget())) {
				obj.setTarget(entity);
			}
		}
	}

	public UUID getTarget() {
		return target;
	}

	public void setTarget(UUID target) {
		this.target = target;
		BWComponents.FAKE_MOB_COMPONENT.sync(obj);
	}
}
