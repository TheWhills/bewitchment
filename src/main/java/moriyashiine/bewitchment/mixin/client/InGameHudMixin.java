/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.BloodComponent;
import moriyashiine.bewitchment.api.component.MagicComponent;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
	@Unique
	private static final ResourceLocation BEWITCHMENT_GUI_ICONS_TEXTURE = Bewitchment.id("textures/gui/icons.png");
	@Unique
	private static final ResourceLocation EMPTY_TEXTURE = Bewitchment.id("textures/gui/empty.png");

	@Unique
	private static boolean hidden = false;

	@Shadow
	@Final
	private Minecraft client;

	@Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 2, target = "Lnet/minecraft/client/Minecraft;getProfiler()Lnet/minecraft/util/profiling/ProfilerFiller;"))
	private void renderPre(GuiGraphics context, CallbackInfo ci) {
		BWComponents.MAGIC_COMPONENT.maybeGet(client.player).ifPresent(magicComponent -> {
			if (magicComponent.getMagicTimer() > 0) {
				RenderSystem.setShaderColor(1, 1, 1, magicComponent.getMagicTimer() / 10f);
				context.blit(BEWITCHMENT_GUI_ICONS_TEXTURE, 13, (context.guiHeight() - 74) / 2, 25, 0, 7, 74);
				context.blit(BEWITCHMENT_GUI_ICONS_TEXTURE, 13, (context.guiHeight() - 74) / 2, 32, 0, 7, (int) (74 - (magicComponent.getMagic() * 74f / MagicComponent.MAX_MAGIC)));
				context.blit(BEWITCHMENT_GUI_ICONS_TEXTURE, 4, (context.guiHeight() - 102) / 2, 0, 0, 25, 102);
				RenderSystem.setShaderColor(1, 1, 1, 1);
			}
		});
		if (BewitchmentAPI.isVampire(client.player, true)) {
			hidden = true;
			drawBlood(context, client.player, (int) (context.guiWidth() / 2F + 82), context.guiHeight() - 39, 10);
			if (client.player.isShiftKeyDown() && client.player.canBeSeenByAnyone()) {
				if (client.crosshairPickEntity instanceof LivingEntity living && living.getType().is(BWTags.HAS_BLOOD)) {
					drawBlood(context, living, (int) (context.guiWidth() / 2F + 12), (int) (context.guiHeight() / 2F + 9), 5);
				}
			}
		}
	}

	@ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 3))
	private ResourceLocation bewitchment$vampireHideFood0(ResourceLocation value) {
		if (hidden) {
			return EMPTY_TEXTURE;
		}
		return value;
	}

	@ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 4))
	private ResourceLocation bewitchment$vampireHideFood1(ResourceLocation value) {
		if (hidden) {
			return EMPTY_TEXTURE;
		}
		return value;
	}

	@ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 5))
	private ResourceLocation bewitchment$vampireHideFood2(ResourceLocation value) {
		if (hidden) {
			return EMPTY_TEXTURE;
		}
		return value;
	}

	@Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getProfiler()Lnet/minecraft/util/profiling/ProfilerFiller;", ordinal = 3, shift = At.Shift.BEFORE))
	private void bewitchment$vampireShowHunger(GuiGraphics context, CallbackInfo ci) {
		hidden = false;
	}

	@Unique
	private static void drawBlood(GuiGraphics context, LivingEntity living, int xPos, int yPos, int droplets) {
		int v = living.hasEffect(MobEffects.HUNGER) ? 9 : 0;
		float blood = ((float) BWComponents.BLOOD_COMPONENT.get(living).getBlood() / BloodComponent.MAX_BLOOD * droplets);
		int full = (int) blood;
		for (int i = 0; i < full; i++) {
			context.blit(BEWITCHMENT_GUI_ICONS_TEXTURE, xPos - i * 8, yPos, 39, v, 9, 9);
		}
		if (full < droplets) {
			float remaining = blood - full;
			context.blit(BEWITCHMENT_GUI_ICONS_TEXTURE, xPos - full * 8, yPos, remaining > 5 / 6f ? 48 : remaining > 4 / 6f ? 57 : remaining > 3 / 6f ? 66 : remaining > 2 / 6f ? 75 : remaining > 1 / 6f ? 84 : remaining > 0 ? 93 : 102, v, 9, 9);
		}
		for (int i = (full + 1); i < droplets; i++) {
			context.blit(BEWITCHMENT_GUI_ICONS_TEXTURE, xPos - i * 8, yPos, 102, v, 9, 9);
		}
	}
}
