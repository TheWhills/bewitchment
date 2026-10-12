/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class SilverArrowItem extends ArrowItem {
	private static final DispenseItemBehavior DISPENSER_BEHAVIOR = new AbstractProjectileDispenseBehavior() {
		protected Projectile getProjectile(Level world, Position position, ItemStack stack) {
			SilverArrowEntity arrowEntity = new SilverArrowEntity(world, position.x(), position.y(), position.z());
			arrowEntity.pickup = AbstractArrow.Pickup.ALLOWED;
			return arrowEntity;
		}
	};

	public SilverArrowItem(Properties settings) {
		super(settings);
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}

	@Override
	public AbstractArrow createArrow(Level world, ItemStack stack, LivingEntity shooter) {
		return new SilverArrowEntity(BWEntityTypes.SILVER_ARROW, shooter, world);
	}
}
