/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class WaystoneItem extends Item {
	public WaystoneItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		boolean client = world.isClientSide;
		if (!client) {
			context.getItemInHand().getOrCreateTag().putLong("LocationPos", context.getClickedPos().relative(context.getClickedFace()).asLong());
			context.getItemInHand().getOrCreateTag().putString("LocationWorld", world.dimension().location().toString());
		}
		return InteractionResult.sidedSuccess(client);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		if (stack.hasTag() && stack.getTag().contains("LocationPos")) {
			BlockPos pos = BlockPos.of(stack.getTag().getLong("LocationPos"));
			tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.location", pos.getX(), pos.getY(), pos.getZ(), stack.getTag().getString("LocationWorld")).withStyle(ChatFormatting.GRAY));
		}
	}
}
