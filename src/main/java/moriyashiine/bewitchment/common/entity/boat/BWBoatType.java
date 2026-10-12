/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.boat;

import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/** Bewitchment's boat woods. Stored on boats by ordinal, so only append new values. */
public enum BWBoatType {
	JUNIPER("juniper", () -> BWObjects.JUNIPER_PLANKS.get(), () -> BWObjects.JUNIPER_BOAT, () -> BWObjects.JUNIPER_CHEST_BOAT),
	CYPRESS("cypress", () -> BWObjects.CYPRESS_PLANKS.get(), () -> BWObjects.CYPRESS_BOAT, () -> BWObjects.CYPRESS_CHEST_BOAT),
	ELDER("elder", () -> BWObjects.ELDER_PLANKS.get(), () -> BWObjects.ELDER_BOAT, () -> BWObjects.ELDER_CHEST_BOAT),
	DRAGONS_BLOOD("dragons_blood", () -> BWObjects.DRAGONS_BLOOD_PLANKS.get(), () -> BWObjects.DRAGONS_BLOOD_BOAT, () -> BWObjects.DRAGONS_BLOOD_CHEST_BOAT);

	private final String name;
	private final Supplier<? extends Block> planks;
	private final Supplier<RegistryObject<Item>> boat;
	private final Supplier<RegistryObject<Item>> chestBoat;

	BWBoatType(String name, Supplier<? extends Block> planks, Supplier<RegistryObject<Item>> boat, Supplier<RegistryObject<Item>> chestBoat) {
		this.name = name;
		this.planks = planks;
		this.boat = boat;
		this.chestBoat = chestBoat;
	}

	public String getName() {
		return name;
	}

	public Block getPlanks() {
		return planks.get();
	}

	public Item getBoatItem() {
		return boat.get().get();
	}

	public Item getChestBoatItem() {
		return chestBoat.get().get();
	}

	public static BWBoatType byId(int id) {
		BWBoatType[] values = values();
		return values[id < 0 || id >= values.length ? 0 : id];
	}
}
