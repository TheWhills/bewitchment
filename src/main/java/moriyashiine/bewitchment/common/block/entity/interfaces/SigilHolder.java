/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity.interfaces;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.DragonsBloodChestBlockEntity;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public interface SigilHolder {
	List<UUID> getEntities();

	UUID getOwner();

	void setOwner(UUID owner);

	Sigil getSigil();

	void setSigil(Sigil sigil);

	int getUses();

	void setUses(int uses);

	boolean getModeOnWhitelist();

	void setModeOnWhitelist(boolean modeOnWhitelist);

	default void fromNbtSigil(CompoundTag nbt) {
		ListTag entitiesList = nbt.getList("Entities", Tag.TAG_COMPOUND);
		for (int i = 0; i < entitiesList.size(); i++) {
			getEntities().add(UUID.fromString(entitiesList.getString(i)));
		}
		if (nbt.contains("Owner")) {
			setOwner(nbt.getUUID("Owner"));
		}
		setSigil(BWRegistries.SIGIL.get(new ResourceLocation(nbt.getString("Sigil"))));
		setUses(nbt.getInt("Uses"));
		setModeOnWhitelist(nbt.getBoolean("ModeOnWhitelist"));
	}

	default void toNbtSigil(CompoundTag nbt) {
		ListTag entitiesList = new ListTag();
		for (int i = 0; i < getEntities().size(); i++) {
			entitiesList.add(StringTag.valueOf(getEntities().get(i).toString()));
		}
		nbt.put("Entities", entitiesList);
		if (getOwner() != null) {
			nbt.putUUID("Owner", getOwner());
		}
		if (getSigil() != null) {
			nbt.putString("Sigil", BWRegistries.SIGIL.getKey(getSigil()).toString());
		}
		nbt.putInt("Uses", getUses());
		nbt.putBoolean("ModeOnWhitelist", getModeOnWhitelist());
	}

	default void use(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		if (getSigil() != null && getSigil().active && test(user)) {
			InteractionResult result = getSigil().use(world, pos, user, hand);
			if (result == InteractionResult.SUCCESS) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				setUses(getUses() - 1);
				syncSigilHolder(blockEntity);
				blockEntity.setChanged();
			}
		}
	}

	default void tick(Level world, BlockPos pos, BlockEntity blockEntity) {
		if (world != null && !world.isClientSide) {
			if (getSigil() != null) {
				if (!getSigil().active) {
					int used = getSigil().tick(world, pos);
					if (used > 0) {
						setUses(getUses() - used);
						syncSigilHolder(blockEntity);
						blockEntity.setChanged();
					}
				}
				if (getUses() <= 0) {
					getEntities().clear();
					setOwner(null);
					setSigil(null);
					setUses(0);
					setModeOnWhitelist(false);
					syncSigilHolder(blockEntity);
					blockEntity.setChanged();
					if (blockEntity.getBlockState().getBlock() == BWObjects.SIGIL) {
						world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}

	default boolean test(Entity entity) {
		if (!getEntities().isEmpty()) {
			if (getModeOnWhitelist()) {
				return getEntities().contains(entity.getUUID());
			}
			return !getEntities().contains(entity.getUUID());
		}
		return true;
	}

	default void syncSigilHolder(BlockEntity blockEntity) {
		if (blockEntity instanceof SigilBlockEntity sigilBlockEntity) {
			sigilBlockEntity.sync();
		} else if (blockEntity instanceof DragonsBloodChestBlockEntity dragonsBloodChestBlockEntity) {
			dragonsBloodChestBlockEntity.sync();
		}
	}

	static void onUse(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		if (world.getBlockEntity(pos) instanceof SigilHolder sigilHolder && sigilHolder.test(user)) {
			sigilHolder.use(world, pos, user, hand);
		}
	}
}
