package moriyashiine.bewitchment.client.event;

import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ClientChatEvent;

/** Client: chat typed while standing in a teleportation-mode witch cauldron is sent as a teleport request instead. */
public final class CauldronTeleportEvent {
	private CauldronTeleportEvent() {
	}

	public static void onClientChat(ClientChatEvent event) {
		String message = event.getMessage();
		Player player = Minecraft.getInstance().player;
		if (player != null && !message.startsWith("/")) {
			for (int i = 0; i < 1; i++) {
				if (player.level().getBlockEntity(player.blockPosition().below(i)) instanceof WitchCauldronBlockEntity witchCauldron && witchCauldron.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					moriyashiine.bewitchment.common.packet.CauldronTeleportPacket.send(witchCauldron.getBlockPos(), message);
					event.setCanceled(true);
					return;
				}
			}
		}
	}
}
