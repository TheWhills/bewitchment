/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import top.theillusivec4.curios.api.SlotContext;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.client.packet.SpawnSpecterBangleParticlesPacket;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpecterBangleItem extends AccessoryItem {
	public SpecterBangleItem(Properties settings) {
		super(settings);
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		LivingEntity entity = slotContext.entity();
		if (entity instanceof ServerPlayer player && entity.isShiftKeyDown() && BewitchmentAPI.drainMagic(player, 1, true)) {
			if (entity.getRandom().nextFloat() < 1 / 40f) {
				BewitchmentAPI.drainMagic(player, 1, false);
			}
			BWComponents.FULL_INVISIBILITY_COMPONENT.maybeGet(player).ifPresent(fullInvisibilityComponent -> {
				if (!fullInvisibilityComponent.isFullInvisible()) {
					fullInvisibilityComponent.setFullInvisible(true);
					entity.setInvisible(true);
				}
			});
			if (entity.getRandom().nextFloat() < 1 / 32f) {
				BWNetwork.sendToTrackingAndSelf(player, new SpawnSpecterBangleParticlesPacket(player));
			}
		}
	}
}
