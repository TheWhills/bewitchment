/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;

public class UnbindFamiliarRitualFunction extends RitualFunction {
	public UnbindFamiliarRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
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
		boolean succeeded = false;
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
			if (livingEntity != null) {
				Player closestPlayer = world.getNearestPlayer(effectivePos.getX() + 0.5, effectivePos.getY() + 0.5, effectivePos.getZ() + 0.5, 8, false);
				if (closestPlayer != null && BewitchmentAPI.getFamiliar(closestPlayer) != null) {
					CompoundTag entityCompound = new CompoundTag();
					livingEntity.saveAsPassenger(entityCompound);
					if (entityCompound.contains("Owner") && closestPlayer.getUUID().equals(entityCompound.getUUID("Owner"))) {
						BWComponents.FAMILIAR_COMPONENT.get(livingEntity).setFamiliar(false);
						BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(world);
						for (int i = universalWorldState.familiars.size() - 1; i >= 0; i--) {
							if (livingEntity.getUUID().equals(universalWorldState.familiars.get(i).getB().getUUID("UUID"))) {
								universalWorldState.familiars.remove(i);
								universalWorldState.setDirty();
								succeeded = true;
							}
						}
					}
				}
			}
		}
		if (!succeeded) {
			Containers.dropContents(world, glyphPos, inventory);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
