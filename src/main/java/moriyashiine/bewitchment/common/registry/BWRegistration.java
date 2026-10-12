/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Wires every Bewitchment registry to the mod event bus. Called once from the
 * {@code @Mod} constructor; replaces the Fabric-era {@code BW*.init()} calls.
 */
public final class BWRegistration {
	private BWRegistration() {
	}

	public static void register(IEventBus modBus) {
		// Custom registries must be created before anything registers into them.
		BWRegistries.register(modBus);

		BWObjects.register(modBus);
		BWBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modBus);
		BWEntityTypes.ENTITY_TYPES.register(modBus);
		BWStatusEffects.STATUS_EFFECTS.register(modBus);
		BWEnchantments.ENCHANTMENTS.register(modBus);
		BWSoundEvents.SOUND_EVENTS.register(modBus);
		BWParticleTypes.PARTICLE_TYPES.register(modBus);
		BWRecipeTypes.RECIPE_SERIALIZERS.register(modBus);
		BWRecipeTypes.RECIPE_TYPES.register(modBus);
		BWScreenHandlerTypes.MENUS.register(modBus);

		BWRitualFunctions.RITUAL_FUNCTIONS.register(modBus);
		BWSigils.SIGILS.register(modBus);
		BWFortunes.FORTUNES.register(modBus);
		BWTransformations.TRANSFORMATIONS.register(modBus);
		BWContracts.CONTRACTS.register(modBus);
		BWCurses.CURSES.register(modBus);

		modBus.addListener(BWEntityTypes::registerAttributes);
		modBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(BWObjects::commonSetup));

		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(BWObjects::onToolModification);
		forgeBus.addListener(BWObjects::onFurnaceFuel);
	}
}
