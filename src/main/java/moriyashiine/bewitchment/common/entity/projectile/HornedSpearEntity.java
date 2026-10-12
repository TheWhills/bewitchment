/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.projectile;

import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class HornedSpearEntity extends AbstractArrow {
	public ItemStack spear = ItemStack.EMPTY;
	private boolean dealtDamage = false;

	public HornedSpearEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
		super(entityType, world);
	}

	public HornedSpearEntity(Level world, double x, double y, double z) {
		super(BWEntityTypes.HORNED_SPEAR, x, y, z, world);
	}

	public HornedSpearEntity(EntityType<? extends AbstractArrow> type, LivingEntity owner, Level world, ItemStack stack) {
		super(type, owner, world);
		spear = stack;
	}

	@Override
	protected ItemStack getPickupItem() {
		return spear;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity owner = getOwner();
		Entity entity = result.getEntity();
		float damage = 7;
		if (entity instanceof LivingEntity livingEntity) {
			damage += EnchantmentHelper.getDamageBonus(spear, livingEntity.getMobType());
		}
		if (owner instanceof HerneEntity) {
			damage *= 3;
		}
		dealtDamage = true;
		if (entity.hurt(entity.level().damageSources().trident(owner == null ? this : owner, owner == null ? this : owner), damage)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}
			if (entity instanceof LivingEntity livingEntity) {
				if (owner instanceof LivingEntity livingOwner) {
					EnchantmentHelper.doPostHurtEffects(livingEntity, owner);
					EnchantmentHelper.doPostDamageEffects(livingOwner, livingEntity);
				}
				doPostHurtEffects(livingEntity);
			}
		}
		setDeltaMovement(getDeltaMovement().multiply(-0.01, -0.1, -0.01));
	}

	@Nullable
	@Override
	protected EntityHitResult findHitEntity(Vec3 currentPosition, Vec3 nextPosition) {
		if (isOwnerAlive()) {
			EntityHitResult collision = ProjectileUtil.getEntityHitResult(level(), this, currentPosition, nextPosition, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1), this::canHitEntity);
			if (collision != null && collision.getEntity() instanceof Mob mob && getOwner().getUUID().equals(BWComponents.MINION_COMPONENT.get(mob).getMaster())) {
				return null;
			}
		}
		return dealtDamage ? null : super.findHitEntity(currentPosition, nextPosition);
	}

	@Override
	public void playerTouch(Player player) {
		Entity owner = getOwner();
		if (owner == null || owner.getUUID() == player.getUUID()) {
			super.playerTouch(player);
		}
	}

	@Override
	public void tick() {
		if (inGroundTime > 4) {
			dealtDamage = true;
		}
		Entity entity = getOwner();
		if (entity instanceof Player && (dealtDamage || isNoPhysics() || getY() <= level().getMinBuildHeight())) {
			if (!isOwnerAlive()) {
				if (!level().isClientSide && pickup == AbstractArrow.Pickup.ALLOWED) {
					spawnAtLocation(getPickupItem(), 0.1f);
				}
				remove(RemovalReason.DISCARDED);
			} else {
				setNoPhysics(true);
				Vec3 vec3d = new Vec3(entity.getX() - getX(), entity.getEyeY() - getY(), entity.getZ() - getZ());
				setPosRaw(getX(), getY() + vec3d.y * 0.1, getZ());
				if (level().isClientSide) {
					yOld = getY();
				}
				setDeltaMovement(getDeltaMovement().scale(0.95d).add(vec3d.normalize().scale(0.3)));
			}
		}
		super.tick();
	}

	@Override
	protected void tickDespawn() {
		if (pickup != Pickup.ALLOWED) {
			super.tickDespawn();
		}
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		spear = ItemStack.of(nbt.getCompound("Spear"));
		dealtDamage = nbt.getBoolean("DealtDamage");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.put("Spear", spear.save(new CompoundTag()));
		nbt.putBoolean("DealtDamage", dealtDamage);
	}

	private boolean isOwnerAlive() {
		Entity entity = getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayer) || !entity.isSpectator();
		} else {
			return false;
		}
	}
}
