package moriyashiine.bewitchment.client.event;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.common.packet.TogglePressingForwardPacket;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

public class TransformationAbilityEvent implements ClientTickEvents.EndWorldTick {
	private int transformationAbilityCooldown = 0;

	@Override
	public void onEndTick(ClientLevel world) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			if (transformationAbilityCooldown > 0) {
				transformationAbilityCooldown--;
			} else if (BewitchmentClient.TRANSFORMATION_ABILITY.isDown()) {
				transformationAbilityCooldown = 20;
				moriyashiine.bewitchment.common.packet.TransformationAbilityPacket.send();
			}
			if (BWComponents.BROOM_USER_COMPONENT.get(player).isPressingForward()) {
				TogglePressingForwardPacket.send(false);
			}
			if (Minecraft.getInstance().options.keyUp.isDown() && player.getVehicle() instanceof BroomEntity) {
				TogglePressingForwardPacket.send(true);
			}
		}
	}
}
