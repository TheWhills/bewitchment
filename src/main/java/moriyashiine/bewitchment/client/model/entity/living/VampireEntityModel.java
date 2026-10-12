/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.HumanoidArm;

public class VampireEntityModel<T extends VampireEntity> extends HumanoidModel<T> {
	private final ModelPart realBody;
	private final ModelPart robe;
	private final ModelPart downArms;
	private final ModelPart lArm;
	private final ModelPart rArm;
	private final ModelPart realHead;
	private final ModelPart lLeg;
	private final ModelPart rLeg;
	private final ModelPart crossedArms;

	private boolean realArm = false;

	public VampireEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		robe = root.getChild("robe");
		downArms = root.getChild("downArms");
		lArm = downArms.getChild("lArm");
		rArm = lArm.getChild("rArm");
		realHead = root.getChild("realHead");
		lLeg = root.getChild("lLeg");
		rLeg = root.getChild("rLeg");
		crossedArms = root.getChild("crossedArms");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		root.addOrReplaceChild("rLeg", CubeListBuilder.create().texOffs(0, 22).mirror(true).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("crossedArms", CubeListBuilder.create().texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F).mirror(true).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F).texOffs(40, 38).mirror(false).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));
		root.addOrReplaceChild("realBody", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition realHead = root.addOrReplaceChild("realHead", CubeListBuilder.create().addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.3F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -3.2F, -4.1F, -0.7854F, 0.0F, 0.0F));
		root.addOrReplaceChild("lLeg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("robe", CubeListBuilder.create().texOffs(0, 39).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.25F, 0.25F, 0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition downArms = root.addOrReplaceChild("downArms", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lArm = downArms.addOrReplaceChild("lArm", CubeListBuilder.create().texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(5.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lArm.addOrReplaceChild("lClaw01", CubeListBuilder.create().addBox(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(2.3F, 8.6F, -1.4F, 0.0F, 0.0F, 0.1745F));
		lArm.addOrReplaceChild("lClaw02", CubeListBuilder.create().addBox(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(2.3F, 8.8F, -0.1F, 0.0F, 0.0F, 0.1745F));
		lArm.addOrReplaceChild("lClaw03", CubeListBuilder.create().addBox(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(2.3F, 8.6F, 1.2F, 0.0F, 0.0F, 0.1745F));
		PartDefinition rArm = lArm.addOrReplaceChild("rArm", CubeListBuilder.create().texOffs(40, 46).mirror(true).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rArm.addOrReplaceChild("rClaw01", CubeListBuilder.create().mirror(true).addBox(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(-2.3F, 8.6F, -1.4F, 0.0F, 0.0F, -0.1745F));
		rArm.addOrReplaceChild("rClaw02", CubeListBuilder.create().mirror(true).addBox(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(-2.3F, 8.8F, -0.1F, 0.0F, 0.0F, -0.1745F));
		rArm.addOrReplaceChild("rClaw03", CubeListBuilder.create().mirror(true).addBox(-1.1F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(-2.3F, 8.6F, 1.2F, 0.0F, 0.0F, -0.1745F));
		return LayerDefinition.create(data, 64, 64);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		realArm = false;
		super.setupAnim(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(realHead, super.head);
		copyRotation(realBody, super.body);
		copyRotation(lArm, super.leftArm);
		copyRotation(rArm, super.rightArm);
		copyRotation(lLeg, super.leftLeg);
		copyRotation(rLeg, super.rightLeg);
		boolean hasTarget = entity.getEntityData().get(VampireEntity.HAS_TARGET);
		crossedArms.visible = !hasTarget;
		downArms.visible = hasTarget;
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		realHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		robe.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		crossedArms.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		downArms.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected ModelPart getArm(HumanoidArm arm) {
		return realArm ? (arm == HumanoidArm.LEFT ? lArm : rArm) : super.getArm(arm);
	}

	private void copyRotation(ModelPart to, ModelPart from) {
		to.xRot = from.xRot;
		to.yRot = from.yRot;
		to.zRot = from.zRot;
	}
}
