/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client-side skins for polymorphed players. The first time a disguise is seen its skin is
 * requested from the vanilla SkinManager (which fetches the profile textures off-thread);
 * until it arrives the player keeps their own skin.
 */
public final class ClientPolymorph {
	private static final Map<UUID, Skin> SKINS = new ConcurrentHashMap<>();
	private static final Skin PENDING = new Skin(null, null);

	private ClientPolymorph() {
	}

	/** The disguise skin for {@code player}, or {@code null} if they are not polymorphed or it is not loaded yet. */
	@Nullable
	public static Skin get(Player player) {
		return BWComponents.POLYMORPH_COMPONENT.maybeGet(player).map(polymorph -> {
			UUID uuid = polymorph.getUuid();
			if (uuid == null) {
				return null;
			}
			Skin skin = SKINS.computeIfAbsent(uuid, id -> {
				GameProfile profile = new GameProfile(id, polymorph.getName());
				Minecraft.getInstance().getSkinManager().registerSkins(profile, (type, location, texture) -> {
					if (type == MinecraftProfileTexture.Type.SKIN) {
						String model = texture.getMetadata("model");
						SKINS.put(id, new Skin(location, model == null ? "default" : model));
					}
				}, false);
				return PENDING;
			});
			return skin == PENDING ? null : skin;
		}).orElse(null);
	}

	public record Skin(ResourceLocation texture, String model) {
	}
}
