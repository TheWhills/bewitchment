/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.GlyphBlock;
import moriyashiine.bewitchment.common.item.WaystoneItem;
import moriyashiine.bewitchment.common.recipe.RitualRecipe;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class GlyphBlockEntity extends BlockEntity implements WorldlyContainer, UsesAltarPower {
	private static final byte[][] inner = {{0, 0, 1, 1, 1, 0, 0}, {0, 1, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 1, 0}, {0, 0, 1, 1, 1, 0, 0}};
	private static final byte[][] outer = {{0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0}, {0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0}};

	private final NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);

	private final int[] availableSlots = {};

	private BlockPos altarPos = null, effectivePos = null;

	public RitualFunction ritualFunction = null;
	private int timer = 0, endTime = 0;

	private boolean catFamiliar = false;

	private boolean loaded = false;

	public GlyphBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.GLYPH.get(), pos, state);
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
		if (nbt.contains("EffectivePos")) {
			effectivePos = BlockPos.of(nbt.getLong("EffectivePos"));
		}
		inventory.clear();
		ContainerHelper.loadAllItems(nbt, inventory);
		ritualFunction = BWRegistries.RITUAL_FUNCTION.get(new ResourceLocation(nbt.getString("RitualFunction")));
		timer = nbt.getInt("Timer");
		endTime = nbt.getInt("EndTime");
		catFamiliar = nbt.getBoolean("CatFamiliar");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if (getAltarPos() != null) {
			nbt.putLong("AltarPos", getAltarPos().asLong());
		}
		if (effectivePos != null) {
			nbt.putLong("EffectivePos", effectivePos.asLong());
		}
		ContainerHelper.saveAllItems(nbt, inventory);
		if (ritualFunction != null) {
			nbt.putString("RitualFunction", BWRegistries.RITUAL_FUNCTION.getKey(ritualFunction).toString());
		}
		nbt.putInt("Timer", timer);
		nbt.putInt("EndTime", endTime);
		nbt.putBoolean("CatFamiliar", catFamiliar);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return availableSlots;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
		return false;
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

	public static void tick(Level world, BlockPos pos, BlockState state, GlyphBlockEntity blockEntity) {
		if (world != null) {
			if (!blockEntity.loaded) {
				blockEntity.setChanged();
				blockEntity.loaded = true;
			}
			if (blockEntity.ritualFunction != null) {
				BlockPos targetPos = blockEntity.effectivePos == null ? pos : blockEntity.effectivePos;
				if (!world.getWorldBorder().isWithinBounds(targetPos)) {
					return;
				}
				blockEntity.timer++;
				if (world.isClientSide) {
					world.addParticle(ParticleTypes.END_ROD, true, pos.getX() + 0.5 + Mth.nextFloat(world.random, -0.2f, 0.2f), pos.getY() + 0.5 + Mth.nextFloat(world.random, -0.2f, 0.2f), pos.getZ() + 0.5 + Mth.nextFloat(world.random, -0.2f, 0.2f), 0, 0, 0);
					if (blockEntity.timer < 0) {
						for (int i = 0; i < 3; i++) {
							world.addParticle((ParticleOptions) blockEntity.ritualFunction.startParticle, true, targetPos.getX() + 0.5 + Mth.nextFloat(world.random, -0.2f, 0.2f), targetPos.getY() + 0.5 + Mth.nextFloat(world.random, -0.2f, 0.2f), targetPos.getZ() + 0.5 + Mth.nextFloat(world.random, -0.2f, 0.2f), Mth.nextFloat(world.random, -1, 1), Mth.nextFloat(world.random, 0.125f, 1), Mth.nextFloat(world.random, -1, 1));
						}
					}
				}
				if (blockEntity.timer >= 0) {
					blockEntity.ritualFunction.tick(world, pos, targetPos, blockEntity.catFamiliar);
					if (!world.isClientSide) {
						world.getChunkAt(blockEntity.effectivePos);
						if (blockEntity.timer == 0) {
							blockEntity.ritualFunction.start((ServerLevel) world, pos, targetPos, blockEntity, blockEntity.catFamiliar);
							world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_PLING.get(), SoundSource.BLOCKS, 1, 1);
							Containers.dropContents(world, pos, blockEntity);
						}
						if (blockEntity.timer >= blockEntity.endTime) {
							blockEntity.effectivePos = null;
							blockEntity.ritualFunction = null;
							blockEntity.timer = 0;
							blockEntity.endTime = 0;
							blockEntity.catFamiliar = false;
							blockEntity.syncGlyph();
						}
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

	public void syncGlyph() {
		sync();
	}

	public void onUse(Level world, BlockPos pos, LivingEntity user, InteractionHand hand, LivingEntity sacrifice) {
		ItemStack stack = user.getItemInHand(hand);
		if (ritualFunction != null && pos.equals(effectivePos) && stack.getItem() instanceof WaystoneItem && stack.hasTag() && stack.getOrCreateTag().contains("LocationPos") && world.dimension().location().toString().equals(stack.getOrCreateTag().getString("LocationWorld"))) {
			effectivePos = BlockPos.of(stack.getOrCreateTag().getLong("LocationPos"));
			stack.hurtAndBreak(1, user, stackUser -> stackUser.broadcastBreakEvent(hand));
			syncGlyph();
		} else {
			if (ritualFunction == null) {
				SimpleContainer test = new SimpleContainer(getContainerSize());
				List<ItemEntity> items = world.getEntities(EntityType.ITEM, new AABB(pos).inflate(2, 0, 2), entity -> true);
				for (ItemEntity entity : items) {
					test.addItem(entity.getItem().copy().split(1));
				}
				RitualRecipe recipe = world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.RITUAL_RECIPE_TYPE.get()).stream().filter(ritualRecipe -> ritualRecipe.matches(test, world)).findFirst().orElse(null);
				if (recipe != null && recipe.input.size() == items.size() && hasValidChalk(recipe)) {
					if (recipe.ritualFunction.isValid((ServerLevel) world, pos, test) || (recipe.ritualFunction.sacrifice != null && sacrifice != null && recipe.ritualFunction.sacrifice.test(sacrifice))) {
						boolean cat = user instanceof Player player && BewitchmentAPI.getFamiliar(player) == EntityType.CAT;
						if (altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(altarPos)).drain((int) (recipe.cost * (cat ? 0.75f : 1)), false)) {
							world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FIRE.get(), SoundSource.BLOCKS, 1, 1);
							if (user instanceof Player player) {
								player.displayClientMessage(Component.translatable("ritual." + recipe.getId().toString().replace(":", ".").replace("/", ".")), true);
							}
							for (int i = 0; i < items.size(); i++) {
								ItemEntity item = items.get(i);
								BWNetwork.sendToTracking(item, new SpawnSmokeParticlesPacket(item));
								setItem(i, item.getItem().split(1));
							}
							effectivePos = pos;
							ritualFunction = recipe.ritualFunction;
							timer = -100;
							endTime = recipe.runningTime;
							catFamiliar = cat;
							syncGlyph();
							return;
						}
						world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL.get(), SoundSource.BLOCKS, 1, 1);
						if (user instanceof Player player) {
							player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.insufficent_altar_power"), true);
						}
						return;
					}
					world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL.get(), SoundSource.BLOCKS, 1, 1);
					if (user instanceof Player player) {
						player.displayClientMessage(Component.translatable(recipe.ritualFunction.getInvalidMessage()), true);
					}
					return;
				}
				world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL.get(), SoundSource.BLOCKS, 1, 1);
				if (user instanceof Player player) {
					player.displayClientMessage(Component.translatable("ritual.none"), true);
				}
			} else if (sacrifice == null) {
				world.playSound(null, pos, BWSoundEvents.BLOCK_GLYPH_FAIL.get(), SoundSource.BLOCKS, 1, 1);
				Containers.dropContents(world, pos, this);
				effectivePos = null;
				ritualFunction = null;
				timer = 0;
				endTime = 0;
				catFamiliar = false;
				syncGlyph();
			}
		}
	}

	private boolean hasValidChalk(RitualRecipe recipe) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int x = 0; x < inner.length; x++) {
			for (int z = 0; z < inner.length; z++) {
				if (inner[x][z] == 1 && !isValidGlyph(recipe.inner, level.getBlockState(mutable.set(worldPosition.getX() + (x - inner.length / 2), worldPosition.getY(), worldPosition.getZ() + (z - inner.length / 2))).getBlock())) {
					return false;
				}
			}
		}
		if (!recipe.outer.isEmpty()) {
			for (int x = 0; x < outer.length; x++) {
				for (int z = 0; z < outer.length; z++) {
					if (outer[x][z] == 1 && !isValidGlyph(recipe.outer, level.getBlockState(mutable.set(worldPosition.getX() + (x - outer.length / 2), worldPosition.getY(), worldPosition.getZ() + (z - outer.length / 2))).getBlock())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean isValidGlyph(String name, Block block) {
		if (name.equals("normal") && block == BWObjects.GLYPH.get()) {
			return true;
		}
		if (name.equals("fiery") && block == BWObjects.FIERY_GLYPH.get()) {
			return true;
		}
		if (name.equals("eldritch") && block == BWObjects.ELDRITCH_GLYPH.get()) {
			return true;
		}
		return name.equals("any") && block instanceof GlyphBlock && block != BWObjects.GOLDEN_GLYPH.get();
	}
}
