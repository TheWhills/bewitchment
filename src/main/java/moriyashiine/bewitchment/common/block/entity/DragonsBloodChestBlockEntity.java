/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DragonsBloodChestBlockEntity extends BWChestBlockEntity implements SigilHolder {
	private final List<UUID> entities = new ArrayList<>();
	private UUID owner = null;
	private Sigil sigil = null;
	private int uses = 0;
	private boolean modeOnWhitelist = false;

	public DragonsBloodChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, Type type, boolean trapped) {
		super(blockEntityType, blockPos, blockState, type, trapped);
	}

	public DragonsBloodChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(BWBlockEntityTypes.DRAGONS_BLOOD_CHEST.get(), blockPos, blockState, Type.DRAGONS_BLOOD, false);
	}

	@Override
	public List<UUID> getEntities() {
		return entities;
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	@Override
	public Sigil getSigil() {
		return sigil;
	}

	@Override
	public void setSigil(Sigil sigil) {
		this.sigil = sigil;
	}

	@Override
	public int getUses() {
		return uses;
	}

	@Override
	public void setUses(int uses) {
		this.uses = uses;
	}

	@Override
	public boolean getModeOnWhitelist() {
		return modeOnWhitelist;
	}

	@Override
	public void setModeOnWhitelist(boolean modeOnWhitelist) {
		this.modeOnWhitelist = modeOnWhitelist;
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
		fromNbtSigil(nbt);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		toNbtSigil(nbt);
	}

	public void sync() {
		if (level != null && !level.isClientSide) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	public static void tick(Level world, BlockPos pos, BlockState state, DragonsBloodChestBlockEntity blockEntity) {
		if (world.isClientSide) {
			ChestBlockEntity.lidAnimateTick(world, pos, state, blockEntity);
		}
		blockEntity.tick(world, pos, blockEntity);
	}
}
