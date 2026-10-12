/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.ritual;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ZombieVillager.class)
public interface ZombieVillagerEntityAccessor {
	@Invoker("finishConversion")
	void bw_finishConversion(ServerLevel world);
}
