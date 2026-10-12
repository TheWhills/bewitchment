/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.interfaces.Lockable;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWSigils;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class TaglockItem extends Item {
	public TaglockItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		Player player = context.getPlayer();
		boolean client = world.isClientSide;
		if (state.getBlock() instanceof BedBlock) {
			if (!client && player != null && player.isShiftKeyDown()) {
				MinecraftServer server = world.getServer();
				if (server != null) {
					if (state.getValue(BedBlock.PART) != BedPart.HEAD) {
						pos = pos.relative(state.getValue(BedBlock.FACING));
					}
					Player earliestSleeper = null;
					for (ServerPlayer playerEntity : server.getPlayerList().getPlayers()) {
						BlockPos bedPos = playerEntity.getRespawnPosition();
						if (bedPos != null && bedPos.equals(pos) && (earliestSleeper == null || playerEntity.getSleepTimer() < earliestSleeper.getSleepTimer())) {
							earliestSleeper = playerEntity;
						}
					}
					if (earliestSleeper != null) {
						return useTaglock(player, earliestSleeper, context.getHand(), false, true);
					}
				}
			}
			return InteractionResult.sidedSuccess(client);
		} else {
			ItemStack stack = context.getItemInHand();
			BlockEntity blockEntity = world.getBlockEntity(state.getBlock() instanceof DoorBlock && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos);
			if (blockEntity instanceof TaglockHolder taglockHolder) {
				if (player.getUUID().equals(taglockHolder.getOwner())) {
					int firstEmpty = taglockHolder.getFirstEmptySlot();
					if (firstEmpty != -1) {
						if (!client) {
							taglockHolder.getTaglockInventory().set(firstEmpty, stack.split(1));
							taglockHolder.syncTaglockHolder(blockEntity);
							blockEntity.setChanged();
						}
						return InteractionResult.sidedSuccess(client);
					}
				}
			} else if (blockEntity instanceof Lockable lockable) {
				if (hasTaglock(stack)) {
					if (player.getUUID().equals(lockable.getOwner()) && lockable.getLocked()) {
						UUID uuid = getTaglockUUID(stack);
						if (!lockable.getEntities().contains(uuid)) {
							if (!client) {
								if (lockable.getEntities().isEmpty()) {
									lockable.setModeOnWhitelist(true);
								}
								lockable.getEntities().add(uuid);
								if (!player.isCreative()) {
									stack.shrink(1);
								}
								lockable.syncLockable(blockEntity);
								blockEntity.setChanged();
							}
							return InteractionResult.sidedSuccess(client);
						}
					}
				}
			} else if (blockEntity instanceof SigilHolder sigilHolder) {
				if (hasTaglock(stack)) {
					if (!client) {
						if (sigilHolder.getSigil() != null) {
							UUID uuid = getTaglockUUID(stack);
							if (!sigilHolder.getEntities().contains(uuid)) {
								if (sigilHolder.getEntities().isEmpty()) {
									sigilHolder.setModeOnWhitelist(true);
								}
								sigilHolder.getEntities().add(uuid);
								if (!player.isCreative()) {
									stack.shrink(1);
								}
								sigilHolder.syncSigilHolder(blockEntity);
								blockEntity.setChanged();
							}
						}
					}
					return InteractionResult.sidedSuccess(client);
				}
			}
		}
		return super.useOn(context);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		if (entity instanceof Player) {
			return useTaglock(user, entity, hand, true, false);
		}
		return super.interactLivingEntity(stack, user, entity, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		if (hasTaglock(stack)) {
			tooltip.add(Component.literal(getTaglockName(stack)).withStyle(ChatFormatting.GRAY));
			CompoundTag nbt = stack.getTag();
			if (nbt.contains("UsedForScrying")) {
				if (nbt.contains("Failed")) {
					tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.failed").withStyle(ChatFormatting.DARK_GRAY));
				} else {
					boolean shifting = Screen.hasShiftDown();
					BlockPos pos = BlockPos.of(stack.getTag().getLong("LocationPos"));
					tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.location", pos.getX(), pos.getY(), pos.getZ(), stack.getTag().getString("LocationWorld")).withStyle(ChatFormatting.DARK_GRAY));
					tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.level", stack.getTag().getInt("Level")).withStyle(ChatFormatting.DARK_GRAY));
					MutableComponent curseTooltip = Component.translatable(Bewitchment.MOD_ID + ".tooltip.curse");
					ListTag cursesList = nbt.getList("Curses", 10);
					if (cursesList.isEmpty()) {
						curseTooltip.append(Component.translatable(Bewitchment.MOD_ID + ".tooltip.none"));
					} else if (!shifting) {
						curseTooltip.append(Component.translatable(Bewitchment.MOD_ID + ".tooltip.press_shift"));
					}
					tooltip.add(curseTooltip.withStyle(ChatFormatting.DARK_GRAY));
					if (shifting) {
						for (Tag element : cursesList) {
							CompoundTag curseCompound = (CompoundTag) element;
							MutableComponent curseText = Component.translatable("curse." + curseCompound.getString("Curse").replace(":", "."));
							int duration = curseCompound.getInt("Duration") / 24000;
							tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.curse_expanded", curseText, duration).withStyle(ChatFormatting.DARK_GRAY));
						}
					}
					MutableComponent contractTooltip = Component.translatable(Bewitchment.MOD_ID + ".tooltip.contract");
					ListTag contractList = nbt.getList("Contracts", 10);
					if (contractList.isEmpty()) {
						contractTooltip.append(Component.translatable(Bewitchment.MOD_ID + ".tooltip.none"));
					} else if (!shifting) {
						contractTooltip.append(Component.translatable(Bewitchment.MOD_ID + ".tooltip.press_shift"));
					}
					tooltip.add(contractTooltip.withStyle(ChatFormatting.DARK_GRAY));
					if (shifting) {
						for (Tag element : contractList) {
							CompoundTag contractCompound = (CompoundTag) element;
							MutableComponent curseText = Component.translatable("contract." + contractCompound.getString("Contract").replace(":", "."));
							int duration = contractCompound.getInt("Duration") / 24000;
							tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.curse_expanded", curseText, duration).withStyle(ChatFormatting.DARK_GRAY));
						}
					}
					tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.transformation", Component.translatable(stack.getTag().getString("Transformation"))).withStyle(ChatFormatting.DARK_GRAY));
					tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.familiar", Component.translatable("entity." + nbt.getString("Familiar").replace(":", "."))).withStyle(ChatFormatting.DARK_GRAY));
					tooltip.add(Component.translatable(Bewitchment.MOD_ID + ".tooltip.pledge", Component.translatable(stack.getTag().getString("Pledge"))).withStyle(ChatFormatting.DARK_GRAY));
				}
			}
		}
	}

	public static InteractionResult useTaglock(Player user, LivingEntity entity, InteractionHand hand, boolean checkVisibility, boolean bed) {
		ItemStack stack = user.getItemInHand(hand);
		if (entity.isAlive() && !entity.getType().is(BWTags.TAGLOCK_BLACKLIST) && !hasTaglock(stack)) {
			boolean failed = false;
			BlockPos sigilPos = BWUtil.getClosestBlockPos(entity.blockPosition(), 16, currentPos -> user.level().getBlockEntity(currentPos) instanceof SigilHolder && ((SigilHolder) user.level().getBlockEntity(currentPos)).getSigil() == BWSigils.SLIPPERY.get());
			if (sigilPos == null && bed) {
				sigilPos = BWUtil.getClosestBlockPos(user.blockPosition(), 16, currentPos -> user.level().getBlockEntity(currentPos) instanceof SigilHolder && ((SigilHolder) user.level().getBlockEntity(currentPos)).getSigil() == BWSigils.SLIPPERY.get());
			}
			if (sigilPos != null) {
				BlockEntity blockEntity = user.level().getBlockEntity(sigilPos);
				SigilHolder sigil = (SigilHolder) blockEntity;
				if (sigil.test(entity)) {
					sigil.setUses(sigil.getUses() - 1);
					blockEntity.setChanged();
					failed = true;
				}
			}
			if (!failed && checkVisibility && !entity.isSleeping()) {
				double targetYaw = entity.getYHeadRot() % 360;
				double userYaw = user.getYHeadRot() % 360;
				if (userYaw < 0) {
					userYaw += 360;
				}
				if (targetYaw < 0) {
					targetYaw += 360;
				}
				failed = Math.abs(targetYaw - userYaw) > 120;
			}
			if (failed) {
				if (entity instanceof Player player) {
					player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.taglock_fail", user.getDisplayName().getString()), false);
					if (entity.level().isClientSide) {
						entity.level().playSound(player, user, BWSoundEvents.ENTITY_GENERIC_PLING.get(), SoundSource.PLAYERS, 1, 1);
					}
				}
				return InteractionResult.FAIL;
			}
			boolean client = user.level().isClientSide;
			if (!client) {
				if (entity instanceof Mob mob) {
					mob.setPersistenceRequired();
				}
				BWUtil.addItemToInventoryAndConsume(user, hand, putTaglock(new ItemStack(BWObjects.TAGLOCK.get()), entity));
			} else {
				user.level().playSound(user, entity, BWSoundEvents.ENTITY_GENERIC_PLING.get(), SoundSource.PLAYERS, 1, 1);
			}
			return InteractionResult.sidedSuccess(client);
		}
		return InteractionResult.FAIL;
	}

	public static ItemStack putTaglock(ItemStack stack, Entity entity) {
		stack.getOrCreateTag().putUUID("OwnerUUID", entity.getUUID());
		stack.getOrCreateTag().putString("OwnerName", entity.getName().getString());
		stack.getOrCreateTag().putBoolean("FromPlayer", entity instanceof Player);
		return stack;
	}

	public static ItemStack copyTo(ItemStack from, ItemStack to) {
		if (hasTaglock(from)) {
			to.getOrCreateTag().putUUID("OwnerUUID", from.getOrCreateTag().getUUID("OwnerUUID"));
			to.getOrCreateTag().putString("OwnerName", from.getOrCreateTag().getString("OwnerName"));
			to.getOrCreateTag().putBoolean("FromPlayer", from.getOrCreateTag().getBoolean("FromPlayer"));
		}
		return to;
	}

	public static boolean hasTaglock(ItemStack stack) {
		return stack.hasTag() && stack.getOrCreateTag().contains("OwnerUUID");
	}

	public static void removeTaglock(ItemStack stack) {
		if (stack.hasTag()) {
			stack.getOrCreateTag().remove("OwnerUUID");
			stack.getOrCreateTag().remove("OwnerName");
			stack.getOrCreateTag().remove("FromPlayer");
		}
	}

	public static UUID getTaglockUUID(ItemStack stack) {
		if (hasTaglock(stack)) {
			return stack.getOrCreateTag().getUUID("OwnerUUID");
		}
		return null;
	}

	public static String getTaglockName(ItemStack stack) {
		if (hasTaglock(stack)) {
			return stack.getOrCreateTag().getString("OwnerName");
		}
		return "";
	}

	public static boolean isTaglockFromPlayer(ItemStack stack) {
		return hasTaglock(stack) && stack.getOrCreateTag().getBoolean("FromPlayer");
	}
}
