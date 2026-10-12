/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("ConstantConditions")
public class ChalkItem extends Item {
	private final Block glyph;

	public ChalkItem(Properties settings, Block glyph) {
		super(settings);
		this.glyph = glyph;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		boolean client = world.isClientSide;
		BlockPlaceContext placementContext = new BlockPlaceContext(context);
		if (!world.getBlockState(pos).canBeReplaced(placementContext)) {
			pos = pos.relative(context.getClickedFace());
		}
		if (!world.getBlockState(pos).canBeReplaced(placementContext)) {
			return InteractionResult.PASS;
		}
		BlockState state = glyph.getStateForPlacement(placementContext);
		if (state.canSurvive(world, pos)) {
			if (!client) {
				Player player = context.getPlayer();
				ItemStack stack = context.getItemInHand();
				world.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1, Mth.nextFloat(world.random, 0.8f, 1.2f));
				world.setBlockAndUpdate(pos, state);
				if (player instanceof ServerPlayer serverPlayer) {
					CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
					stack.hurtAndBreak(1, player, stackUser -> stackUser.broadcastBreakEvent(context.getHand()));
				}
			}
			return InteractionResult.sidedSuccess(client);
		}
		return super.useOn(context);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public String getDescriptionId() {
		return glyph.getDescriptionId();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		if (stack.hasTag() && stack.getOrCreateTag().contains("InnerCircle")) {
			tooltip.add(Component.translatable("bewitchment.tooltip.inner_circle", Component.translatable(stack.getOrCreateTag().getString("InnerCircle"))).withStyle(ChatFormatting.GRAY));
			if (stack.getOrCreateTag().contains("OuterCircle")) {
				tooltip.add(Component.translatable("bewitchment.tooltip.outer_circle", Component.translatable(stack.getOrCreateTag().getString("OuterCircle"))).withStyle(ChatFormatting.GRAY));
			}
			tooltip.add(Component.translatable("bewitchment.tooltip.cost", stack.getOrCreateTag().getInt("Cost")).withStyle(ChatFormatting.GRAY));
			if (stack.getOrCreateTag().contains("RunningTime")) {
				tooltip.add(Component.translatable("bewitchment.tooltip.running_time", stack.getOrCreateTag().getInt("RunningTime") / 20f).withStyle(ChatFormatting.GRAY));
			}
		}
	}
}
