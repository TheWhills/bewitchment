/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import moriyashiine.bewitchment.common.component.base.ServerTickingComponent;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import java.util.ArrayList;
import java.util.List;

public class CursesComponent implements ServerTickingComponent {
	private final LivingEntity obj;
	private final List<Curse.Instance> curses = new ArrayList<>();

	public CursesComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		ListTag cursesList = tag.getList("Curses", Tag.TAG_COMPOUND);
		for (int i = 0; i < cursesList.size(); i++) {
			CompoundTag curseCompound = cursesList.getCompound(i);
			addCurse(new Curse.Instance(BWRegistries.CURSE.get(new ResourceLocation(curseCompound.getString("Curse"))), curseCompound.getInt("Duration")));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.put("Curses", toNbtCurse());
	}

	@Override
	public void serverTick() {
		for (int i = curses.size() - 1; i >= 0; i--) {
			Curse.Instance instance = curses.get(i);
			instance.curse.tick(obj);
			instance.duration--;
			if (instance.duration <= 0) {
				curses.remove(i);
			}
		}
	}

	public List<Curse.Instance> getCurses() {
		return curses;
	}

	public boolean hasCurse(Curse curse) {
		return getCurses().stream().anyMatch(instance -> instance.curse == curse);
	}

	public void addCurse(Curse.Instance instance) {
		if (hasCurse(instance.curse)) {
			for (Curse.Instance curse : getCurses()) {
				if (curse.curse == instance.curse) {
					curse.duration = instance.duration;
					return;
				}
			}
		}
		getCurses().add(instance);
	}

	public void removeCurse(Curse curse) {
		if (hasCurse(curse)) {
			for (Curse.Instance instance : getCurses()) {
				if (instance.curse == curse) {
					instance.duration = 0;
				}
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	public ListTag toNbtCurse() {
		ListTag cursesList = new ListTag();
		for (Curse.Instance instance : getCurses()) {
			CompoundTag curseCompound = new CompoundTag();
			curseCompound.putString("Curse", BWRegistries.CURSE.getKey(instance.curse).toString());
			curseCompound.putInt("Duration", instance.duration);
			cursesList.add(curseCompound);
		}
		return cursesList;
	}
}
