/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ScepterItem extends Item {
	public ScepterItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (!PotionUtils.getCustomEffects(user.getItemInHand(hand)).isEmpty()) {
			return ItemUtils.startUsingInstantly(world, user, hand);
		}
		return InteractionResultHolder.fail(user.getItemInHand(hand));
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem().equals(Items.NETHERITE_INGOT);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (user instanceof Player player) {
			if (!world.isClientSide && BewitchmentAPI.drainMagic(player, 2, false)) {
				ThrownPotion potion = new ThrownPotion(world, user);
				List<MobEffectInstance> effects = PotionUtils.getCustomEffects(stack);
				ItemStack potionStack = PotionUtils.setCustomEffects(new ItemStack(Items.SPLASH_POTION), effects);
				potionStack.getOrCreateTag().putInt("CustomPotionColor", PotionUtils.getColor(effects));
				if (stack.getOrCreateTag().contains("PolymorphUUID")) {
					potionStack.getOrCreateTag().putUUID("PolymorphUUID", stack.getOrCreateTag().getUUID("PolymorphUUID"));
					potionStack.getOrCreateTag().putString("PolymorphName", stack.getOrCreateTag().getString("PolymorphName"));
				}
				potion.setItem(potionStack);
				potion.shootFromRotation(user, user.getXRot(), user.getYRot(), -20, 0.5f, 1);
				world.addFreshEntity(potion);
				world.playSound(null, user.blockPosition(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 1, 1);
				if (!player.isCreative()) {
					stack.getOrCreateTag().putInt("PotionUses", stack.getOrCreateTag().getInt("PotionUses") - 1);
					if (stack.getOrCreateTag().getInt("PotionUses") <= 0) {
						if (stack.getOrCreateTag().contains("PolymorphUUID")) {
							potionStack.getOrCreateTag().remove("PolymorphUUID");
							potionStack.getOrCreateTag().remove("PolymorphName");
						}
						stack.getOrCreateTag().put("CustomPotionEffects", new ListTag());
					}

					stack.hurtAndBreak(1, user, stackUser -> stackUser.broadcastBreakEvent(user.getUsedItemHand()));
				}
			}
		}
		return stack;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return !PotionUtils.getCustomEffects(stack).isEmpty() ? UseAnim.BOW : UseAnim.NONE;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 16;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		int uses = 0;
		if (stack.hasTag()) {
			uses = stack.getTag().getInt("PotionUses");
		}
		tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.uses_left", uses).withStyle(ChatFormatting.GRAY));
		PotionUtils.addPotionTooltip(stack, tooltip, 1);
	}
}
