/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import moriyashiine.bewitchment.common.component.base.ServerTickingComponent;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class FortuneComponent implements ServerTickingComponent {
	private final Player obj;
	private Fortune.Instance fortune = null;

	public FortuneComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (tag.contains("Fortune")) {
			setFortune(new Fortune.Instance(BWRegistries.FORTUNE.get(new ResourceLocation(tag.getString("Fortune"))), tag.getInt("FortuneDuration")));
		}
	}

	@SuppressWarnings({"ConstantConditions", "NullableProblems"})
	@Override
	public void writeToNbt(CompoundTag tag) {
		if (getFortune() != null) {
			tag.putString("Fortune", BWRegistries.FORTUNE.getKey(getFortune().fortune).toString());
			tag.putInt("FortuneDuration", getFortune().duration);
		}
	}

	@Override
	public void serverTick() {
		if (getFortune() != null) {
			if (getFortune().fortune.tick((ServerLevel) obj.level(), obj)) {
				getFortune().duration = 0;
			} else {
				getFortune().duration--;
			}
			if (getFortune().duration <= 0) {
				if (getFortune().fortune.finish((ServerLevel) obj.level(), obj)) {
					setFortune(null);
				} else {
					getFortune().duration = obj.getRandom().nextInt(120000);
				}
			}
		}
	}

	public Fortune.Instance getFortune() {
		return fortune;
	}

	public void setFortune(Fortune.Instance fortune) {
		this.fortune = fortune;
	}
}
