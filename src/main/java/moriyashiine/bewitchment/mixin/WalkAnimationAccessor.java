/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Replaces the Fabric access widener entry {@code mutable field LivingEntity limbAnimator}:
 * the transformed-player renderer shares the player's walk animation with its disguise entity.
 */
@Mixin(LivingEntity.class)
public interface WalkAnimationAccessor {
	@Mutable
	@Accessor("walkAnimation")
	void bewitchment$setWalkAnimation(WalkAnimationState walkAnimation);
}
