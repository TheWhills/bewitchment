/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.boat;

import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/** A chest boat made of one of Bewitchment's woods (replaces Terraform's boat entities). */
public class BWChestBoat extends ChestBoat {
	private static final EntityDataAccessor<Integer> DATA_BW_TYPE = SynchedEntityData.defineId(BWChestBoat.class, EntityDataSerializers.INT);

	public BWChestBoat(EntityType<? extends ChestBoat> type, Level level) {
		super(type, level);
	}

	public BWChestBoat(Level level, double x, double y, double z, BWBoatType boatType) {
		this(BWEntityTypes.CHEST_BOAT.get(), level);
		setPos(x, y, z);
		xo = x;
		yo = y;
		zo = z;
		setBWType(boatType);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_BW_TYPE, BWBoatType.JUNIPER.ordinal());
	}

	public BWBoatType getBWType() {
		return BWBoatType.byId(entityData.get(DATA_BW_TYPE));
	}

	public void setBWType(BWBoatType boatType) {
		entityData.set(DATA_BW_TYPE, boatType.ordinal());
	}

	/** Named per wood, e.g. "Juniper Boat". */
	@Override
	protected Component getTypeName() {
		return Component.translatable("entity.bewitchment." + getBWType().getName() + "_chest_boat");
	}

	@Override
	public Item getDropItem() {
		return getBWType().getChestBoatItem();
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("BewitchmentType", getBWType().getName());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		for (BWBoatType boatType : BWBoatType.values()) {
			if (boatType.getName().equals(tag.getString("BewitchmentType"))) {
				setBWType(boatType);
			}
		}
	}
}
