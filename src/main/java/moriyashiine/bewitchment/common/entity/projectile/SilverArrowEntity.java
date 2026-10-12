/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.projectile;

import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SilverArrowEntity extends AbstractArrow {
	public SilverArrowEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
		super(entityType, world);
	}

	public SilverArrowEntity(EntityType<? extends AbstractArrow> type, LivingEntity owner, Level world) {
		super(type, owner, world);
	}

	public SilverArrowEntity(Level world, double x, double y, double z) {
		super(BWEntityTypes.SILVER_ARROW, x, y, z, world);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(BWObjects.SILVER_ARROW);
	}
}
