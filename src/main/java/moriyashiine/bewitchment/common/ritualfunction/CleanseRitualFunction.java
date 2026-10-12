/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.mixin.ritual.ZombieVillagerEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;

public class CleanseRitualFunction extends RitualFunction {
	public CleanseRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public String getInvalidMessage() {
		return "ritual.precondition.found_entity";
	}

	@Override
	public boolean isValid(ServerLevel world, BlockPos pos, Container inventory) {
		ItemStack taglock = null;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (inventory.getItem(i).getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
				taglock = stack;
				break;
			}
		}
		return taglock != null && BewitchmentAPI.getTaglockOwner(world, taglock) != null;
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		ItemStack taglock = null;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (inventory.getItem(i).getItem() instanceof TaglockItem) {
				taglock = stack;
				break;
			}
		}
		if (taglock != null) {
			LivingEntity livingEntity = BewitchmentAPI.getTaglockOwner(world, taglock);
			BWComponents.CURSES_COMPONENT.maybeGet(livingEntity).ifPresent(cursesComponent -> {
				for (Curse.Instance instance : cursesComponent.getCurses()) {
					if (catFamiliar || (world.random.nextFloat() < (instance.curse.type == Curse.Type.LESSER ? 7.5f / 10f : 5 / 10f))) {
						cursesComponent.removeCurse(instance.curse);
					}
				}
			});
			if (livingEntity instanceof ZombieVillager) {
				((ZombieVillagerEntityAccessor) livingEntity).bw_finishConversion(world);
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
