/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.pledge;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.Pledgeable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ConstantConditions")
@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!level().isClientSide && target != null) {
			if (this instanceof Pledgeable pledgeable) {
				if (target instanceof Player player && BewitchmentAPI.isPledged(player, pledgeable.getPledgeID())) {
					BewitchmentAPI.unpledge(player);
				}
				pledgeable.summonMinions((Mob) (Object) this);
			}
		}
		return target;
	}
}
