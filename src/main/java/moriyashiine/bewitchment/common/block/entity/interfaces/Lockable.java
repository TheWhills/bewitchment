/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity.interfaces;

import moriyashiine.bewitchment.common.block.entity.ElderChestBlockEntity;
import moriyashiine.bewitchment.common.block.entity.LockableBlockEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.List;
import java.util.UUID;

public interface Lockable {
	List<UUID> getEntities();

	UUID getOwner();

	void setOwner(UUID owner);

	boolean getModeOnWhitelist();

	void setModeOnWhitelist(boolean modeOnWhitelist);

	boolean getLocked();

	void setLocked(boolean locked);

	default void fromNbtLockable(CompoundTag nbt) {
		ListTag entitiesList = nbt.getList("Entities", Tag.TAG_COMPOUND);
		for (int i = 0; i < entitiesList.size(); i++) {
			getEntities().add(UUID.fromString(entitiesList.getString(i)));
		}
		if (nbt.contains("Owner")) {
			setOwner(nbt.getUUID("Owner"));
		}
		setModeOnWhitelist(nbt.getBoolean("ModeOnWhitelist"));
		setLocked(nbt.getBoolean("Locked"));
	}

	default void toNbtLockable(CompoundTag nbt) {
		ListTag entitiesList = new ListTag();
		for (int i = 0; i < getEntities().size(); i++) {
			entitiesList.add(StringTag.valueOf(getEntities().get(i).toString()));
		}
		nbt.put("Entities", entitiesList);
		if (getOwner() != null) {
			nbt.putUUID("Owner", getOwner());
		}
		nbt.putBoolean("ModeOnWhitelist", getModeOnWhitelist());
		nbt.putBoolean("Locked", getLocked());
	}

	default InteractionResult use(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		if (user.getUUID().equals(getOwner())) {
			ItemStack stack = user.getItemInHand(hand);
			if (!getLocked()) {
				if (stack.is(BWTags.SILVER_INGOTS)) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity instanceof Lockable lockable) {
						if (!world.isClientSide) {
							if (!(user instanceof Player player && player.isCreative())) {
								stack.shrink(1);
							}
							lockable.setModeOnWhitelist(true);
							lockable.setLocked(true);
							syncLockable(blockEntity);
							blockEntity.setChanged();
						}
						return InteractionResult.SUCCESS;
					}
				}
			}
		}
		if (getLocked() && !test(user)) {
			return InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}

	default boolean test(Entity entity) {
		if (entity.getUUID().equals(getOwner())) {
			return true;
		} else if (getLocked()) {
			if (!getEntities().isEmpty()) {
				if (getModeOnWhitelist()) {
					return getEntities().contains(entity.getUUID());
				}
				return !getEntities().contains(entity.getUUID());
			}
			return false;
		}
		return true;
	}

	default void syncLockable(BlockEntity blockEntity) {
		if (blockEntity instanceof LockableBlockEntity lockableBlockEntity) {
			lockableBlockEntity.sync();
		} else if (blockEntity instanceof ElderChestBlockEntity elderChestBlockEntity) {
			elderChestBlockEntity.sync();
		}
	}

	static InteractionResult onUse(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		if (world.getBlockEntity(pos) instanceof Lockable lockable) {
			if (lockable.test(user)) {
				return lockable.use(world, pos, user, hand);
			}
			return InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}
}
