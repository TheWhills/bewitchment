/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;

/**
 * Body-part transforms for accessory renderers, ported 1:1 from Trinkets' TrinketRenderer
 * (3.7.2) so the accessories sit exactly where they did on Fabric. Curios has no equivalent
 * helpers for these anchor points.
 */
public final class AccessoryTransforms {
	private AccessoryTransforms() {
	}

	private static boolean crouching(HumanoidModel<?> model, LivingEntity entity) {
		return entity.isCrouching() && !model.riding && !entity.isSwimming();
	}

	public static void translateToChest(PoseStack matrices, HumanoidModel<?> model, LivingEntity entity) {
		if (crouching(model, entity)) {
			matrices.translate(0.0F, 0.2F, 0.0F);
			matrices.mulPose(Axis.XP.rotation(model.body.xRot));
		}
		matrices.mulPose(Axis.YP.rotation(model.body.yRot));
		matrices.translate(0.0F, 0.4F, -0.16F);
	}

	public static void translateToRightLeg(PoseStack matrices, HumanoidModel<?> model, LivingEntity entity) {
		if (crouching(model, entity)) {
			matrices.translate(0.0F, 0.0F, 0.25F);
		}
		matrices.translate(-0.125F, 0.75F, 0.0F);
		matrices.mulPose(Axis.ZP.rotation(model.rightLeg.zRot));
		matrices.mulPose(Axis.YP.rotation(model.rightLeg.yRot));
		matrices.mulPose(Axis.XP.rotation(model.rightLeg.xRot));
		matrices.translate(0.0F, 0.75F, 0.0F);
	}

	public static void translateToLeftLeg(PoseStack matrices, HumanoidModel<?> model, LivingEntity entity) {
		if (crouching(model, entity)) {
			matrices.translate(0.0F, 0.0F, 0.25F);
		}
		matrices.translate(0.125F, 0.75F, 0.0F);
		matrices.mulPose(Axis.ZP.rotation(model.leftLeg.zRot));
		matrices.mulPose(Axis.YP.rotation(model.leftLeg.yRot));
		matrices.mulPose(Axis.XP.rotation(model.leftLeg.xRot));
		matrices.translate(0.0F, 0.75F, 0.0F);
	}
}
