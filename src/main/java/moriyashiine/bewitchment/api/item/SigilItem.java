/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
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
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class SigilItem extends Item {
	private final Supplier<? extends Sigil> sigil;

	/** The sigil is a supplier because items register before Bewitchment's sigil registry. */
	public SigilItem(Properties settings, Supplier<? extends Sigil> sigil) {
		super(settings);
		this.sigil = sigil;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		boolean client = world.isClientSide;
		BlockState state = world.getBlockState(pos);
		if (!getSigil().active) {
			BlockPlaceContext placementContext = new BlockPlaceContext(context);
			if (!state.canBeReplaced(placementContext)) {
				pos = pos.relative(context.getClickedFace());
			}
			if (!world.getBlockState(pos).canBeReplaced(placementContext)) {
				return InteractionResult.PASS;
			}
			BlockState sigilState = BWObjects.SIGIL.get().getStateForPlacement(placementContext);
			if (sigilState.canSurvive(world, pos)) {
				if (!client) {
					world.playSound(null, pos, sigilState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1, Mth.nextFloat(world.random, 0.8f, 1.2f));
					world.setBlockAndUpdate(pos, sigilState);
					BlockEntity blockEntity = world.getBlockEntity(pos);
					SigilHolder sigilHolder = (SigilHolder) blockEntity;
					sigilHolder.setSigil(getSigil());
					sigilHolder.setUses(getSigil().uses * (BewitchmentAPI.getFamiliar(player) == BWEntityTypes.SNAKE.get() ? 2 : 1));
					sigilHolder.setOwner(player.getUUID());
					sigilHolder.syncSigilHolder(blockEntity);
					blockEntity.setChanged();
					if (player instanceof ServerPlayer serverPlayerEntity) {
						CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayerEntity, pos, stack);
						if (!player.isCreative()) {
							stack.shrink(1);
						}
					}
				}
				return InteractionResult.sidedSuccess(client);
			}
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SigilHolder sigilHolder && state.getBlock() != BWObjects.SIGIL.get()) {
				if (state.getBlock() instanceof DoorBlock && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
					blockEntity = world.getBlockEntity(pos.below());
				}
				if (!client) {
					sigilHolder.setSigil(getSigil());
					sigilHolder.setUses(getSigil().uses * (BewitchmentAPI.getFamiliar(player) == BWEntityTypes.SNAKE.get() ? 2 : 1));
					sigilHolder.setOwner(player.getUUID());
					sigilHolder.syncSigilHolder(blockEntity);
					blockEntity.setChanged();
					if (!player.isCreative()) {
						stack.shrink(1);
					}
				}
				return InteractionResult.sidedSuccess(client);
			}
		}
		return super.useOn(context);
	}

	@Override
	public String getDescriptionId() {
		return BWObjects.SIGIL.get().getDescriptionId();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		tooltip.add(Component.translatable("sigil." + BWRegistries.SIGIL.getKey(getSigil()).toString().replace(":", ".")).withStyle(ChatFormatting.GRAY));
	}

	public Sigil getSigil() {
		return sigil.get();
	}
}
