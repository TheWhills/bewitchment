/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** FireBlock.setFlammable is private; Fabric exposed it through FlammableBlockRegistry. */
@Mixin(FireBlock.class)
public interface FireBlockAccessor {
	@Invoker("setFlammable")
	void bewitchment$setFlammable(Block block, int encouragement, int flammability);
}
