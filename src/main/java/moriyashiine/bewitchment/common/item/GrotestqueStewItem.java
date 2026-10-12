/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.component.PledgeComponent;
import moriyashiine.bewitchment.api.entity.Pledgeable;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class GrotestqueStewItem extends Item {
	public GrotestqueStewItem(Properties settings) {
		super(settings);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (!world.isClientSide && user instanceof Player player) {
			PledgeComponent pledgeComponent = BWComponents.PLEDGE_COMPONENT.get(player);
			if (pledgeComponent.getPledge().equals(BWPledges.NONE)) {
				LivingEntity closest = null;
				for (LivingEntity livingEntity : world.getEntitiesOfClass(LivingEntity.class, new AABB(user.blockPosition()).inflate(8), foundEntity -> foundEntity.isAlive() && foundEntity instanceof Pledgeable)) {
					if (closest == null || user.distanceTo(livingEntity) < user.distanceTo(closest)) {
						closest = livingEntity;
					}
				}
				if (closest instanceof Pledgeable pledgeable) {
					pledgeComponent.setPledge(pledgeable.getPledgeID());
					pledgeable.getPledgedPlayerUUIDs().add(user.getUUID());
					world.playSound(null, user.blockPosition(), BWSoundEvents.ENTITY_GENERIC_PLEDGE, SoundSource.PLAYERS, 1, 1);
				}
			}
		}
		return Items.MUSHROOM_STEW.finishUsingItem(stack, world, user);
	}
}
