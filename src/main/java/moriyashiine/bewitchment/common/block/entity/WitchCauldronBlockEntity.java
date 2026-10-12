/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.recipe.CauldronBrewingRecipe;
import moriyashiine.bewitchment.common.recipe.OilRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class WitchCauldronBlockEntity extends BlockEntity implements Container, UsesAltarPower {
	private AABB box;

	private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

	private BlockPos altarPos = null;

	public OilRecipe oilRecipe = null;

	public Mode mode = Mode.NORMAL;

	public String name;

	public int color = 0x3f76e4, heatTimer = 0;

	private boolean loaded = false;

	public WitchCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.WITCH_CAULDRON, pos, state);
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
		ContainerHelper.loadAllItems(nbt, inventory);
		mode = Mode.valueOf(nbt.getString("Mode"));
		if (nbt.contains("Name")) {
			name = nbt.getString("Name");
		}
		if (nbt.contains("Color")) {
			color = nbt.getInt("Color");
		}
		heatTimer = nbt.getInt("HeatTimer");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if (getAltarPos() != null) {
			nbt.putLong("AltarPos", getAltarPos().asLong());
		}
		ContainerHelper.saveAllItems(nbt, inventory);
		nbt.putString("Mode", mode.name);
		if (name != null) {
			nbt.putString("Name", name);
		}
		nbt.putInt("Color", color);
		nbt.putInt("HeatTimer", heatTimer);
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
		altarPos = pos;
	}

	public static void tick(Level world, BlockPos pos, BlockState state, WitchCauldronBlockEntity blockEntity) {
		if (world != null) {
			if (!blockEntity.loaded) {
				blockEntity.setChanged();
				blockEntity.box = new AABB(pos).deflate(0.75);
				blockEntity.oilRecipe = world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.OIL_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(blockEntity, world)).findFirst().orElse(null);
				blockEntity.loaded = true;
			}
			blockEntity.heatTimer = Mth.clamp(blockEntity.heatTimer + (state.getValue(BlockStateProperties.LIT) && state.getValue(BWProperties.LEVEL) > 0 ? 1 : -1), 0, 160);
			if (!world.isClientSide) {
				if (blockEntity.heatTimer >= 60 && state.getValue(BWProperties.LEVEL) > 0) {
					if (world.random.nextFloat() <= 0.075f) {
						world.playSound(null, pos, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 1 / 3f, blockEntity.mode == Mode.FAILED ? 0.5f : 1);
					}
					if (world.getGameTime() % 5 == 0) {
						world.getEntities(EntityType.ITEM, blockEntity.box, entity -> true).forEach(itemEntity -> {
							if (itemEntity.getItem().getItem() == BWObjects.WOOD_ASH || state.getValue(BWProperties.LEVEL) == 3) {
								world.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1 / 3f, 1);
								ItemStack stack = itemEntity.getItem();
								if (stack.getItem().hasCraftingRemainingItem()) {
									ItemEntity remainder = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(stack.getItem().getCraftingRemainingItem()));
									remainder.setDeltaMovement(Vec3.ZERO);
									remainder.setNoGravity(true);
									world.addFreshEntity(remainder);
								}
								blockEntity.mode = blockEntity.insertStack(stack.split(1));
								blockEntity.setChanged();
								blockEntity.syncCauldron();
							}
						});
					}
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

	public void syncCauldron() {
		sync();
	}

	public void setColor(int color) {
		if (level != null) {
			this.color = color;
		}
	}

	public Mode reset() {
		if (level != null) {
			setColor(0x3f76e4);
			clearContent();
			oilRecipe = null;
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BWProperties.LEVEL, 0));
		}
		return Mode.NORMAL;
	}

	public Mode fail() {
		setColor(0x6b4423);
		return Mode.FAILED;
	}

	private int getFirstEmptySlot() {
		for (int i = 0; i < getContainerSize(); i++) {
			if (getItem(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	private Mode insertStack(ItemStack stack) {
		if (level != null) {
			if (stack.getItem() == BWObjects.WOOD_ASH) {
				Mode reset = reset();
				syncCauldron();
				return reset;
			} else if (mode != Mode.FAILED && mode != Mode.TELEPORTATION) {
				int firstEmpty = getFirstEmptySlot();
				if (firstEmpty != -1) {
					setItem(firstEmpty, stack);
					if (mode == Mode.NORMAL && stack.getItem() == BWObjects.MANDRAKE_ROOT) {
						clearContent();
						return Mode.BREWING;
					}
					if (mode == Mode.BREWING) {
						CauldronBrewingRecipe cauldronBrewingRecipe = level.getRecipeManager().getAllRecipesFor(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).stream().filter(recipe -> recipe.input.test(stack)).findFirst().orElse(null);
						if (isRecipeValid(cauldronBrewingRecipe) || stack.getItem() == Items.REDSTONE || stack.getItem() == Items.GLOWSTONE_DUST) {
							BlockPos altarPos = getAltarPos();
							if (altarPos != null && ((WitchAltarBlockEntity) level.getBlockEntity(altarPos)).drain(getBrewCost(), true)) {
								setColor(PotionUtils.getColor(getPotion(null)));
								return Mode.BREWING;
							}
						}
					} else {
						oilRecipe = level.getRecipeManager().getAllRecipesFor(BWRecipeTypes.OIL_RECIPE_TYPE).stream().filter(recipe -> recipe.matches(this, level)).findFirst().orElse(null);
						if (oilRecipe != null) {
							setColor(oilRecipe.color);
							return Mode.OIL_CRAFTING;
						} else if (firstEmpty == 0 && stack.getItem() == Items.ENDER_PEARL) {
							clearContent();
							setColor(0x319a89);
							return Mode.TELEPORTATION;
						}
						setColor(0xfc00fc);
						return Mode.OIL_CRAFTING;
					}
				}
			}
		}
		return fail();
	}

	public ItemStack getPotion(LivingEntity creator) {
		ItemStack stack = new ItemStack(Items.POTION);
		if (level != null) {
			List<MobEffectInstance> effects = new ArrayList<>();
			int durationBoost = creator != null && BWUtil.getArmorPieces(creator, armorStack -> armorStack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == BWMaterials.ALCHEMIST_ARMOR) >= 3 ? 1 : 0;
			int amplifierBoost = 0;
			boolean leonard = creator instanceof Player player && BewitchmentAPI.isPledged(player, BWPledges.LEONARD);
			for (int i = 0; i < getContainerSize(); i++) {
				ItemStack stackInSlot = getItem(i);
				if (stackInSlot.getItem() instanceof TaglockItem && TaglockItem.isTaglockFromPlayer(stackInSlot)) {
					stack.getOrCreateTag().putUUID("PolymorphUUID", TaglockItem.getTaglockUUID(stackInSlot));
					stack.getOrCreateTag().putString("PolymorphName", TaglockItem.getTaglockName(stackInSlot));
				}
				CauldronBrewingRecipe cauldronBrewingRecipe = level.getRecipeManager().getAllRecipesFor(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE).stream().filter(recipe -> recipe.input.test(stackInSlot)).findFirst().orElse(null);
				if (cauldronBrewingRecipe != null && effects.stream().noneMatch(effect -> effect.getEffect() == cauldronBrewingRecipe.output)) {
					effects.add(new MobEffectInstance(cauldronBrewingRecipe.output, cauldronBrewingRecipe.time));
				} else if (stackInSlot.getItem() == Items.REDSTONE) {
					durationBoost++;
				} else if (stackInSlot.getItem() == Items.GLOWSTONE_DUST) {
					amplifierBoost++;
				}
			}
			for (int i = 0; i < effects.size(); i++) {
				for (int j = 0; j < durationBoost; j++) {
					MobEffect type = effects.get(i).getEffect();
					int duration = effects.get(i).getDuration();
					effects.set(i, new MobEffectInstance(type, type.isInstantenous() ? duration : duration * 2));
				}
				for (int j = 0; j < amplifierBoost && j < 2; j++) {
					if (j == 1 && !leonard) {
						break;
					}
					MobEffect type = effects.get(i).getEffect();
					int duration = effects.get(i).getDuration();
					effects.set(i, new MobEffectInstance(type, type.isInstantenous() ? duration : duration / 2, effects.get(i).getAmplifier() + 1));
				}
			}
			List<MobEffectInstance> finalEffects = new ArrayList<>();
			for (int i = effects.size() - 1; i >= 0; i--) {
				if (effects.get(i).getEffect() == BWStatusEffects.CORRUPTION) {
					finalEffects.add(effects.remove(i));
				} else if (effects.get(i).getEffect() == BWStatusEffects.POLYMORPH) {
					MobEffectInstance removed = effects.remove(i);
					finalEffects.add(new MobEffectInstance(removed.getEffect(), removed.getDuration(), removed.getAmplifier(), removed.isAmbient(), false, removed.showIcon()));
				}
			}
			finalEffects.addAll(effects);
			PotionUtils.setCustomEffects(stack, finalEffects);
			stack.getOrCreateTag().putInt("CustomPotionColor", PotionUtils.getColor(finalEffects));
			stack.getOrCreateTag().putBoolean("BewitchmentBrew", true);
		}
		return stack;
	}

	public int getBrewCost() {
		int cost = 0;
		for (int i = 0; i < getContainerSize(); i++) {
			if (!getItem(i).isEmpty()) {
				cost += 150;
			}
		}
		return cost;
	}

	public int getTargetLevel(ItemStack stack) {
		Item item = stack.getItem();
		int level = getBlockState().getValue(BWProperties.LEVEL);
		if (mode == Mode.NORMAL) {
			if (item == Items.BUCKET && level == 3) {
				return 0;
			} else if (item == Items.WATER_BUCKET && level == 0) {
				return 3;
			} else if (item == Items.GLASS_BOTTLE) {
				return level - 1;
			} else if (item == Items.POTION && level < 3 && PotionUtils.getPotion(stack) == Potions.WATER) {
				return level + 1;
			}
		} else if (mode == Mode.OIL_CRAFTING) {
			if (oilRecipe != null && item == Items.GLASS_BOTTLE) {
				return level - 1;
			}
		} else if (mode == Mode.BREWING) {
			if (item == Items.GLASS_BOTTLE) {
				return level - 1;
			}
		}
		return -1;
	}

	private boolean isRecipeValid(CauldronBrewingRecipe cauldronBrewingRecipe) {
		if (cauldronBrewingRecipe == null) {
			return false;
		}
		return BWConfig.enablePolymorph || cauldronBrewingRecipe.output != BWStatusEffects.POLYMORPH;
	}

	public enum Mode {
		NORMAL("NORMAL"), OIL_CRAFTING("OIL_CRAFTING"), BREWING("BREWING"), TELEPORTATION("TELEPORTATION"), FAILED("FAILED");

		public final String name;

		Mode(String name) {
			this.name = name;
		}
	}
}
