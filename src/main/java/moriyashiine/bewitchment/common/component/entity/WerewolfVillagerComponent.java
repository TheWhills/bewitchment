/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.NotNull;

public class WerewolfVillagerComponent implements ServerTickingComponent {
	private final Villager obj;
	private CompoundTag storedWerewolf = null;
	private int despawnTimer = 2400;

	public WerewolfVillagerComponent(Villager obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (tag.contains("StoredWerewolf")) {
			storedWerewolf = tag.getCompound("StoredWerewolf");
		}
		if (tag.contains("DespawnTimer")) {
			despawnTimer = tag.getInt("DespawnTimer");
		}
	}

	@Override
	public void writeToNbt(@NotNull CompoundTag tag) {
		if (storedWerewolf != null) {
			tag.put("StoredWerewolf", storedWerewolf);
		}
		tag.putInt("DespawnTimer", despawnTimer);
	}

	@Override
	public void serverTick() {
		if (getStoredWerewolf() != null) {
			if (!obj.hasCustomName() && getDespawnTimer() > 0) {
				setDespawnTimer(getDespawnTimer() - 1);
				if (getDespawnTimer() == 0) {
					obj.remove(Entity.RemovalReason.DISCARDED);
				}
			}
			if (obj.tickCount % 20 == 0 && obj.level().isNight() && BewitchmentAPI.getMoonPhase(obj.level()) == 0 && (obj.hasCustomName() || obj.level().canSeeSky(obj.blockPosition()))) {
				WerewolfEntity entity = BWEntityTypes.WEREWOLF.get().create(obj.level());
				if (entity != null) {
					BWNetwork.sendToTracking(obj, new SpawnSmokeParticlesPacket(obj));
					obj.level().playSound(null, obj.getX(), obj.getY(), obj.getZ(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM.get(), obj.getSoundSource(), 1, obj.getVoicePitch());
					entity.load(getStoredWerewolf());
					entity.absMoveTo(obj.getX(), obj.getY(), obj.getZ(), obj.getRandom().nextFloat() * 360, 0);
					entity.setHealth(entity.getMaxHealth() * (obj.getHealth() / obj.getMaxHealth()));
					entity.setRemainingFireTicks(obj.getRemainingFireTicks());
					entity.removeAllEffects();
					obj.getActiveEffects().forEach(entity::addEffect);
					BWComponents.CURSES_COMPONENT.maybeGet(entity).ifPresent(entityCursesComponent -> BWComponents.CURSES_COMPONENT.maybeGet(obj).ifPresent(thisCursesComponent -> {
						entityCursesComponent.getCurses().clear();
						thisCursesComponent.getCurses().forEach(entityCursesComponent::addCurse);
					}));
					if (!obj.hasCustomName() && getDespawnTimer() >= 0) {
						setDespawnTimer(2400);
					}
					entity.storedVillager = obj.saveWithoutId(new CompoundTag());
					obj.level().addFreshEntity(entity);
					obj.remove(Entity.RemovalReason.DISCARDED);
				}
			}
		}
	}

	public CompoundTag getStoredWerewolf() {
		return storedWerewolf;
	}

	public void setStoredWerewolf(CompoundTag storedWerewolf) {
		this.storedWerewolf = storedWerewolf;
	}

	public int getDespawnTimer() {
		return despawnTimer;
	}

	public void setDespawnTimer(int despawnTimer) {
		this.despawnTimer = despawnTimer;
	}
}
