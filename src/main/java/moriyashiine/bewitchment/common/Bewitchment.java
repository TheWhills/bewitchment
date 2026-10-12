/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.common.event.BWCommonEvents;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.common.registry.*;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Bewitchment.MOD_ID)
public class Bewitchment {
	public static final String MOD_ID = "bewitchment";

	public Bewitchment() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		BWRegistration.register(modBus);
		BWNetwork.register();
		modBus.addListener(this::commonSetup);
		onInitialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> BewitchmentClient::init);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> BewitchmentAPI.registerAltarMapEntries(
				BWObjects.STONE_WITCH_ALTAR.get(),
				BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR.get(),
				BWObjects.PRISMARINE_WITCH_ALTAR.get(),
				BWObjects.NETHER_BRICK_WITCH_ALTAR.get(),
				BWObjects.BLACKSTONE_WITCH_ALTAR.get(),
				BWObjects.GOLDEN_WITCH_ALTAR.get(),
				BWObjects.END_STONE_WITCH_ALTAR.get(),
				BWObjects.OBSIDIAN_WITCH_ALTAR.get(),
				BWObjects.PURPUR_WITCH_ALTAR.get()));
	}

	private void onInitialize() {
		MidnightConfig.init(MOD_ID, BWConfig.class);
		BWCommonEvents.register(MinecraftForge.EVENT_BUS);
		// TODO(forge-port): Pehkui scale types, Terraform boats and biome modifications are ported in later steps.
		BWScaleTypes.init();
		BWBoatTypes.init();
		BWWorldGenerators.init();
	}

	public static ResourceLocation id(String value) {
		return new ResourceLocation(MOD_ID, value);
	}
}
