/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import moriyashiine.bewitchment.common.component.base.AutoSyncedComponent;
import moriyashiine.bewitchment.common.component.base.CommonTickingComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class FamiliarComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean familiar = false;

	public FamiliarComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setFamiliar(tag.getBoolean("Familiar"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("Familiar", isFamiliar());
	}

	@Override
	public void tick() {
		if (isFamiliar()) {
			if (!obj.level().isClientSide) {
				if (obj.tickCount % 100 == 0) {
					obj.heal(1);
				}
			} else if (obj.getRandom().nextFloat() < 0.25f) {
				obj.level().addParticle(ParticleTypes.ENCHANT, obj.getRandomX(obj.getBbWidth()), obj.getY() + Mth.nextFloat(obj.getRandom(), 0, obj.getBbHeight()), obj.getRandomZ(obj.getBbWidth()), 0, 0, 0);
			}
		}
	}

	public boolean isFamiliar() {
		return familiar;
	}

	public void setFamiliar(boolean familiar) {
		this.familiar = familiar;
		BWComponents.FAMILIAR_COMPONENT.sync(obj);
	}
}
