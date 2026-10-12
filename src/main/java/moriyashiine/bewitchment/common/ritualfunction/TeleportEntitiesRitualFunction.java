/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.item.WaystoneItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;

public class TeleportEntitiesRitualFunction extends RitualFunction {
	public TeleportEntitiesRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		BlockPos location = null;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
				LivingEntity livingEntity = BewitchmentAPI.getTaglockOwner(world, stack);
				if (livingEntity != null && world.dimension().equals(livingEntity.getCommandSenderWorld().dimension())) {
					location = livingEntity.blockPosition();
					break;
				}
			} else if (stack.getItem() instanceof WaystoneItem && stack.hasTag() && stack.getOrCreateTag().contains("LocationPos") && world.dimension().location().toString().equals(stack.getOrCreateTag().getString("LocationWorld"))) {
				location = BlockPos.of(stack.getOrCreateTag().getLong("LocationPos"));
				break;
			}
		}
		if (location != null && world.getWorldBorder().isWithinBounds(location)) {
			int radius = catFamiliar ? 9 : 3;
			for (Entity entity : world.getEntitiesOfClass(Entity.class, new AABB(effectivePos).inflate(radius))) {
				BWUtil.teleport(entity, location.getX() + 0.5, location.getY() - 0.5, location.getZ() + 0.5, true);
			}
		} else {
			Containers.dropContents(world, glyphPos, inventory);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
