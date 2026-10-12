/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin extends Level {
	protected ClientWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
	}

	@ModifyVariable(method = "playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float modifySound0(float volume, double x, double y, double z, SoundEvent sound, SoundSource category) {
		if (category != SoundSource.MASTER && category != SoundSource.VOICE) {
			LocalPlayer clientPlayer = Minecraft.getInstance().player;
			if (clientPlayer != null && clientPlayer.hasEffect(BWStatusEffects.DEAFENED.get())) {
				return Math.max(0, volume * (1 - (0.2f * (clientPlayer.getEffect(BWStatusEffects.DEAFENED.get()).getAmplifier() + 1))));
			}
		}
		return volume;
	}

	@ModifyVariable(method = "playSeededSound", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float modifySound1(float volume, @Nullable Player except, Entity entity, Holder<SoundEvent> sound, SoundSource category) {
		if (category != SoundSource.MASTER && category != SoundSource.VOICE) {
			LocalPlayer clientPlayer = Minecraft.getInstance().player;
			if (clientPlayer != null && clientPlayer.hasEffect(BWStatusEffects.DEAFENED.get())) {
				return Math.max(0, volume * (1 - (0.2f * (clientPlayer.getEffect(BWStatusEffects.DEAFENED.get()).getAmplifier() + 1))));
			}
		}
		return volume;
	}
}
