/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;

public class SmeltItemsRitualFunction extends RitualFunction {
	public SmeltItemsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void tick(Level world, BlockPos glyphPos, BlockPos effectivePos, boolean catFamiliar) {
		int radius = catFamiliar ? 9 : 3;
		if (!world.isClientSide) {
			if (world.getGameTime() % 20 == 0) {
				for (ItemEntity itemEntity : world.getEntities(EntityType.ITEM, new AABB(effectivePos).inflate(radius, 0, radius), entity -> true)) {
					if (world.random.nextFloat() < 1 / 4f) {
						world.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING).forEach(smeltingRecipe -> {
							for (Ingredient ingredient : smeltingRecipe.getIngredients()) {
								if (ingredient.test(itemEntity.getItem())) {
									world.playSound(null, itemEntity.blockPosition(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1, 1);
									Containers.dropItemStack(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), smeltingRecipe.getResultItem(world.registryAccess()).copy());
									world.addFreshEntity(new ExperienceOrb(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 1));
									itemEntity.getItem().shrink(1);
								}
							}
						});
					}
				}
			}
		} else {
			world.addParticle(ParticleTypes.FLAME, effectivePos.getX() + Mth.nextDouble(world.random, -radius, radius), effectivePos.getY() + 0.5, effectivePos.getZ() + Mth.nextDouble(world.random, -radius, radius), 0, 0, 0);
		}
	}
}
