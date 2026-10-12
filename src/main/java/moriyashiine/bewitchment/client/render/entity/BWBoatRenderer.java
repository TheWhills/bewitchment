/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity;

import com.mojang.datafixers.util.Pair;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.boat.BWBoat;
import moriyashiine.bewitchment.common.entity.boat.BWBoatType;
import moriyashiine.bewitchment.common.entity.boat.BWChestBoat;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.EnumMap;
import java.util.Map;

/** Vanilla boat rendering with Bewitchment wood textures (uses Forge's getModelWithLocation hook). */
public class BWBoatRenderer extends BoatRenderer {
	private final Map<BWBoatType, Pair<ResourceLocation, ListModel<Boat>>> models = new EnumMap<>(BWBoatType.class);

	public BWBoatRenderer(EntityRendererProvider.Context context, boolean chest) {
		super(context, chest);
		for (BWBoatType type : BWBoatType.values()) {
			ResourceLocation texture = Bewitchment.id("textures/entity/" + (chest ? "chest_boat/" : "boat/") + type.getName() + "_boat.png");
			ListModel<Boat> model = chest ? new ChestBoatModel(context.bakeLayer(layer(type, true))) : new BoatModel(context.bakeLayer(layer(type, false)));
			models.put(type, Pair.of(texture, model));
		}
	}

	public static ModelLayerLocation layer(BWBoatType type, boolean chest) {
		return new ModelLayerLocation(Bewitchment.id((chest ? "chest_boat/" : "boat/") + type.getName()), "main");
	}

	@Override
	public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
		BWBoatType type = boat instanceof BWBoat bwBoat ? bwBoat.getBWType() : boat instanceof BWChestBoat bwChestBoat ? bwChestBoat.getBWType() : BWBoatType.JUNIPER;
		return models.get(type);
	}
}
