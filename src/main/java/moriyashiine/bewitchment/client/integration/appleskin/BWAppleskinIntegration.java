/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.appleskin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import squeek.appleskin.api.event.HUDOverlayEvent;

/** Hides AppleSkin's saturation and exhaustion overlays for vampires, who live on blood instead. Only loaded when AppleSkin is present. */
public final class BWAppleskinIntegration {
	private BWAppleskinIntegration() {
	}

	public static void register() {
		MinecraftForge.EVENT_BUS.addListener((HUDOverlayEvent.Saturation event) -> {
			if (BewitchmentAPI.isVampire(Minecraft.getInstance().player, true)) {
				event.setCanceled(true);
			}
		});
		MinecraftForge.EVENT_BUS.addListener((HUDOverlayEvent.Exhaustion event) -> {
			if (BewitchmentAPI.isVampire(Minecraft.getInstance().player, true)) {
				event.setCanceled(true);
			}
		});
	}
}
