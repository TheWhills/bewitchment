/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.entity;

import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BroomEntity extends Entity {
	public ItemStack stack = ItemStack.EMPTY;

	private float damage = 0;

	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYaw;
	private double lerpPitch;

	public BroomEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		stack = ItemStack.of(nbt.getCompound("Stack"));
		damage = nbt.getFloat("Damage");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		nbt.put("Stack", stack.save(new CompoundTag()));
		nbt.putFloat("Damage", damage);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this, 0);
	}

	@Override
	public void tick() {
		super.tick();
		if (isControlledByLocalInstance()) {
			lerpSteps = 0;
			syncPacketPositionCodec(getX(), getY(), getZ());
		}
		if (lerpSteps > 0) {
			absMoveTo(getX() + (lerpX - getX()) / lerpSteps, getY() + (lerpY - getY()) / lerpSteps, getZ() + (lerpZ - getZ()) / lerpSteps);
			setRot((float) (getYRot() + Mth.wrapDegrees(lerpYaw - getYRot()) / lerpSteps), (float) (getXRot() + (lerpPitch - getXRot()) / lerpSteps));
			--lerpSteps;
		}
		if (isControlledByLocalInstance()) {
			syncPacketPositionCodec(getX(), getY(), getZ());
			Entity passenger = getFirstPassenger();
			if (passenger instanceof Player player) {
				setRot(passenger.getYRot(), passenger.getXRot());
				if (BWComponents.BROOM_USER_COMPONENT.get(player).isPressingForward()) {
					push(passenger.getLookAngle().x / 8 * getSpeed(), passenger.getLookAngle().y / 8 * getSpeed(), passenger.getLookAngle().z / 8 * getSpeed());
					setDeltaMovement(Mth.clamp(getDeltaMovement().x, -getSpeed(), getSpeed()), Mth.clamp(getDeltaMovement().y, -getSpeed(), getSpeed()), Mth.clamp(getDeltaMovement().z, -getSpeed(), getSpeed()));
				}
			}
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.9));
		} else {
			setDeltaMovement(Vec3.ZERO);
		}
		if (!level().isClientSide && damage > 0) {
			damage -= 1 / 20f;
			damage = Math.max(damage, 0);
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (!level().isClientSide) {
			return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
		}
		return super.interact(player, hand);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (isInvulnerableTo(source)) {
			return false;
		}
		if (!level().isClientSide && !isRemoved()) {
			damage += amount;
			if (damage > 4) {
				spawnAtLocation(getDroppedStack());
				remove(RemovalReason.DISCARDED);
			}
		}
		return true;
	}

	@Nullable
	@Override
	public Entity getFirstPassenger() {
		return getPassengers().isEmpty() ? null : getPassengers().get(0);
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return getFirstPassenger() == null;
	}

	@Override
	public boolean canCollideWith(Entity other) {
		return Boat.canVehicleCollide(this, other);
	}

	@Override
	public boolean isPickable() {
		return !isRemoved();
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}

	@Override
	protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		lerpX = x;
		lerpY = y;
		lerpZ = z;
		lerpYaw = yaw;
		lerpPitch = pitch;
		lerpSteps = 10;
	}

	public void init(ItemStack stack) {
	}

	protected ItemStack getDroppedStack() {
		return stack;
	}

	protected UUID getOwner() {
		return TaglockItem.getTaglockUUID(stack);
	}

	protected float getSpeed() {
		return 0.75f;
	}
}
