/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class CursePoppetItem extends PoppetItem {
	public CursePoppetItem(Properties settings, boolean worksInShelf) {
		super(settings, worksInShelf);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		return stack.hasTag() && stack.getOrCreateTag().getBoolean("Cursed") && stack.getOrCreateTag().contains("OwnerUUID") ? ItemUtils.startUsingInstantly(world, user, hand) : super.use(world, user, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (!world.isClientSide && stack.hasTag()) {
			MinecraftServer server = world.getServer();
			if (server != null && stack.getOrCreateTag().contains("Cursed")) {
				UUID uuid = TaglockItem.getTaglockUUID(stack);
				for (ServerLevel serverWorld : server.getAllLevels()) {
					Entity entity = serverWorld.getEntity(uuid);
					if (entity instanceof LivingEntity livingEntity) {
						boolean failed = false;
						Curse curse = BWRegistries.CURSE.get(new ResourceLocation(stack.getOrCreateTag().getString("Curse")));
						PoppetData poppetData = BewitchmentAPI.getPoppet(world, BWObjects.CURSE_POPPET, entity);
						if (!poppetData.stack().isEmpty() && poppetData.stack().hasTag() && !poppetData.stack().getTag().getBoolean("Cursed")) {
							poppetData.stack().getTag().putString("Curse", BWRegistries.CURSE.getKey(curse).toString());
							poppetData.stack().getTag().putBoolean("Cursed", true);
							TaglockItem.removeTaglock(poppetData.stack());
							poppetData.update(world, true);
							failed = true;
						}
						if (curse != null) {
							if (!failed) {
								BWComponents.CURSES_COMPONENT.get(livingEntity).addCurse(new Curse.Instance(curse, 168000));
							}
							world.playSound(null, user.blockPosition(), BWSoundEvents.ENTITY_GENERIC_CURSE, SoundSource.PLAYERS, 1, 1);
							if (!(user instanceof Player && ((Player) user).isCreative())) {
								stack.shrink(1);
							}
							return stack;
						}
					}
				}
				if (user instanceof Player player) {
					player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.invalid_entity", stack.getOrCreateTag().getString("OwnerName")), true);
				}
			}
		}
		return stack;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return stack.hasTag() && stack.getOrCreateTag().getBoolean("Cursed") && stack.getOrCreateTag().contains("OwnerUUID") ? UseAnim.BOW : super.getUseAnimation(stack);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		super.appendHoverText(stack, world, tooltip, context);
		if (stack.hasTag() && stack.getOrCreateTag().contains("Curse")) {
			tooltip.add(Component.translatable("curse." + stack.getOrCreateTag().getString("Curse").replace(":", ".")).withStyle(ChatFormatting.DARK_RED));
		}
	}
}
