/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.item.BroomItem;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
import java.util.function.Supplier;

public class DragonsBloodBroomItem extends BroomItem {
	public DragonsBloodBroomItem(Properties settings, Supplier<? extends EntityType<?>> broom) {
		super(settings, broom);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		if (stack.hasTag() && stack.getTag().contains("Sigil")) {
			tooltip.add(Component.translatable("sigil." + stack.getTag().getString("Sigil").replace(":", ".")).withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.uses_left", stack.getTag().getInt("Uses")).withStyle(ChatFormatting.GRAY));
		}
	}
}
