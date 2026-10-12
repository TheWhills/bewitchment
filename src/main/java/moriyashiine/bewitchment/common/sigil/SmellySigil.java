/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("ConstantConditions")
public class SmellySigil extends Sigil {
	public SmellySigil(boolean active, int uses) {
		super(active, uses);
	}

	@Override
	public int tick(Level world, BlockPos pos) {
		int amount = 0;
		if (world.getGameTime() % 20 == 0) {
			SigilHolder sigil = ((SigilHolder) world.getBlockEntity(pos));
			double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;
			for (Monster entity : world.getEntitiesOfClass(Monster.class, new AABB(pos).inflate(16, 6, 16), LivingEntity::isAlive)) {
				if (!entity.getType().is(BWTags.TAGLOCK_BLACKLIST) && sigil.test(entity)) {
					if (entity.getNavigation().getTargetPos() == null || world.getBlockState(entity.getNavigation().getTargetPos()).getBlock() != BWObjects.SIGIL) {
						if (entity.getTarget() == null && Math.sqrt(entity.distanceToSqr(new Vec3(x, y, z))) > 3 && (entity.getNavigation().getTargetPos() == null || entity.getNavigation().getTargetPos().distSqr(pos) > 1)) {
							entity.getNavigation().moveTo(x, y, z, 1);
							amount++;
						}
					}
				}
			}
		}
		return amount;
	}
}
