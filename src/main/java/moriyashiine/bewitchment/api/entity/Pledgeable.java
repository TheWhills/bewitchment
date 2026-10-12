/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.entity;

import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import java.util.Collection;
import java.util.UUID;

public interface Pledgeable {
	String getPledgeID();

	Collection<UUID> getPledgedPlayerUUIDs();

	EntityType<?> getMinionType();

	Collection<MobEffectInstance> getMinionBuffs();

	int getTimeSinceLastAttack();

	void setTimeSinceLastAttack(int timeSinceLastAttack);

	default void summonMinions(Mob pledgeable) {
		if (!pledgeable.level().isClientSide && pledgeable.level().getEntities(getMinionType(), new AABB(pledgeable.blockPosition()).inflate(32), entity -> entity instanceof Mob mobEntity && !entity.isRemoved() && pledgeable.getUUID().equals(BWComponents.MINION_COMPONENT.get(mobEntity).getMaster())).size() < 3) {
			for (int i = 0; i < Mth.nextInt(pledgeable.getRandom(), 2, 3); i++) {
				Entity entity = getMinionType().create(pledgeable.level());
				if (entity instanceof Mob mobEntity) {
					BWUtil.attemptTeleport(entity, pledgeable.blockPosition().above(), 3, true);
					mobEntity.finalizeSpawn((ServerLevelAccessor) pledgeable.level(), pledgeable.level().getCurrentDifficultyAt(pledgeable.blockPosition()), MobSpawnType.EVENT, null, null);
					entity.setXRot(pledgeable.getRandom().nextFloat() * 360);
					mobEntity.setTarget(pledgeable.getTarget());
					BWComponents.MINION_COMPONENT.get(mobEntity).setMaster(pledgeable.getUUID());
					mobEntity.setPersistenceRequired();
					getMinionBuffs().forEach(mobEntity::addEffect);
					pledgeable.level().addFreshEntity(entity);
				}
			}
		}
	}

	default void fromNbtPledgeable(CompoundTag tag) {
		ListTag pledgedPlayerUUIDsList = tag.getList("PledgedPlayerUUIDs", Tag.TAG_COMPOUND);
		for (int i = 0; i < pledgedPlayerUUIDsList.size(); i++) {
			getPledgedPlayerUUIDs().add(pledgedPlayerUUIDsList.getCompound(i).getUUID("UUID"));
		}
		setTimeSinceLastAttack(tag.getInt("TimeSinceLastAttack"));
	}

	default void toNbtPledgeable(CompoundTag tag) {
		ListTag pledgedPlayerUUIDsList = new ListTag();
		for (UUID uuid : getPledgedPlayerUUIDs()) {
			CompoundTag pledgedPlayerUUIDCompound = new CompoundTag();
			pledgedPlayerUUIDCompound.putUUID("UUID", uuid);
			pledgedPlayerUUIDsList.add(pledgedPlayerUUIDCompound);
		}
		tag.put("PledgedPlayerUUIDs", pledgedPlayerUUIDsList);
		tag.putInt("TimeSinceLastAttack", getTimeSinceLastAttack());
	}
}
