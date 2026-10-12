package moriyashiine.bewitchment.client.event;

import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class CauldronTeleportEvent implements ClientSendMessageEvents.AllowChat {
	@Override
	public boolean allowSendChatMessage(String message) {
		if (!message.startsWith("/")) {
			Player player = Minecraft.getInstance().player;
			for (int i = 0; i < 1; i++) {
				if (player.level().getBlockEntity(player.blockPosition().below(i)) instanceof WitchCauldronBlockEntity witchCauldron && witchCauldron.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					moriyashiine.bewitchment.common.packet.CauldronTeleportPacket.send(witchCauldron.getBlockPos(), message);
					return false;
				}
			}
		}
		return true;
	}
}
