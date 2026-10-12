/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.world;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BWWorldState extends SavedData {
	public final Map<Long, NonNullList<ItemStack>> poppetShelves = new LinkedHashMap<>();
	public final Map<Long, String> witchCauldrons = new LinkedHashMap<>();

	public final List<Long> potentialCandelabras = new ArrayList<>();
	public final List<Long> potentialSigils = new ArrayList<>();
	public final List<Long> glowingBrambles = new ArrayList<>();

	public static BWWorldState readNbt(CompoundTag nbt) {
		BWWorldState worldState = new BWWorldState();
		ListTag poppetShelvesList = nbt.getList("PoppetShelves", Tag.TAG_COMPOUND);
		for (int i = 0; i < poppetShelvesList.size(); i++) {
			CompoundTag poppetShelfCompound = poppetShelvesList.getCompound(i);
			NonNullList<ItemStack> inventory = null;
			if (poppetShelfCompound.contains("Inventory")) {
				inventory = NonNullList.withSize(9, ItemStack.EMPTY);
				ContainerHelper.loadAllItems(poppetShelfCompound.getCompound("Inventory"), inventory);
			}
			worldState.poppetShelves.put(poppetShelfCompound.getLong("Pos"), inventory);
		}
		ListTag witchCauldronsList = nbt.getList("WitchCauldrons", Tag.TAG_COMPOUND);
		for (int i = 0; i < witchCauldronsList.size(); i++) {
			CompoundTag cauldronCompound = witchCauldronsList.getCompound(i);
			if (cauldronCompound.contains("Name")) {
				worldState.witchCauldrons.put(cauldronCompound.getLong("Pos"), cauldronCompound.getString("Name"));
			}
		}
		ListTag potentialCandelabrasList = nbt.getList("PotentialCandelabras", Tag.TAG_COMPOUND);
		for (int i = 0; i < potentialCandelabrasList.size(); i++) {
			CompoundTag posCompound = potentialCandelabrasList.getCompound(i);
			worldState.potentialCandelabras.add(posCompound.getLong("Pos"));
		}
		ListTag potentialSigilsList = nbt.getList("PotentialSigils", Tag.TAG_COMPOUND);
		for (int i = 0; i < potentialSigilsList.size(); i++) {
			CompoundTag posCompound = potentialSigilsList.getCompound(i);
			worldState.potentialSigils.add(posCompound.getLong("Pos"));
		}
		ListTag glowingBramblesList = nbt.getList("GlowingBrambles", Tag.TAG_COMPOUND);
		for (int i = 0; i < glowingBramblesList.size(); i++) {
			CompoundTag posCompound = glowingBramblesList.getCompound(i);
			worldState.glowingBrambles.add(posCompound.getLong("Pos"));
		}
		return worldState;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		ListTag poppetShelvesList = new ListTag();
		this.poppetShelves.forEach((pos, inventory) -> {
			CompoundTag poppetShelfCompound = new CompoundTag();
			poppetShelfCompound.putLong("Pos", pos);
			if (inventory != null) {
				poppetShelfCompound.put("Inventory", ContainerHelper.saveAllItems(new CompoundTag(), inventory));
			}
			poppetShelvesList.add(poppetShelfCompound);
		});
		nbt.put("PoppetShelves", poppetShelvesList);
		ListTag witchCauldronsList = new ListTag();
		this.witchCauldrons.forEach((pos, name) -> {
			CompoundTag cauldronCompound = new CompoundTag();
			cauldronCompound.putLong("Pos", pos);
			cauldronCompound.putString("Name", name);
			witchCauldronsList.add(cauldronCompound);
		});
		nbt.put("WitchCauldrons", witchCauldronsList);
		ListTag potentialCandelabrasList = new ListTag();
		for (long pos : this.potentialCandelabras) {
			CompoundTag posCompound = new CompoundTag();
			posCompound.putLong("Pos", pos);
			potentialCandelabrasList.add(posCompound);
		}
		nbt.put("PotentialCandelabras", potentialCandelabrasList);
		ListTag potentialSigilsList = new ListTag();
		for (long pos : this.potentialSigils) {
			CompoundTag posCompound = new CompoundTag();
			posCompound.putLong("Pos", pos);
			potentialSigilsList.add(posCompound);
		}
		nbt.put("PotentialSigils", potentialSigilsList);
		ListTag glowingBramblesList = new ListTag();
		for (long pos : this.glowingBrambles) {
			CompoundTag posCompound = new CompoundTag();
			posCompound.putLong("Pos", pos);
			glowingBramblesList.add(posCompound);
		}
		nbt.put("GlowingBrambles", glowingBramblesList);
		return nbt;
	}

	public static BWWorldState get(Level world) {
		return ((ServerLevel) world).getDataStorage().computeIfAbsent(BWWorldState::readNbt, BWWorldState::new, Bewitchment.MOD_ID + "_universal");
	}
}
