/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class WitchAltarBlockEntity extends BlockEntity implements Container {
	private int loadingTimer = 20;

	private final Map<Block, Integer> checked = new HashMap<>();
	private final BlockPos.MutableBlockPos checking = new BlockPos.MutableBlockPos();
	private int counter = 0;

	public int power = 0, maxPower = 0, gain = 1;

	public boolean markedForScan = false;

	private final NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);

	public WitchAltarBlockEntity(BlockPos pos, BlockState state) {
		super(BWBlockEntityTypes.WITCH_ALTAR.get(), pos, state);
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
		inventory.clear();
		ContainerHelper.loadAllItems(nbt, inventory);
		power = nbt.getInt("Power");
		maxPower = nbt.getInt("MaxPower");
		gain = nbt.getInt("Gain");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		ContainerHelper.saveAllItems(nbt, inventory);
		nbt.putInt("Power", power);
		nbt.putInt("MaxPower", maxPower);
		nbt.putInt("Gain", gain);
	}

	public void sync() {
		if (level != null && !level.isClientSide) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	public static void tick(Level world, BlockPos pos, BlockState state, WitchAltarBlockEntity blockEntity) {
		if (world != null && !world.isClientSide) {
			if (blockEntity.loadingTimer > 0) {
				blockEntity.loadingTimer--;
				if (blockEntity.loadingTimer == 0) {
					blockEntity.markedForScan = true;
				}
			} else {
				if (world.getGameTime() % 20 == 0) {
					blockEntity.setChanged();
				}
				if (blockEntity.markedForScan) {
					blockEntity.counter = 0;
					blockEntity.checked.clear();
					blockEntity.scan(Short.MAX_VALUE);
					blockEntity.markedForScan = false;
				}
				blockEntity.scan(40);
				if (world.getGameTime() % 20 == 0) {
					if (world.getBlockState(pos.above()).getBlock() == BWObjects.BLESSED_STONE.get()) {
						blockEntity.power = Integer.MAX_VALUE;
					} else {
						blockEntity.power = Math.min(blockEntity.power + blockEntity.gain, blockEntity.maxPower);
					}
					PlayerLookup.around((ServerLevel) world, Vec3.atLowerCornerOf(pos), 24).forEach(player -> {
						if (player.canBeSeenByAnyone() && !player.isCreative() && !BWComponents.CURSES_COMPONENT.get(player).hasCurse(BWCurses.APATHY.get()) && BewitchmentAPI.fillMagic(player, 5, true) && blockEntity.drain(10, true)) {
							BewitchmentAPI.fillMagic(player, 5, false);
							blockEntity.drain(10, false);
						}
					});
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

	public boolean drain(int amount, boolean simulate) {
		if (power - amount >= 0) {
			if (!simulate) {
				power -= amount;
			}
			return true;
		}
		return false;
	}

	private void scan(int times) {
		if (level != null) {
			for (int i = 0; i < times; i++) {
				counter = ++counter % Short.MAX_VALUE;
				int x = counter & 31;
				int y = (counter >> 5) & 31;
				int z = (counter >> 10) & 31;
				BlockState checkedState = level.getBlockState(checking.set(worldPosition.getX() + x - 15, worldPosition.getY() + y - 15, worldPosition.getZ() + z - 15));
				Block checkedBlock = checkedState.getBlock();
				if (givesAltarPower(checkedState) && !(checkedState.is(BlockTags.LOGS) && BuiltInRegistries.BLOCK.getKey(checkedBlock).getPath().contains("stripped"))) {
					checked.put(checkedBlock, Math.min(checked.getOrDefault(checkedBlock, 0) + 1, 256));
				}
				if (counter == Short.MAX_VALUE - 1) {
					gain = getPentacleValue(getItem(1));
					maxPower = 0;
					float varietyMultiplier = 1;
					for (Block block : checked.keySet()) {
						if (checked.get(block) > 3) {
							varietyMultiplier *= 1.1f;
						}
						maxPower += checked.get(block);
					}
					maxPower = (int) ((maxPower * Math.min(varietyMultiplier, 4) / 10) * getSwordValue(getItem(0)) + getWandValue(getItem(2)));
					power = Math.min(power, maxPower);
					checked.clear();
				}
			}
		}
	}

	private static boolean givesAltarPower(BlockState state) {
		if (state.getBlock() instanceof BushBlock || state.getBlock() instanceof HugeMushroomBlock || state.getBlock() instanceof BonemealableBlock && !(state.getBlock() instanceof GrassBlock || state.getBlock() instanceof NyliumBlock)) {
			return true;
		}
		return state.is(BWTags.GIVES_ALTAR_POWER);
	}

	private static float getSwordValue(ItemStack stack) {
		return stack.is(BWTags.WEAK_SWORDS) ? 1.1F : stack.is(BWTags.AVERAGE_SWORDS) ? 1.2F : stack.is(BWTags.STRONG_SWORDS) ? 1.3F : 1;
	}

	private static int getPentacleValue(ItemStack stack) {
		return stack.is(BWTags.WEAK_PENTACLES) ? 2 : stack.is(BWTags.AVERAGE_PENTACLES) ? 3 : stack.is(BWTags.STRONG_PENTACLES) ? 5 : 1;
	}

	private static int getWandValue(ItemStack stack) {
		return stack.is(BWTags.WEAK_WANDS) ? 40 : stack.is(BWTags.AVERAGE_WANDS) ? 80 : stack.is(BWTags.STRONG_WANDS) ? 120 : 0;
	}
}
