/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import top.theillusivec4.curios.api.SlotContext;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DruidBandItem extends AccessoryItem {
	public DruidBandItem(Properties settings) {
		super(settings);
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		LivingEntity entity = slotContext.entity();
		if (entity instanceof Player player && !entity.level().isClientSide && entity.tickCount % 10 == 0 & entity.level().getBlockState(entity.blockPosition().below()).is(BWTags.NATURAL_TERRAIN)) {
			MobEffectInstance speed = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0, true, false);
			MobEffectInstance regeneration = new MobEffectInstance(MobEffects.REGENERATION, 300, 0, true, false);
			boolean canApply = entity.canBeAffected(speed) && !entity.hasEffect(MobEffects.MOVEMENT_SPEED);
			canApply |= entity.canBeAffected(regeneration) && !entity.hasEffect(MobEffects.REGENERATION);
			if (canApply && BewitchmentAPI.drainMagic(player, 1, false)) {
				entity.addEffect(speed);
				entity.addEffect(regeneration);
			}
		}
	}
}
