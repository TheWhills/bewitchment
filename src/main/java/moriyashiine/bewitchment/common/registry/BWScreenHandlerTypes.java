/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.client.screen.BaphometScreenHandler;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class BWScreenHandlerTypes {
	public static final MenuType<DemonScreenHandler> DEMON_SCREEN_HANDLER = new MenuType<>((syncId, playerInventory) -> new DemonScreenHandler(syncId, new DemonScreenHandler.DemonMerchantImpl()), FeatureFlags.VANILLA_SET);
	public static final MenuType<BaphometScreenHandler> BAPHOMET_SCREEN_HANDLER = new MenuType<>((syncId, playerInventory) -> new BaphometScreenHandler(syncId, new DemonScreenHandler.DemonMerchantImpl()), FeatureFlags.VANILLA_SET);

	public static void init() {
		Registry.register(BuiltInRegistries.MENU, Bewitchment.id("demon_screen"), DEMON_SCREEN_HANDLER);
		Registry.register(BuiltInRegistries.MENU, Bewitchment.id("baphomet_screen"), BAPHOMET_SCREEN_HANDLER);
	}
}
