/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public abstract class BWHostileEntity extends Monster {
	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(BWHostileEntity.class, EntityDataSerializers.INT);

	protected BWHostileEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
		initializeVariant();
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
	}

	public void initializeVariant() {
		int variants = getVariants();
		if (variants > 1) {
			if (hasShiny()) {
				if (random.nextInt(8192) == 0) {
					entityData.set(VARIANT, 0);
				} else {
					entityData.set(VARIANT, random.nextInt(variants - 1) + 1);
				}
			} else {
				entityData.set(VARIANT, random.nextInt(variants));
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		if (getVariants() > 1) {
			entityData.define(VARIANT, 1);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (getVariants() > 1) {
			entityData.set(VARIANT, nbt.getInt("Variant"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if (getVariants() > 1) {
			nbt.putInt("Variant", entityData.get(VARIANT));
		}
	}

	protected abstract boolean hasShiny();

	public abstract int getVariants();

	public int getVariant() {
		if (this.getEntityData().get(BWHostileEntity.VARIANT) < 0) initializeVariant();
		return this.getEntityData().get(BWHostileEntity.VARIANT);
	}
}
