/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.misc;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.api.entity.Pledgeable;
import moriyashiine.bewitchment.client.packet.SpawnPortalParticlesPacket;
import moriyashiine.bewitchment.common.item.ScepterItem;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;

public class BWUtil {
	public static final MerchantOffers EMPTY_TRADES = new MerchantOffers();

	public static Iterable<BlockPos> getBlockPoses(BlockPos origin, int radius) {
		return BlockPos.withinManhattan(origin, radius, radius, radius);
	}

	public static BlockPos getClosestBlockPos(BlockPos origin, int radius, Predicate<BlockPos> provider) {
		return BlockPos.findClosestMatch(origin, radius, radius, provider).orElse(null);
	}

	public static NearestAttackableTargetGoal<LivingEntity> createGenericPledgeableTargetGoal(Mob entity) {
		return new NearestAttackableTargetGoal<>(entity, LivingEntity.class, 10, true, false, foundEntity -> {
			if (foundEntity instanceof ArmorStand) {
				return false;
			}
			if (foundEntity instanceof Player player) {
				if (BewitchmentAPI.isPledged(player, ((Pledgeable) entity).getPledgeID())) {
					return false;
				}
			} else if (foundEntity.getMobType() == BewitchmentAPI.DEMON) {
				return false;
			}
			return BWUtil.getArmorPieces(foundEntity, stack -> stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3;
		});
	}

	public static boolean isTool(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof TieredItem || item instanceof ProjectileWeaponItem || item instanceof ScepterItem || item instanceof ShieldItem || item instanceof TridentItem;
	}

	public static boolean rejectTrades(LivingEntity merchant) {
		return !merchant.level().getEntitiesOfClass(Player.class, new AABB(merchant.blockPosition()).inflate(8), foundEntity -> merchant.hasLineOfSight(foundEntity) && foundEntity.isAlive() && BWComponents.CURSES_COMPONENT.get(foundEntity).hasCurse(BWCurses.APATHY.get())).isEmpty();
	}

	public static int getArmorPieces(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
		int amount = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR && predicate.test(livingEntity.getItemBySlot(slot))) {
				amount++;
			}
		}
		return amount;
	}

	public static void addItemToInventoryAndConsume(Player player, InteractionHand hand, ItemStack toAdd) {
		boolean shouldAdd = false;
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getCount() == 1) {
			if (player.isCreative()) {
				shouldAdd = true;
			} else {
				player.setItemInHand(hand, toAdd);
			}
		} else {
			stack.shrink(1);
			shouldAdd = true;
		}
		if (shouldAdd) {
			if (!player.getInventory().add(toAdd)) {
				player.drop(toAdd, false, true);
			}
		}
	}

	public static void attemptTeleport(Entity entity, BlockPos origin, int distance, boolean hasEffects) {
		for (int i = 0; i < 32; i++) {
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(origin.getX() + Mth.nextDouble(entity.level().random, -distance, distance), origin.getY() + Mth.nextDouble(entity.level().random, -distance / 2f, distance / 2f), origin.getZ() + Mth.nextDouble(entity.level().random, -distance, distance));
			if (!entity.level().getBlockState(mutable).isSolid()) {
				while (mutable.getY() > 0 && !entity.level().getBlockState(mutable).isSolid()) {
					mutable.move(Direction.DOWN);
				}
				if (entity.level().getBlockState(mutable).blocksMotion()) {
					teleport(entity, mutable.getX() + 0.5, mutable.getY() + 0.5, mutable.getZ() + 0.5, hasEffects);
					break;
				}
			}
		}
	}

	public static void teleport(Entity entity, double x, double y, double z, boolean hasEffects) {
		if (hasEffects) {
			if (!entity.isSilent()) {
				entity.level().playSound(null, entity.blockPosition(), BWSoundEvents.ENTITY_GENERIC_TELEPORT.get(), entity.getSoundSource(), 1, 1);
			}
			BWNetwork.sendToTrackingAndSelf(entity, new SpawnPortalParticlesPacket(entity));
		}
		if (entity instanceof LivingEntity livingEntity) {
			if (entity instanceof Player player) {
				player.stopSleepInBed(true, false);
			} else {
				livingEntity.stopSleeping();
			}
		}
		entity.teleportToWithTicket(x, y + 0.5, z);
		if (hasEffects) {
			if (!entity.isSilent()) {
				entity.level().playSound(null, entity.blockPosition(), BWSoundEvents.ENTITY_GENERIC_TELEPORT.get(), entity.getSoundSource(), 1, 1);
			}
			BWNetwork.sendToTrackingAndSelf(entity, new SpawnPortalParticlesPacket(entity));
		}
	}
}
