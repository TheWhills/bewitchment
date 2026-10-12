/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BottleOfBloodItem extends Item {
	public BottleOfBloodItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		return Items.POTION.use(world, user, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (BewitchmentAPI.isVampire(user, true)) {
			BWComponents.BLOOD_COMPONENT.get(user).fillBlood(20, false);
		} else {
			user.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
			user.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
		}
		return Items.POTION.finishUsingItem(stack, world, user);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return Items.POTION.getUseAnimation(stack);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return Items.POTION.getUseDuration(stack);
	}
}
