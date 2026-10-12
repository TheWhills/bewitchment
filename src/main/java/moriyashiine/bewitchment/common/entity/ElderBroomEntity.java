/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ElderBroomEntity extends BroomEntity {
	private BlockPos originalPos = null;
	private String originalWorld = null;

	public ElderBroomEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("OriginalPos")) {
			originalPos = BlockPos.of(nbt.getLong("OriginalPos"));
			originalWorld = nbt.getString("OriginalWorld");
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if (originalPos != null) {
			nbt.putLong("OriginalPos", originalPos.asLong());
			nbt.putString("OriginalWorld", originalWorld);
		}
	}

	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		originalPos = blockPosition();
		originalWorld = level().dimension().toString();
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
		Vec3 value = super.getDismountLocationForPassenger(passenger);
		if (originalPos != null && level().dimension().toString().equals(originalWorld)) {
			double x = originalPos.getX() + 0.5;
			double y = originalPos.getY();
			double z = originalPos.getZ() + 0.5;
			value = new Vec3(x, y, z);
			BWUtil.teleport(this, x, y, z, false);
			originalPos = null;
			originalWorld = null;
		}
		return value;
	}
}
