/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class PricklyBeltItem extends AccessoryItem {
	public PricklyBeltItem(Properties settings) {
		super(settings);
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
