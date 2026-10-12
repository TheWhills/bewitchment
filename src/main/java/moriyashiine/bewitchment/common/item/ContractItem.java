/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ContractItem extends Item {
	public ContractItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, user, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (!world.isClientSide && stack.hasTag() && user instanceof Player player) {
			Contract contract = BWRegistries.CONTRACT.get(new ResourceLocation(stack.getOrCreateTag().getString("Contract")));
			if (contract != null) {
				BWComponents.CONTRACTS_COMPONENT.get(player).addContract(new Contract.Instance(contract, stack.getTag().getInt("Duration"), 0));
				world.playSound(null, player.blockPosition(), BWSoundEvents.ITEM_CONTRACT_USE.get(), SoundSource.PLAYERS, 1, 1);
				if (!player.isCreative()) {
					stack.shrink(1);
				}
				return stack;
			}
		}
		return stack;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		if (stack.hasTag() && stack.getOrCreateTag().contains("Contract")) {
			tooltip.add(Component.translatable("contract." + stack.getOrCreateTag().getString("Contract").replace(":", ".")).withStyle(ChatFormatting.DARK_RED));
			tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.days", stack.getOrCreateTag().getInt("Duration") / 24000).withStyle(ChatFormatting.DARK_RED));
		}
	}
}
