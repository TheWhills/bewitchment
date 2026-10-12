/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.registry;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RitualFunction {
	public final ParticleType<?> startParticle;
	public final Predicate<LivingEntity> sacrifice;

	public RitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		this.startParticle = startParticle;
		this.sacrifice = sacrifice;
	}

	public String getInvalidMessage() {
		return "ritual.precondition.sacrifice";
	}

	public boolean isValid(ServerLevel world, BlockPos pos, Container inventory) {
		return sacrifice == null;
	}

	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.isDamageableItem()) {
				if (stack.hurt(1, world.random, null) && stack.getDamageValue() == stack.getMaxDamage()) {
					stack.shrink(1);
				}
			} else {
				Item item = stack.getItem();
				inventory.setItem(i, item.hasCraftingRemainingItem() ? new ItemStack(item.getCraftingRemainingItem()) : ItemStack.EMPTY);
			}
		}
	}

	public void tick(Level world, BlockPos glyphPos, BlockPos effectivePos, boolean catFamiliar) {
	}
}
