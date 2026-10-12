/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.client.misc.ClientPolymorph;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Polymorphed players render with their disguise's skin and arm model (Impersonate did this on Fabric). */
@Mixin(AbstractClientPlayer.class)
public abstract class PolymorphSkinMixin {
	@Inject(method = "getSkinTextureLocation", at = @At("HEAD"), cancellable = true)
	private void bewitchment$polymorphSkin(CallbackInfoReturnable<ResourceLocation> cir) {
		ClientPolymorph.Skin skin = ClientPolymorph.get((AbstractClientPlayer) (Object) this);
		if (skin != null) {
			cir.setReturnValue(skin.texture());
		}
	}

	@Inject(method = "getModelName", at = @At("HEAD"), cancellable = true)
	private void bewitchment$polymorphModel(CallbackInfoReturnable<String> cir) {
		ClientPolymorph.Skin skin = ClientPolymorph.get((AbstractClientPlayer) (Object) this);
		if (skin != null) {
			cir.setReturnValue(skin.model());
		}
	}
}
