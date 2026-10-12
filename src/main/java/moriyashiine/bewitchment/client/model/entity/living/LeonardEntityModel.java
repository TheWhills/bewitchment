/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class LeonardEntityModel<T extends LeonardEntity> extends HumanoidModel<T> {
	private static final ItemStack SCEPTER = new ItemStack(BWObjects.SCEPTER);

	private final ModelPart realBody;
	private final ModelPart frontLoincloth00;
	private final ModelPart backLoincloth00;
	private final ModelPart realHead;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;

	private boolean realArm = false;

	public LeonardEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		frontLoincloth00 = realBody.getChild("stomach").getChild("frontLoincloth00");
		backLoincloth00 = realBody.getChild("stomach").getChild("backLoincloth00");
		realHead = root.getChild("realHead");
		BipedLeftArm = root.getChild("BipedLeftArm");
		BipedRightArm = root.getChild("BipedRightArm");
		BipedLeftLeg = root.getChild("BipedLeftLeg");
		BipedRightLeg = root.getChild("BipedRightLeg");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition realBody = root.addOrReplaceChild("realBody", CubeListBuilder.create().texOffs(20, 16).addBox(-4.5F, 0.0F, -2.5F, 9.0F, 7.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -9.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		realBody.addOrReplaceChild("lPec", CubeListBuilder.create().texOffs(14, 42).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 3.0F), PartPose.offsetAndRotation(2.2F, 3.1F, -0.1F, 0.0F, -0.0873F, 0.0873F));
		realBody.addOrReplaceChild("rPec", CubeListBuilder.create().texOffs(14, 42).mirror(true).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 3.0F), PartPose.offsetAndRotation(-2.2F, 3.1F, -0.1F, 0.0F, 0.0873F, -0.0873F));
		PartDefinition stomach = realBody.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(17, 29).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 6.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		stomach.addOrReplaceChild("frontLoincloth00", CubeListBuilder.create().texOffs(16, 54).addBox(-4.5F, 0.0F, -0.5F, 9.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 6.5F, -2.0F, 0.0F, 0.0F, 0.0F));
		stomach.addOrReplaceChild("backLoincloth00", CubeListBuilder.create().texOffs(39, 51).addBox(-4.5F, 0.0F, -1.4F, 9.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 6.5F, 1.1F, 0.0F, 0.0F, 0.0F));
		PartDefinition realHead = root.addOrReplaceChild("realHead", CubeListBuilder.create().texOffs(1, 2).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F), PartPose.offsetAndRotation(0.0F, -9.4F, -0.2F, 0.0F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(29, 2).addBox(-2.0F, -1.9F, -5.1F, 4.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -4.6F, -2.5F, 0.5236F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("jawUpper00", CubeListBuilder.create().texOffs(43, 0).addBox(-2.5F, -1.0F, -5.0F, 5.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -2.5F, -2.2F, 0.0698F, 0.0F, 0.0F));
		PartDefinition jawLower = realHead.addOrReplaceChild("jawLower", CubeListBuilder.create().texOffs(30, 10).addBox(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -1.0F, -3.0F, -0.0349F, 0.0F, 0.0F));
		jawLower.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(0, 53).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.3F, -2.4F, -0.0349F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("lEar", CubeListBuilder.create().texOffs(37, 29).mirror(true).addBox(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(2.6F, -5.0F, 0.8F, -0.5236F, 0.0F, -0.5236F));
		realHead.addOrReplaceChild("rEar", CubeListBuilder.create().texOffs(37, 29).mirror(true).addBox(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-2.6F, -5.0F, 0.8F, -0.5236F, 0.0F, 0.5236F));
		PartDefinition lHorn01 = realHead.addOrReplaceChild("lHorn01", CubeListBuilder.create().texOffs(25, 0).mirror(true).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(2.0F, -6.0F, -0.2F, -0.6109F, 0.0F, 0.5236F));
		PartDefinition lHorn02 = lHorn01.addOrReplaceChild("lHorn02", CubeListBuilder.create().texOffs(25, 1).mirror(true).addBox(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, -0.3142F, 0.0F, 0.2094F));
		PartDefinition lHorn03 = lHorn02.addOrReplaceChild("lHorn03", CubeListBuilder.create().texOffs(0, 1).mirror(true).addBox(-0.5F, -2.8F, -0.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(0.0F, -2.6F, 0.0F, -0.3142F, 0.0F, 0.0F));
		lHorn03.addOrReplaceChild("lHorn04", CubeListBuilder.create().mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.4014F, 0.0F, 0.0F));
		PartDefinition rHorn01 = realHead.addOrReplaceChild("rHorn01", CubeListBuilder.create().texOffs(25, 0).mirror(true).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, -6.0F, -0.2F, -0.6109F, 0.0F, -0.5236F));
		PartDefinition rHorn02 = rHorn01.addOrReplaceChild("rHorn02", CubeListBuilder.create().texOffs(25, 1).mirror(true).addBox(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, -0.3142F, 0.0F, -0.2094F));
		PartDefinition rHorn03 = rHorn02.addOrReplaceChild("rHorn03", CubeListBuilder.create().texOffs(0, 1).mirror(true).addBox(-0.5F, -2.8F, -0.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(0.0F, -2.6F, 0.0F, -0.3142F, 0.0F, 0.0F));
		rHorn03.addOrReplaceChild("rHorn04", CubeListBuilder.create().mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.4014F, 0.0F, 0.0F));
		PartDefinition mHorn01 = realHead.addOrReplaceChild("mHorn01", CubeListBuilder.create().texOffs(25, 1).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -6.0F, 0.4F, -0.8727F, 0.0F, 0.0F));
		PartDefinition mHorn02 = mHorn01.addOrReplaceChild("mHorn02", CubeListBuilder.create().texOffs(25, 0).addBox(-1.0F, -2.9F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -3.7F, 0.0F, -0.4538F, 0.0F, 0.0F));
		PartDefinition mHorn03 = mHorn02.addOrReplaceChild("mHorn03", CubeListBuilder.create().texOffs(0, 1).addBox(-0.5F, -2.8F, -0.2F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(0.0F, -3.8F, 0.0F, -0.3142F, 0.0F, 0.0F));
		mHorn03.addOrReplaceChild("mHorn04", CubeListBuilder.create().addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition BipedRightLeg = root.addOrReplaceChild("BipedRightLeg", CubeListBuilder.create().texOffs(0, 16).mirror(true).addBox(-2.1F, 0.0F, -2.5F, 4.0F, 10.0F, 5.0F), PartPose.offsetAndRotation(-2.1F, 4.5F, 0.1F, 0.0F, 0.0F, 0.0F));
		PartDefinition rLeg02 = BipedRightLeg.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(0, 31).mirror(true).addBox(-2.0F, -0.1F, -2.0F, 4.0F, 10.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 9.7F, 0.0F, 0.0698F, 0.0F, -0.0349F));
		rLeg02.addOrReplaceChild("rFlipper01", CubeListBuilder.create().texOffs(30, 42).mirror(true).addBox(-2.0F, 0.0F, -3.0F, 3.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(-0.3F, 8.8F, -1.7F, 0.0F, 0.1745F, -0.0349F));
		rLeg02.addOrReplaceChild("rFlipper02", CubeListBuilder.create().texOffs(28, 48).mirror(true).addBox(-0.9F, 0.0F, -3.0F, 2.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.9F, 8.8F, -1.7F, 0.0F, -0.1396F, 0.0349F));
		PartDefinition BipedLeftArm = root.addOrReplaceChild("BipedLeftArm", CubeListBuilder.create().texOffs(49, 19).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), PartPose.offsetAndRotation(5.5F, -7.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		BipedLeftArm.addOrReplaceChild("lClaws", CubeListBuilder.create().texOffs(0, 45).addBox(-1.1F, 0.0F, -2.1F, 2.0F, 3.0F, 4.0F), PartPose.offsetAndRotation(1.4F, 12.9F, -0.1F, 0.0F, 0.0F, 0.2793F));
		BipedLeftArm.addOrReplaceChild("lShoulderPoof", CubeListBuilder.create().texOffs(46, 40).addBox(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(0.4F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0F));
		BipedLeftArm.addOrReplaceChild("lSleeve", CubeListBuilder.create().texOffs(46, 9).addBox(-1.52F, -2.5F, -2.0F, 3.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(0.5F, 9.5F, 2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition BipedRightArm = root.addOrReplaceChild("BipedRightArm", CubeListBuilder.create().texOffs(49, 19).mirror(true).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), PartPose.offsetAndRotation(-5.5F, -7.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		BipedRightArm.addOrReplaceChild("rClaws", CubeListBuilder.create().texOffs(0, 45).mirror(true).addBox(-1.1F, 0.0F, -2.1F, 2.0F, 3.0F, 4.0F), PartPose.offsetAndRotation(-1.4F, 12.9F, -0.1F, 0.0F, 0.0F, -0.2793F));
		BipedRightArm.addOrReplaceChild("rShoulderPoof", CubeListBuilder.create().texOffs(46, 40).mirror(true).addBox(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(-0.4F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0F));
		BipedRightArm.addOrReplaceChild("rSleeve", CubeListBuilder.create().texOffs(46, 9).mirror(true).addBox(-1.48F, -2.5F, -2.0F, 3.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(-0.5F, 9.5F, 2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition BipedLeftLeg = root.addOrReplaceChild("BipedLeftLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.9F, 0.0F, -2.5F, 4.0F, 10.0F, 5.0F), PartPose.offsetAndRotation(2.1F, 4.5F, 0.1F, -0.0698F, 0.0F, -0.0349F));
		PartDefinition lLeg02 = BipedLeftLeg.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(0, 31).addBox(-2.0F, -0.1F, -2.0F, 4.0F, 10.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 9.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		lLeg02.addOrReplaceChild("lFlipper01", CubeListBuilder.create().texOffs(30, 42).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.3F, 8.8F, -1.7F, 0.0F, -0.1745F, 0.0349F));
		lLeg02.addOrReplaceChild("lFlipper02", CubeListBuilder.create().texOffs(28, 48).addBox(-1.1F, 0.0F, -3.0F, 2.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(-0.9F, 8.8F, -1.7F, 0.0F, 0.1396F, -0.0349F));
		return LayerDefinition.create(data, 64, 64);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		entity.setItemInHand(InteractionHand.MAIN_HAND, SCEPTER);
		realArm = false;
		super.setupAnim(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(realHead, super.head);
		copyRotation(realBody, super.body);
		copyRotation(BipedLeftArm, super.leftArm);
		BipedLeftArm.zRot -= 0.1f;
		copyRotation(BipedRightArm, super.rightArm);
		BipedRightArm.zRot += 0.1f;
		copyRotation(BipedLeftLeg, super.leftLeg);
		BipedLeftLeg.xRot /= 2;
		BipedLeftLeg.xRot -= 0.0698f;
		BipedLeftLeg.zRot -= 0.0349f;
		copyRotation(BipedRightLeg, super.rightLeg);
		BipedRightLeg.xRot /= 2;
		BipedRightLeg.xRot -= 0.0698f;
		BipedRightLeg.zRot += 0.0349f;
		frontLoincloth00.xRot = Math.min(BipedLeftLeg.xRot, BipedRightLeg.xRot) - 0.1745f;
		backLoincloth00.xRot = Math.max(BipedLeftLeg.xRot, BipedRightLeg.xRot);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		realHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedLeftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedRightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedLeftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		BipedRightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
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
