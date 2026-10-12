/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BWUniversalWorldState extends SavedData {
	public final List<UUID> pledgesToRemove = new ArrayList<>();
	public final List<Tuple<UUID, CompoundTag>> familiars = new ArrayList<>();

	public static BWUniversalWorldState readNbt(CompoundTag nbt) {
		BWUniversalWorldState universalWorldState = new BWUniversalWorldState();
		ListTag pledgesToRemoveList = nbt.getList("PledgesToRemove", Tag.TAG_COMPOUND);
		for (int i = 0; i < pledgesToRemoveList.size(); i++) {
			universalWorldState.pledgesToRemove.add(pledgesToRemoveList.getCompound(i).getUUID("UUID"));
		}
		ListTag familiarsList = nbt.getList("Familiars", Tag.TAG_COMPOUND);
		for (int i = 0; i < familiarsList.size(); i++) {
			CompoundTag familiarCompound = familiarsList.getCompound(i);
			universalWorldState.familiars.add(new Tuple<>(familiarCompound.getUUID("Player"), familiarCompound.getCompound("Familiar")));
		}
		return universalWorldState;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		ListTag pledgesToRemoveList = new ListTag();
		for (UUID uuid : this.pledgesToRemove) {
			CompoundTag pledgeCompound = new CompoundTag();
			pledgeCompound.putUUID("UUID", uuid);
			pledgesToRemoveList.add(pledgeCompound);
		}
		nbt.put("PledgesToRemove", pledgesToRemoveList);
		ListTag familiarsList = new ListTag();
		for (Tuple<UUID, CompoundTag> pair : this.familiars) {
			CompoundTag familiarCompound = new CompoundTag();
			familiarCompound.putUUID("Player", pair.getA());
			familiarCompound.put("Familiar", pair.getB());
			familiarsList.add(familiarCompound);
		}
		nbt.put("Familiars", familiarsList);
		return nbt;
	}

	@SuppressWarnings("ConstantConditions")
	public static BWUniversalWorldState get(Level world) {
		return world.getServer().overworld().getDataStorage().computeIfAbsent(BWUniversalWorldState::readNbt, BWUniversalWorldState::new, Bewitchment.MOD_ID);
	}
}
