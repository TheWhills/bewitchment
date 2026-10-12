/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.github.ladysnake.impersonate.Impersonator;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import moriyashiine.bewitchment.common.statuseffect.PolymorphStatusEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PolymorphComponent implements ServerTickingComponent {
	private final Entity obj;
	private UUID uuid;
	private String name;

	public PolymorphComponent(Entity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (tag.contains("UUID")) {
			uuid = tag.getUUID("UUID");
			name = tag.getString("Name");
		}
	}

	@Override
	public void writeToNbt(@NotNull CompoundTag tag) {
		if (getUuid() != null) {
			tag.putUUID("UUID", uuid);
			tag.putString("Name", name);
		}
	}

	@Override
	public void serverTick() {
		if (obj instanceof Player player && getUuid() != null && !player.hasEffect(BWStatusEffects.POLYMORPH)) {
			setUuid(null);
			setName(null);
			Impersonator.get(player).stopImpersonation(PolymorphStatusEffect.IMPERSONATE_IDENTIFIER);
		}
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
