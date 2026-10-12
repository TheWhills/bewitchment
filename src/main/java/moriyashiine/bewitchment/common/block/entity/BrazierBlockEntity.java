/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.misc.BWCurios;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.recipe.CurseRecipe;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
public class BrazierBlockEntity extends BlockEntity implements Container, UsesAltarPower {
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

	private BlockPos altarPos = null;

	public IncenseRecipe incenseRecipe = null;
	public CurseRecipe curseRecipe = null;
	private int timer = 0;

	private boolean loaded = false, hasIncense;

	public BrazierBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.BRAZIER.get(), pos, state);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		saveAdditional(nbt);
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		if (nbt.contains("AltarPos")) {
			setAltarPos(BlockPos.of(nbt.getLong("AltarPos")));
		}
		inventory.clear();
		ContainerHelper.loadAllItems(nbt, inventory);
		timer = nbt.getInt("Timer");
		hasIncense = nbt.getBoolean("HasIncense");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if (getAltarPos() != null) {
			nbt.putLong("AltarPos", getAltarPos().asLong());
		}
		ContainerHelper.saveAllItems(nbt, inventory);
		nbt.putInt("Timer", timer);
		nbt.putBoolean("HasIncense", hasIncense);
	}

	public void sync() {
		if (level != null && !level.isClientSide) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	public BlockPos getAltarPos() {
		return altarPos;
	}

	@Override
	public void setAltarPos(BlockPos pos) {
		this.altarPos = pos;
	}

	public static void tick(Level world, BlockPos pos, BlockState state, BrazierBlockEntity blockEntity) {
		if (world != null) {
			if (!blockEntity.loaded) {
				if (!world.isClientSide && state.getValue(BlockStateProperties.LIT)) {
					blockEntity.incenseRecipe = world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.INCENSE_RECIPE_TYPE.get()).stream().filter(recipe -> recipe.matches(blockEntity, world)).findFirst().orElse(null);
					if (BWConfig.enableCurses) {
						blockEntity.curseRecipe = world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.CURSE_RECIPE_TYPE.get()).stream().filter(recipe -> recipe.matches(blockEntity, world)).findFirst().orElse(null);
					}
					blockEntity.hasIncense = blockEntity.incenseRecipe != null;
					blockEntity.setChanged();
					blockEntity.syncBrazier();
				}
				blockEntity.loaded = true;
			}
			if (blockEntity.timer < 0) {
				blockEntity.timer++;
				if (world.isClientSide) {
					if (world.random.nextBoolean()) {
						world.addParticle(blockEntity.hasIncense ? (ParticleOptions) BWParticleTypes.INCENSE_SMOKE.get() : ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5 + Mth.nextDouble(world.random, -0.2, 0.2), pos.getY() + (state.getValue(BlockStateProperties.HANGING) ? 0.4 : 1.25), pos.getZ() + 0.5 + Mth.nextDouble(world.random, -0.2, 0.2), 0, 0.05, 0);
					}
				} else if (blockEntity.timer == 0) {
					boolean clear = blockEntity.hasIncense;
					if (blockEntity.curseRecipe != null) {
						Player closestPlayer = world.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 24, false);
						if (blockEntity.altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(blockEntity.altarPos)).drain(blockEntity.curseRecipe.cost, false)) {
							if (closestPlayer != null) {
								Entity target = blockEntity.getTarget();
								if (target instanceof Player player && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.RAVEN.get() && world.random.nextBoolean()) {
									target = closestPlayer;
								}
								if (target instanceof LivingEntity livingEntity) {
									PoppetData poppetData = BewitchmentAPI.getPoppet(world, BWObjects.CURSE_POPPET.get(), target);
									if (!poppetData.stack().isEmpty() && poppetData.stack().hasTag() && !poppetData.stack().getTag().getBoolean("Cursed")) {
										poppetData.stack().getTag().putString("Curse", BWRegistries.CURSE.getKey(blockEntity.curseRecipe.curse).toString());
										poppetData.stack().getTag().putBoolean("Cursed", true);
										TaglockItem.removeTaglock(poppetData.stack());
										poppetData.update(world, true);
									} else {
										int duration = 168000;
										if (BewitchmentAPI.getFamiliar(closestPlayer) == BWEntityTypes.RAVEN.get()) {
											duration *= 2;
										}
										if (target instanceof Player player && BWCurios.isEquipped(player, BWObjects.NAZAR.get()) && BewitchmentAPI.drainMagic(player, 50, false)) {
											duration /= 2;
										}
										BWComponents.CURSES_COMPONENT.get(livingEntity).addCurse(new Curse.Instance(blockEntity.curseRecipe.curse, duration));
									}
									world.playSound(null, pos, BWSoundEvents.ENTITY_GENERIC_CURSE.get(), SoundSource.BLOCKS, 1, 1);
									clear = true;
								}
							} else {
								world.playSound(null, pos, BWSoundEvents.BLOCK_BRAZIER_FAIL.get(), SoundSource.BLOCKS, 1, 1);
								if (closestPlayer != null) {
									String entityName = "";
									for (int i = 0; i < blockEntity.getContainerSize(); i++) {
										ItemStack stack = blockEntity.getItem(i);
										if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
											entityName = TaglockItem.getTaglockName(stack);
											break;
										}
									}
									closestPlayer.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.invalid_entity", entityName), true);
								}
							}
						} else {
							if (closestPlayer != null) {
								world.playSound(null, pos, BWSoundEvents.BLOCK_BRAZIER_FAIL.get(), SoundSource.BLOCKS, 1, 1);
								closestPlayer.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.insufficent_altar_power"), true);
							}
						}
					}
					blockEntity.reset(clear);
					blockEntity.syncBrazier();
				}
			}
		}
	}

	@Override
	public int getContainerSize() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < getContainerSize(); i++) {
			if (getItem(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return ContainerHelper.removeItem(inventory, slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}

	@Override
	public boolean stillValid(Player player) {
		return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) < 16;
	}

	@Override
	public void clearContent() {
		inventory.clear();
	}

	public void syncBrazier() {
		sync();
	}

	private int getFirstEmptySlot() {
		for (int i = 0; i < getContainerSize(); i++) {
			if (getItem(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	public void onUse(Level world, BlockPos pos, Player player, InteractionHand hand) {
		if (!getBlockState().getValue(BlockStateProperties.WATERLOGGED)) {
			ItemStack stack = player.getItemInHand(hand);
			if (getBlockState().getValue(BlockStateProperties.LIT)) {
				world.setBlockAndUpdate(pos, getBlockState().setValue(BlockStateProperties.LIT, false));
				world.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 2);
				reset(hasIncense);
				syncBrazier();
			} else {
				if (stack.getItem() instanceof FlintAndSteelItem) {
					world.setBlockAndUpdate(pos, getBlockState().setValue(BlockStateProperties.LIT, true));
					world.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1, 1);
					stack.hurtAndBreak(1, player, stackUser -> stackUser.broadcastBreakEvent(hand));
					IncenseRecipe foundIncenseRecipe = world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.INCENSE_RECIPE_TYPE.get()).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
					if (foundIncenseRecipe != null) {
						incenseRecipe = foundIncenseRecipe;
						timer = -6000;
						hasIncense = true;
						syncBrazier();
					} else if (BWConfig.enableCurses) {
						CurseRecipe foundCurseRecipe = world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.CURSE_RECIPE_TYPE.get()).stream().filter(recipe -> recipe.matches(this, world)).findFirst().orElse(null);
						if (foundCurseRecipe != null && getTarget() != null) {
							curseRecipe = foundCurseRecipe;
							timer = -100;
							syncBrazier();
						}
					}
				} else if (!stack.isEmpty()) {
					int firstEmpty = getFirstEmptySlot();
					if (firstEmpty != -1) {
						setItem(firstEmpty, stack.split(1));
						syncBrazier();
					}
				} else {
					reset(hasIncense);
					syncBrazier();
				}
			}
		}
		setChanged();
	}

	private Entity getTarget() {
		if (level != null && !level.isClientSide) {
			for (int i = 0; i < getContainerSize(); i++) {
				Entity entity = BewitchmentAPI.getTaglockOwner(level, getItem(i));
				if (entity != null) {
					return entity;
				}
			}
		}
		return null;
	}

	private void reset(boolean clear) {
		if (level != null) {
			if (clear) {
				cleanInventory();
			}
			Containers.dropContents(level, worldPosition.above(getBlockState().getValue(BlockStateProperties.HANGING) ? 0 : 1), this);
			incenseRecipe = null;
			curseRecipe = null;
			timer = 0;
			hasIncense = false;
		}
	}

	private void cleanInventory() {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = getItem(i);
			if (stack.isDamageableItem()) {
				if (stack.hurt(1, level.random, null) && stack.getDamageValue() == stack.getMaxDamage()) {
					stack.shrink(1);
				}
			} else {
				Item item = stack.getItem();
				setItem(i, item.hasCraftingRemainingItem() ? new ItemStack(item.getCraftingRemainingItem()) : ItemStack.EMPTY);
			}
		}
	}
}
