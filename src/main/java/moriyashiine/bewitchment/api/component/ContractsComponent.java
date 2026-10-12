/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import java.util.ArrayList;
import java.util.List;

public class ContractsComponent implements ServerTickingComponent {
	private final Player obj;
	private final List<Contract.Instance> contracts = new ArrayList<>();

	public ContractsComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		ListTag contractsList = tag.getList("Contracts", Tag.TAG_COMPOUND);
		for (int i = 0; i < contractsList.size(); i++) {
			CompoundTag contractCompound = contractsList.getCompound(i);
			addContract(new Contract.Instance(BWRegistries.CONTRACT.get(new ResourceLocation(contractCompound.getString("Contract"))), contractCompound.getInt("Duration"), contractCompound.getInt("Cost")));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.put("Contracts", toNbtContract());
	}

	@Override
	public void serverTick() {
		int level = 0;
		for (int i = contracts.size() - 1; i >= 0; i--) {
			Contract.Instance instance = contracts.get(i);
			level += instance.cost;
			instance.contract.tick(obj);
			instance.duration--;
			if (instance.duration <= 0) {
				contracts.remove(i);
			}
		}
		if (level > 0) {
			obj.addEffect(new MobEffectInstance(BWStatusEffects.PACT.get(), 10, level - 1, true, false));
			if (obj.getHealth() > obj.getMaxHealth()) {
				obj.setHealth(obj.getMaxHealth());
			}
		}
	}

	public List<Contract.Instance> getContracts() {
		return contracts;
	}

	public boolean hasContract(Contract contract) {
		return getContracts().stream().anyMatch(instance -> instance.contract == contract);
	}

	public void addContract(Contract.Instance instance) {
		if (hasContract(instance.contract)) {
			for (Contract.Instance contract : getContracts()) {
				if (contract.contract == instance.contract) {
					contract.duration = instance.duration;
					return;
				}
			}
		}
		getContracts().add(instance);
	}

	public void removeContract(Contract contract) {
		if (hasContract(contract)) {
			for (Contract.Instance instance : getContracts()) {
				if (instance.contract == contract) {
					instance.duration = 0;
				}
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	public ListTag toNbtContract() {
		ListTag contractsTag = new ListTag();
		for (Contract.Instance instance : getContracts()) {
			CompoundTag contractCompound = new CompoundTag();
			contractCompound.putString("Contract", BWRegistries.CONTRACT.getKey(instance.contract).toString());
			contractCompound.putInt("Duration", instance.duration);
			contractCompound.putInt("Cost", instance.cost);
			contractsTag.add(contractCompound);
		}
		return contractsTag;
	}
}
