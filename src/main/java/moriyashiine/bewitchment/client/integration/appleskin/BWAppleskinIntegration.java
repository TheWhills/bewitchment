/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.appleskin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.client.Minecraft;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.HUDOverlayEvent;

public class BWAppleskinIntegration implements AppleSkinApi {
	@Override
	public void registerEvents() {
		HUDOverlayEvent.Saturation.EVENT.register(saturation -> {
			if (BewitchmentAPI.isVampire(Minecraft.getInstance().player, true)) {
				saturation.isCanceled = true;
			}
		});
		HUDOverlayEvent.Exhaustion.EVENT.register(exhaustion -> {
			if (BewitchmentAPI.isVampire(Minecraft.getInstance().player, true)) {
				exhaustion.isCanceled = true;
			}
		});
	}
}
