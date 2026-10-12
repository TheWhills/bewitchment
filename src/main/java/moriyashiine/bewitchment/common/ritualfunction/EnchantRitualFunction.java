/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;

public class EnchantRitualFunction extends RitualFunction {
	public EnchantRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		for (ItemEntity itemEntity : world.getEntities(EntityType.ITEM, new AABB(effectivePos).inflate(2, 0, 2), entity -> entity.getItem().isEnchantable())) {
			Player closestPlayer = world.getNearestPlayer(itemEntity, 8);
			if (closestPlayer != null && closestPlayer.experienceLevel >= 5) {
				closestPlayer.giveExperienceLevels(-5);
				EnchantmentHelper.enchantItem(world.random, itemEntity.getItem(), catFamiliar ? 60 : 40, false);
			}
			Containers.dropItemStack(world, effectivePos.getX() + 0.5, effectivePos.getY() + 0.5, effectivePos.getZ() + 0.5, itemEntity.getItem().copy());
			itemEntity.getItem().shrink(1);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
