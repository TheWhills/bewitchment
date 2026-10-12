/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import moriyashiine.bewitchment.common.component.base.AutoSyncedComponent;
import moriyashiine.bewitchment.common.component.base.ServerTickingComponent;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Who an entity is disguised as by the polymorph effect. On players this is synced so every
 * client renders the disguise (skin via ClientPolymorph, names via NameFormat/TabListNameFormat);
 * arrows and potion clouds only carry the target to whoever they hit.
 */
public class PolymorphComponent implements AutoSyncedComponent, ServerTickingComponent {
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
		} else {
			uuid = null;
			name = null;
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
		if (obj instanceof Player player && getUuid() != null && !player.hasEffect(BWStatusEffects.POLYMORPH.get())) {
			setUuid(null);
			setName(null);
			refresh(player);
		}
	}

	/** Push the current disguise (or its removal) to clients and refresh the player's names. */
	public static void refresh(Player player) {
		BWComponents.POLYMORPH_COMPONENT.sync(player);
		player.refreshDisplayName();
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.refreshTabListName();
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
