/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class GhostEntityModel<T extends GhostEntity> extends HumanoidModel<T> {
	private final ModelPart realBody;
	private final ModelPart realBodyTrail00;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart realHead;

	private boolean realArm = false;

	public GhostEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		realBodyTrail00 = realBody.getChild("realBodyTrail00");
		BipedLeftArm = root.getChild("BipedLeftArm");
		BipedRightArm = root.getChild("BipedRightArm");
		realHead = root.getChild("realHead");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition BipedRightArm = root.addOrReplaceChild("BipedRightArm", CubeListBuilder.create().texOffs(40, 16).mirror(true).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, -1.3963F, 0.0F, 0.1F));
		BipedRightArm.addOrReplaceChild("rArmWisp", CubeListBuilder.create().texOffs(40, 34).mirror(true).addBox(-1.5F, -4.5F, 0.1F, 3.0F, 11.0F, 4.0F), PartPose.offsetAndRotation(-1.0F, 2.7F, 1.7F, 0.0F, 0.0F, 0.0F));
		PartDefinition realHead = root.addOrReplaceChild("realHead", CubeListBuilder.create().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition skull = realHead.addOrReplaceChild("skull", CubeListBuilder.create().texOffs(34, 0).addBox(-3.5F, -7.5F, -3.0F, 7.0F, 5.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		skull.addOrReplaceChild("skullJaw", CubeListBuilder.create().texOffs(19, 30).addBox(-2.5F, -1.0F, -3.5F, 5.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -1.9F, 0.9F, 0.1745F, 0.0F, 0.0F));
		PartDefinition BipedLeftArm = root.addOrReplaceChild("BipedLeftArm", CubeListBuilder.create().texOffs(40, 16).mirror(true).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 13.0F, 4.0F), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -1.3963F, 0.0F, -0.1F));
		BipedLeftArm.addOrReplaceChild("lArmWisp", CubeListBuilder.create().texOffs(40, 34).mirror(true).addBox(-1.5F, -4.5F, 0.1F, 3.0F, 11.0F, 4.0F), PartPose.offsetAndRotation(1.0F, 2.7F, 1.7F, 0.0F, 0.0F, 0.0F));
		PartDefinition realBody = root.addOrReplaceChild("realBody", CubeListBuilder.create().texOffs(14, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2094F, 0.0F, 0.0F));
		PartDefinition realBodyTrail00 = realBody.addOrReplaceChild("realBodyTrail00", CubeListBuilder.create().texOffs(33, 51).addBox(-4.5F, 0.0F, -2.5F, 9.0F, 6.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 7.7F, 0.0F, 0.1047F, 0.0F, 0.0F));
		realBodyTrail00.addOrReplaceChild("realBodyTrail01", CubeListBuilder.create().texOffs(0, 39).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 9.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 5.8F, 0.1F, 0.1047F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		realArm = false;
		super.setupAnim(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(realHead, super.head);
		realBodyTrail00.xRot = Mth.sin(animationProgress / 12) / 6;
		AnimationUtils.animateZombieArms(leftArm, rightArm, false, entity.attackAnim, animationProgress);
		if (entity.getEntityData().get(GhostEntity.HAS_TARGET)) {
			rightArm.xRot += 4.5;
			rightArm.zRot = Mth.sin(animationProgress) / 2;
			leftArm.xRot += 4.5;
			leftArm.zRot = -rightArm.zRot;
		} else {
			leftArm.zRot = -0.1f;
			rightArm.zRot = 0.1f;
		}
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		realHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		leftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected ModelPart getArm(HumanoidArm arm) {
		return realArm ? (arm == HumanoidArm.LEFT ? BipedLeftArm : BipedRightArm) : super.getArm(arm);
	}

	private void copyRotation(ModelPart to, ModelPart from) {
		to.xRot = from.xRot;
		to.yRot = from.yRot;
		to.zRot = from.zRot;
	}
}
