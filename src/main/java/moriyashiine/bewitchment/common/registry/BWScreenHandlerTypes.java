/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.client.screen.BaphometScreenHandler;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BWScreenHandlerTypes {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Bewitchment.MOD_ID);

	public static final RegistryObject<MenuType<DemonScreenHandler>> DEMON_SCREEN_HANDLER = MENUS.register("demon_screen", () -> new MenuType<>((syncId, playerInventory) -> new DemonScreenHandler(syncId, new DemonScreenHandler.DemonMerchantImpl()), FeatureFlags.VANILLA_SET));
	public static final RegistryObject<MenuType<BaphometScreenHandler>> BAPHOMET_SCREEN_HANDLER = MENUS.register("baphomet_screen", () -> new MenuType<>((syncId, playerInventory) -> new BaphometScreenHandler(syncId, new DemonScreenHandler.DemonMerchantImpl()), FeatureFlags.VANILLA_SET));
}
