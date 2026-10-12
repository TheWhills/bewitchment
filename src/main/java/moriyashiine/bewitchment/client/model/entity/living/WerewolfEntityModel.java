/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
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

public class WerewolfEntityModel<T extends WerewolfEntity> extends HumanoidModel<T> {
	private final ModelPart realBody;
	private final ModelPart tail01;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;
	private final ModelPart neck;
	private final ModelPart lArm01;
	private final ModelPart rArm01;

	private boolean realArm = false;

	public WerewolfEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		tail01 = realBody.getChild("stomach").getChild("tail01");
		BipedLeftLeg = realBody.getChild("stomach").getChild("BipedLeftLeg");
		BipedRightLeg = realBody.getChild("stomach").getChild("BipedRightLeg");
		neck = root.getChild("neck");
		lArm01 = root.getChild("lArm01");
		rArm01 = root.getChild("rArm01");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition realBody = root.addOrReplaceChild("realBody", CubeListBuilder.create().texOffs(49, 15).addBox(-5.5F, -3.3F, -2.0F, 11.0F, 11.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5672F, 0.0F, 0.0F));
		realBody.addOrReplaceChild("realHead", CubeListBuilder.create().texOffs(1, 2).addBox(-0.5F, 0.7F, -0.4F, 1.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition stomach = realBody.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(16, 16).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 12.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 6.9F, 2.6F, -0.3927F, 0.0F, 0.0F));
		PartDefinition tail01 = stomach.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(112, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 8.6F, 2.0F, 0.1047F, 0.0F, 0.0F));
		PartDefinition tail02 = tail01.addOrReplaceChild("tail02", CubeListBuilder.create().texOffs(111, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 3.7F, 0.0F, -0.2094F, 0.0F, 0.0F));
		PartDefinition tail03 = tail02.addOrReplaceChild("tail03", CubeListBuilder.create().texOffs(112, 40).addBox(-1.5F, 0.2F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 6.5F, 0.1F, 0.1396F, 0.0F, 0.0F));
		tail03.addOrReplaceChild("tail04", CubeListBuilder.create().texOffs(116, 7).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.4F, 0.0698F, 0.0F, 0.0F));
		stomach.addOrReplaceChild("fur06", CubeListBuilder.create().texOffs(90, 52).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 5.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 6.6F, 1.5F, 0.6981F, 0.0F, 0.0F));
		stomach.addOrReplaceChild("fur05", CubeListBuilder.create().texOffs(90, 43).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 5.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 4.7F, 1.6F, 0.6981F, 0.0F, 0.0F));
		PartDefinition BipedLeftLeg = stomach.addOrReplaceChild("BipedLeftLeg", CubeListBuilder.create().texOffs(11, 46).addBox(-2.3F, -1.1F, -1.9F, 5.0F, 13.0F, 5.0F), PartPose.offsetAndRotation(2.7F, 10.2F, -0.4F, 0.0F, 0.0F, 0.0F));
		PartDefinition lLeg02 = BipedLeftLeg.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(0, 35).addBox(-2.01F, 0.4F, -2.0F, 4.0F, 7.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 9.8F, 0.5F, 1.309F, -0.0524F, 0.0F));
		PartDefinition lLeg03 = lLeg02.addOrReplaceChild("lLeg03", CubeListBuilder.create().texOffs(0, 22).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 5.9F, 0.8F, -0.7854F, 0.0F, 0.0873F));
		PartDefinition lFoot = lLeg03.addOrReplaceChild("lFoot", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 8.7F, -1.2F, 0.1309F, 0.0436F, -0.0087F));
		lFoot.addOrReplaceChild("lFootClaw01", CubeListBuilder.create().texOffs(1, 48).addBox(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(-1.3F, 0.5F, -2.6F, 0.2269F, 0.1047F, 0.0F));
		lFoot.addOrReplaceChild("lFootClaw02", CubeListBuilder.create().texOffs(1, 48).addBox(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.5F, -2.6F, 0.2269F, 0.0F, 0.0F));
		lFoot.addOrReplaceChild("lFootClaw03", CubeListBuilder.create().texOffs(1, 48).addBox(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(1.3F, 0.5F, -2.6F, 0.2269F, -0.1047F, 0.0F));
		PartDefinition BipedRightLeg = stomach.addOrReplaceChild("BipedRightLeg", CubeListBuilder.create().texOffs(11, 46).mirror(true).addBox(-2.7F, -1.1F, -1.9F, 5.0F, 13.0F, 5.0F), PartPose.offsetAndRotation(-2.7F, 10.2F, -0.4F, 0.0F, 0.0F, 0.0F));
		PartDefinition rLeg02 = BipedRightLeg.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(0, 35).mirror(true).addBox(-1.99F, 0.4F, -2.0F, 4.0F, 7.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 9.8F, 0.5F, 1.309F, 0.0524F, 0.0F));
		PartDefinition rLeg03 = rLeg02.addOrReplaceChild("rLeg03", CubeListBuilder.create().texOffs(0, 22).mirror(true).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 5.9F, 0.8F, -0.7854F, 0.0F, -0.0873F));
		PartDefinition rFoot = rLeg03.addOrReplaceChild("rFoot", CubeListBuilder.create().texOffs(0, 14).mirror(true).addBox(-2.0F, 0.0F, -2.8F, 4.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 8.7F, -1.2F, 0.1309F, -0.0436F, 0.0087F));
		rFoot.addOrReplaceChild("rFootClaw01", CubeListBuilder.create().texOffs(1, 48).mirror(true).addBox(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(1.3F, 0.5F, -2.6F, 0.2269F, -0.1047F, 0.0F));
		rFoot.addOrReplaceChild("rFootClaw02", CubeListBuilder.create().texOffs(1, 48).mirror(true).addBox(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.5F, -2.6F, 0.2269F, 0.0F, 0.0F));
		rFoot.addOrReplaceChild("rFootClaw03", CubeListBuilder.create().texOffs(1, 48).mirror(true).addBox(-0.5F, -0.5F, -1.7F, 1.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(-1.3F, 0.5F, -2.6F, 0.2269F, 0.1047F, 0.0F));
		realBody.addOrReplaceChild("fur04", CubeListBuilder.create().texOffs(90, 35).addBox(-2.5F, 1.0F, 0.0F, 5.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -0.3F, 4.9F, 0.4363F, 0.0F, 0.0F));
		realBody.addOrReplaceChild("fur03", CubeListBuilder.create().texOffs(90, 24).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 7.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -2.6F, 4.5F, 0.6807F, 0.0F, 0.0F));
		realBody.addOrReplaceChild("fur02", CubeListBuilder.create().texOffs(90, 11).addBox(-5.0F, -1.0F, -1.0F, 10.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -4.7F, 3.4F, 0.8727F, 0.0F, 0.0F));
		PartDefinition lArm01 = root.addOrReplaceChild("lArm01", CubeListBuilder.create().texOffs(32, 47).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lArm02 = lArm01.addOrReplaceChild("lArm02", CubeListBuilder.create().texOffs(49, 46).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 13.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 11.5F, 0.0F, -0.5236F, 0.0F, 0.1484F));
		lArm02.addOrReplaceChild("lArmFur", CubeListBuilder.create().texOffs(62, 50).addBox(-0.5F, -0.5F, 1.5F, 1.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(0.4F, -4.0F, 0.0F, -0.4363F, 0.0873F, 0.0873F));
		PartDefinition lClawJoint = lArm02.addOrReplaceChild("lClawJoint", CubeListBuilder.create().addBox(-0.4F, 1.5F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 8.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		lClawJoint.addOrReplaceChild("lClaw01", CubeListBuilder.create().texOffs(27, 0).addBox(-1.4F, 1.2F, -1.6F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.2F, 0.0F, -0.1047F, 0.0F, 0.2269F));
		lClawJoint.addOrReplaceChild("lClaw02", CubeListBuilder.create().texOffs(27, 0).addBox(-1.4F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.2F, -0.1F, 0.0F, 0.0F, 0.2269F));
		lClawJoint.addOrReplaceChild("lClaw03", CubeListBuilder.create().texOffs(27, 0).addBox(-1.4F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.2F, 0.8F, 0.1047F, 0.0F, 0.2269F));
		PartDefinition rArm01 = root.addOrReplaceChild("rArm01", CubeListBuilder.create().texOffs(32, 47).mirror(true).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rArm02 = rArm01.addOrReplaceChild("rArm02", CubeListBuilder.create().texOffs(49, 46).mirror(true).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 13.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 11.5F, 0.0F, -0.5236F, 0.0F, -0.1484F));
		rArm02.addOrReplaceChild("rArmFur", CubeListBuilder.create().texOffs(62, 50).mirror(true).addBox(-0.5F, -0.5F, 1.5F, 1.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(-0.4F, -4.0F, 0.0F, -0.4363F, -0.0873F, -0.0873F));
		PartDefinition rClawJoint = rArm02.addOrReplaceChild("rClawJoint", CubeListBuilder.create().mirror(true).addBox(-0.6F, 1.5F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 8.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		rClawJoint.addOrReplaceChild("rClaw01", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-0.6F, 1.2F, -1.6F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.2F, 0.0F, -0.1047F, 0.0F, -0.2269F));
		rClawJoint.addOrReplaceChild("rClaw02", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-0.6F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.2F, -0.1F, 0.0F, 0.0F, -0.2269F));
		rClawJoint.addOrReplaceChild("rClaw03", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-0.6F, 1.2F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.2F, 0.8F, 0.1047F, 0.0F, -0.2269F));
		PartDefinition neck = root.addOrReplaceChild("neck", CubeListBuilder.create().addBox(-3.5F, -1.5F, -1.0F, 7.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2531F, 0.0F, 0.0F));
		PartDefinition realHead2 = neck.addOrReplaceChild("realHead2", CubeListBuilder.create().texOffs(44, 0).addBox(-4.0F, -3.0F, -4.4F, 8.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(0.0F, -0.5F, -3.6F, 1.9199F, 0.0F, 0.0F));
		PartDefinition jawUpperLeft = realHead2.addOrReplaceChild("jawUpperLeft", CubeListBuilder.create().texOffs(20, 36).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 5.0F, 2.0F), PartPose.offsetAndRotation(1.2F, -5.1F, -1.5F, 0.0F, 0.0F, -0.1396F));
		jawUpperLeft.addOrReplaceChild("upperTeethLeft01", CubeListBuilder.create().texOffs(56, 37).addBox(-0.5F, 1.0F, -1.8F, 1.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(0.4F, -2.8F, -1.0F, 0.0F, 0.0F, 0.0F));
		jawUpperLeft.addOrReplaceChild("upperTeethLeft02", CubeListBuilder.create().texOffs(63, 38).addBox(-2.27F, 1.4F, -1.4F, 3.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -3.2F, -1.0F, 0.0F, 0.0F, 0.1367F));
		PartDefinition jawUpperRight = realHead2.addOrReplaceChild("jawUpperRight", CubeListBuilder.create().texOffs(20, 36).mirror(true).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 5.0F, 2.0F), PartPose.offsetAndRotation(-1.2F, -5.1F, -1.5F, 0.0F, 0.0F, 0.1396F));
		jawUpperRight.addOrReplaceChild("upperTeethRight", CubeListBuilder.create().texOffs(56, 37).mirror(true).addBox(-0.5F, 1.0F, -1.8F, 1.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(-0.4F, -2.8F, -1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition jawLower = realHead2.addOrReplaceChild("jawLower", CubeListBuilder.create().texOffs(39, 37).addBox(-2.0F, -2.9F, -1.5F, 4.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -4.1F, -3.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lowerTeeth01 = jawLower.addOrReplaceChild("lowerTeeth01", CubeListBuilder.create().texOffs(63, 41).addBox(-1.6F, 1.3F, -0.6F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -3.7F, 0.1F, 0.0F, 0.0F, 0.0F));
		lowerTeeth01.addOrReplaceChild("lowerTeeth02", CubeListBuilder.create().texOffs(63, 41).addBox(0.6F, 1.3F, -0.7F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lEar01 = realHead2.addOrReplaceChild("lEar01", CubeListBuilder.create().texOffs(70, 0).mirror(true).addBox(0.0F, -1.0F, -1.6F, 1.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(2.8F, -0.1F, 2.8F, 0.6981F, 0.1222F, -0.6981F));
		lEar01.addOrReplaceChild("lEar02", CubeListBuilder.create().texOffs(78, 0).mirror(true).addBox(-1.4F, 2.0F, -1.4F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-0.3F, -3.0F, -0.5F, 0.2269F, 0.0F, -0.2618F));
		PartDefinition rEar01 = realHead2.addOrReplaceChild("rEar01", CubeListBuilder.create().texOffs(70, 0).addBox(-1.0F, -1.0F, -1.6F, 1.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(-2.8F, -0.1F, 2.8F, 0.6981F, -0.1222F, 0.6981F));
		rEar01.addOrReplaceChild("rEar02", CubeListBuilder.create().texOffs(78, 0).addBox(0.4F, 2.0F, -1.4F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.3F, -3.0F, -0.5F, 0.2269F, 0.0F, 0.2618F));
		realHead2.addOrReplaceChild("lCheekFur", CubeListBuilder.create().texOffs(26, 4).mirror(true).addBox(0.0F, -0.8F, -3.3F, 0.0F, 6.0F, 5.0F), PartPose.offsetAndRotation(3.5F, -0.5F, -0.6F, 0.1222F, -0.0873F, -0.5236F));
		realHead2.addOrReplaceChild("rCheekFur", CubeListBuilder.create().texOffs(26, 4).addBox(0.0F, -0.8F, -3.3F, 0.0F, 6.0F, 5.0F), PartPose.offsetAndRotation(-3.5F, -0.5F, -0.6F, 0.1222F, 0.0873F, 0.5236F));
		realHead2.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(29, 35).addBox(-1.5F, -3.2F, -2.0F, 3.0F, 5.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -4.2F, -0.1F, 0.182F, 0.0F, 0.0F));
		neck.addOrReplaceChild("fur01", CubeListBuilder.create().texOffs(90, 0).addBox(-3.5F, 2.0F, 0.0F, 7.0F, 7.0F, 2.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.9F, 1.7453F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 128, 64);
	}

	@Override
	public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		realArm = false;
		super.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		realArm = true;
		copyRotation(neck, super.head);
		neck.setPos(0, -18.2f, -1);
		neck.xRot -= 0.2531f;
		copyRotation(realBody, super.body);
		realBody.setPos(0, -12.7f, 0);
		realBody.xRot += 0.5672f;
		copyRotation(lArm01, super.leftArm);
		lArm01.setPos(5.5f, -15, 2);
		lArm01.xRot += 0.1745f;
		lArm01.yRot -= 0.0873f;
		lArm01.zRot -= 0.2356f;
		copyRotation(rArm01, super.rightArm);
		rArm01.setPos(-5.5f, -15, 2);
		rArm01.xRot += 0.1745f;
		rArm01.yRot += 0.0873f;
		rArm01.zRot += 0.2356f;
		copyRotation(BipedLeftLeg, super.leftLeg);
		BipedLeftLeg.xRot /= 2;
		BipedLeftLeg.xRot -= 3 / 4f;
		BipedLeftLeg.yRot -= 0.2269f;
		BipedLeftLeg.zRot -= 0.0873f;
		copyRotation(BipedRightLeg, super.rightLeg);
		BipedRightLeg.xRot /= 2;
		BipedRightLeg.xRot -= 3 / 4f;
		BipedRightLeg.yRot -= -0.2269f;
		BipedRightLeg.zRot += 0.0873f;
		tail01.zRot = Mth.sin(animationProgress / 8) / 8;
		if (entity.isShiftKeyDown()) {
			neck.y += 2;
			neck.z -= 4;
			realBody.y += 2;
			realBody.z -= 4;
			realBody.xRot += 0.5f;
			lArm01.y += 2;
			lArm01.z -= 4;
			rArm01.y += 2;
			rArm01.z -= 4;
			BipedLeftLeg.xRot -= 0.5f;
			BipedRightLeg.xRot -= 0.5f;
		}
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		neck.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lArm01.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rArm01.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected ModelPart getArm(HumanoidArm arm) {
		return realArm ? (arm == HumanoidArm.LEFT ? lArm01 : rArm01) : super.getArm(arm);
	}

	private void copyRotation(ModelPart to, ModelPart from) {
		to.xRot = from.xRot;
		to.yRot = from.yRot;
		to.zRot = from.zRot;
	}
}
