/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.dev;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

/**
 * CI-only client check, enabled with -Dbewitchment.clientSmokeTest=true (see build.gradle).
 * Once the client has finished loading resources it runs Mixin's audit, which applies every
 * pending mixin (client ones included) to its target class, then closes the game. Any mixin
 * that cannot apply fails the run instead of crashing a player's game later.
 */
public final class ClientSmokeTest {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static boolean done;

	private ClientSmokeTest() {
	}

	public static void registerIfEnabled() {
		if (Boolean.getBoolean("bewitchment.clientSmokeTest")) {
			MinecraftForge.EVENT_BUS.addListener(ClientSmokeTest::onClientTick);
		}
	}

	private static void onClientTick(TickEvent.ClientTickEvent event) {
		Minecraft client = Minecraft.getInstance();
		if (done || event.phase != TickEvent.Phase.END || client.getOverlay() != null || client.screen == null) {
			return;
		}
		done = true;
		LOGGER.info("Bewitchment client smoke test: resources loaded, auditing mixins");
		MixinEnvironment.getCurrentEnvironment().audit();
		LOGGER.info("BEWITCHMENT CLIENT SMOKE TEST PASSED");
		client.stop();
	}
}
