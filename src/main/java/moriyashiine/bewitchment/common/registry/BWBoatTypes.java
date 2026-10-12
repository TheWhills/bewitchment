/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class BWBoatTypes {
	public static void init() {
		registerBoat("juniper", BWObjects.JUNIPER_PLANKS.get().asItem());
		registerBoat("cypress", BWObjects.CYPRESS_PLANKS.get().asItem());
		registerBoat("elder", BWObjects.ELDER_PLANKS.get().asItem());
		registerBoat("dragons_blood", BWObjects.DRAGONS_BLOOD_PLANKS.get().asItem());
	}

	private static void registerBoat(String name, Item planks) {
		ResourceLocation boatId = Bewitchment.id(name + "_boat");
		ResourceKey<TerraformBoatType> key = TerraformBoatTypeRegistry.createKey(boatId);
		Item boat = TerraformBoatItemHelper.registerBoatItem(boatId, key, false);
		Item chest_boat = TerraformBoatItemHelper.registerBoatItem(Bewitchment.id(name + "_chest_boat"), key, true);
		TerraformBoatType boatType = new TerraformBoatType.Builder()
				.item(boat)
				.chestItem(chest_boat)
				.planks(planks)
				.build();
		Registry.register(TerraformBoatTypeRegistry.INSTANCE, key, boatType);
		BWObjects.BOATS.add(boat);
		BWObjects.BOATS.add(chest_boat);
	}
}
