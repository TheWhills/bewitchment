/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.LilithEntity;
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

public class LilithEntityModel<T extends LilithEntity> extends HumanoidModel<T> {
	private final ModelPart realBody;
	private final ModelPart frontLoincloth;
	private final ModelPart backLoincloth;
	private final ModelPart lWing01;
	private final ModelPart rWing01;
	private final ModelPart bipedLeftArm;
	private final ModelPart bipedRightArm;
	private final ModelPart bipedLeftLeg;
	private final ModelPart bipedRightLeg;
	private final ModelPart realHead;

	private boolean realArm = false;

	public LilithEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		frontLoincloth = realBody.getChild("stomach").getChild("hips").getChild("frontLoincloth");
		backLoincloth = realBody.getChild("stomach").getChild("hips").getChild("backLoincloth");
		rWing01 = realBody.getChild("rWing01");
		lWing01 = realBody.getChild("lWing01");
		bipedLeftArm = root.getChild("bipedLeftArm");
		bipedRightArm = root.getChild("bipedRightArm");
		bipedLeftLeg = root.getChild("bipedLeftLeg");
		bipedRightLeg = root.getChild("bipedRightLeg");
		realHead = root.getChild("realHead");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition realBody = root.addOrReplaceChild("realBody", CubeListBuilder.create().texOffs(20, 16).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -12.0F, 1.2F, 0.0F, 0.0F, 0.0F));
		PartDefinition stomach = realBody.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(19, 28).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 6.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition hips = stomach.addOrReplaceChild("hips", CubeListBuilder.create().texOffs(19, 39).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 5.9F, 0.0F, 0.0F, 0.0F, 0.0F));
		hips.addOrReplaceChild("frontLoincloth", CubeListBuilder.create().texOffs(73, 45).addBox(-5.6F, 0.0F, 0.0F, 10.0F, 14.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -2.7F, -0.0698F, 0.0F, 0.0F));
		hips.addOrReplaceChild("backLoincloth", CubeListBuilder.create().texOffs(103, 45).addBox(-5.5F, 0.0F, -1.0F, 9.0F, 12.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.1F, 1.8F, 0.0F, 0.0F, 0.0F));
		PartDefinition boobs = realBody.addOrReplaceChild("boobs", CubeListBuilder.create().texOffs(19, 48).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 1.9F, -0.8F, -0.6981F, 0.0F, 0.0F));
		boobs.addOrReplaceChild("boobWrap", CubeListBuilder.create().texOffs(20, 59).addBox(-3.5F, 0.27F, 0.15F, 7.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 3.7F, -1.9F, -0.8727F, 0.0F, 0.0F));
		PartDefinition lWing01 = realBody.addOrReplaceChild("lWing01", CubeListBuilder.create().texOffs(55, 40).mirror(true).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(1.8F, 2.8F, 1.4F, 0.3491F, 0.6458F, 0.0F));
		PartDefinition lWing02 = lWing01.addOrReplaceChild("lWing02", CubeListBuilder.create().texOffs(41, 41).mirror(true).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F), PartPose.offsetAndRotation(0.1F, 0.2F, 5.6F, 0.5236F, 0.1396F, 0.0F));
		PartDefinition lWing03 = lWing02.addOrReplaceChild("lWing03", CubeListBuilder.create().texOffs(42, 53).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.offsetAndRotation(0.1F, 0.4F, 8.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition lWing04 = lWing03.addOrReplaceChild("lWing04", CubeListBuilder.create().texOffs(51, 52).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 8.6F, 0.2F, -0.4712F, 0.0F, 0.0F));
		lWing04.addOrReplaceChild("lFeathers03", CubeListBuilder.create().texOffs(60, 0).mirror(true).addBox(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lWing03.addOrReplaceChild("lFeathers02", CubeListBuilder.create().texOffs(90, 0).mirror(true).addBox(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F), PartPose.offsetAndRotation(-0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
		lWing02.addOrReplaceChild("lFeathers01", CubeListBuilder.create().texOffs(65, 13).mirror(true).addBox(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F), PartPose.offsetAndRotation(0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition rWing01 = realBody.addOrReplaceChild("rWing01", CubeListBuilder.create().texOffs(55, 40).mirror(true).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(-1.8F, 2.8F, 1.4F, 0.3491F, -0.6458F, 0.0F));
		PartDefinition rWing02 = rWing01.addOrReplaceChild("rWing02", CubeListBuilder.create().texOffs(41, 41).mirror(true).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F), PartPose.offsetAndRotation(-0.1F, 0.2F, 5.6F, 0.5236F, -0.1396F, 0.0F));
		PartDefinition rWing03 = rWing02.addOrReplaceChild("rWing03", CubeListBuilder.create().texOffs(42, 53).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.offsetAndRotation(-0.1F, 0.4F, 8.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition rWing04 = rWing03.addOrReplaceChild("rWing04", CubeListBuilder.create().texOffs(51, 52).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 8.6F, 0.2F, -0.4712F, 0.0F, 0.0F));
		rWing04.addOrReplaceChild("rFeathers03", CubeListBuilder.create().texOffs(60, 0).mirror(true).addBox(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rWing03.addOrReplaceChild("rFeathers02", CubeListBuilder.create().texOffs(90, 0).mirror(true).addBox(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F), PartPose.offsetAndRotation(0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
		rWing02.addOrReplaceChild("rFeathers01", CubeListBuilder.create().texOffs(65, 13).mirror(true).addBox(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F), PartPose.offsetAndRotation(-0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition bipedLeftLeg = root.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F), PartPose.offsetAndRotation(2.3F, 1.9F, 1.2F, 0.0F, 0.0F, 0.0F));
		PartDefinition lLeg02 = bipedLeftLeg.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, -1.2F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 11.5F, -0.2F, 0.0873F, 0.0F, 0.0349F));
		lLeg02.addOrReplaceChild("lTalon01", CubeListBuilder.create().texOffs(0, 52).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(1.1F, 9.7F, -1.4F, 0.0349F, -0.1745F, 0.0F));
		lLeg02.addOrReplaceChild("lTalon02", CubeListBuilder.create().texOffs(0, 52).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 9.7F, -1.7F, 0.0349F, 0.0F, 0.0F));
		lLeg02.addOrReplaceChild("lTalon03", CubeListBuilder.create().texOffs(0, 52).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-1.1F, 9.7F, -1.4F, 0.0349F, 0.1745F, 0.0F));
		lLeg02.addOrReplaceChild("lTalon04", CubeListBuilder.create().texOffs(0, 58).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 9.7F, 1.6F, -0.0349F, 0.0F, 0.0F));
		PartDefinition bipedRightArm = root.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(46, 16).mirror(true).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), PartPose.offsetAndRotation(-4.5F, -10.1F, 1.2F, 0.0F, 0.0F, 0.0F));
		bipedRightArm.addOrReplaceChild("rClaw01", CubeListBuilder.create().texOffs(11, 53).mirror(true).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-0.6F, 11.2F, -1.6F, 0.0F, 0.0F, -0.1745F));
		bipedRightArm.addOrReplaceChild("rClaw02", CubeListBuilder.create().texOffs(11, 53).mirror(true).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-0.6F, 11.8F, -0.5F, 0.0F, 0.0F, -0.1222F));
		bipedRightArm.addOrReplaceChild("rClaw03", CubeListBuilder.create().texOffs(11, 53).mirror(true).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-0.6F, 11.8F, 0.6F, 0.0F, 0.0F, -0.1222F));
		bipedRightArm.addOrReplaceChild("rClaw04", CubeListBuilder.create().texOffs(11, 53).mirror(true).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-0.6F, 11.2F, 1.6F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bipedLeftArm = root.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(46, 16).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), PartPose.offsetAndRotation(4.5F, -10.1F, 1.2F, 0.0F, 0.0F, 0.0F));
		bipedLeftArm.addOrReplaceChild("lClaw01", CubeListBuilder.create().texOffs(11, 53).addBox(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.6F, 11.2F, -1.6F, 0.0F, 0.0F, 0.1745F));
		bipedLeftArm.addOrReplaceChild("lClaw02", CubeListBuilder.create().texOffs(11, 53).addBox(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.6F, 11.8F, -0.5F, 0.0F, 0.0F, 0.1222F));
		bipedLeftArm.addOrReplaceChild("lClaw03", CubeListBuilder.create().texOffs(11, 53).addBox(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.6F, 11.8F, 0.6F, 0.0F, 0.0F, 0.1222F));
		bipedLeftArm.addOrReplaceChild("lClaw04", CubeListBuilder.create().texOffs(11, 53).addBox(0.0F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.6F, 11.2F, 1.6F, 0.0F, 0.0F, 0.1745F));
		PartDefinition realHead = root.addOrReplaceChild("realHead", CubeListBuilder.create().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -12.0F, 1.2F, 0.0F, 0.0F, 0.0F));
		PartDefinition lHorn01 = realHead.addOrReplaceChild("lHorn01", CubeListBuilder.create().texOffs(109, 0).mirror(true).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(2.3F, -6.9F, -1.3F, -0.2618F, 0.0F, 0.5236F));
		PartDefinition lHorn02 = lHorn01.addOrReplaceChild("lHorn02", CubeListBuilder.create().texOffs(109, 7).mirror(true).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -3.5F, 0.0F, -0.4538F, 0.0F, -0.1745F));
		PartDefinition lHorn03 = lHorn02.addOrReplaceChild("lHorn03", CubeListBuilder.create().texOffs(118, 7).mirror(true).addBox(-0.99F, -3.0F, -0.99F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, -0.4363F, 0.0F, 0.0F));
		PartDefinition lHorn04a = lHorn03.addOrReplaceChild("lHorn04a", CubeListBuilder.create().mirror(true).addBox(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.2F, -2.7F, -0.3F, -0.6981F, 0.0F, -0.4014F));
		lHorn04a.addOrReplaceChild("lHorn04b", CubeListBuilder.create().mirror(true).addBox(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lHorn04a.addOrReplaceChild("lHorn04c", CubeListBuilder.create().mirror(true).addBox(-0.7F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lHorn04d = lHorn04a.addOrReplaceChild("lHorn04d", CubeListBuilder.create().mirror(true).addBox(-0.3F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lHorn05 = lHorn04d.addOrReplaceChild("lHorn05", CubeListBuilder.create().texOffs(0, 4).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.8F, 0.0F, -0.4363F, 0.0F, 0.2269F));
		lHorn05.addOrReplaceChild("lHorn06", CubeListBuilder.create().mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.4887F, 0.0F, 0.2269F));
		PartDefinition rHorn01 = realHead.addOrReplaceChild("rHorn01", CubeListBuilder.create().texOffs(109, 0).mirror(true).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(-2.3F, -6.9F, -1.3F, -0.2618F, 0.0F, -0.5236F));
		PartDefinition rHorn02 = rHorn01.addOrReplaceChild("rHorn02", CubeListBuilder.create().texOffs(109, 7).mirror(true).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -3.5F, 0.0F, -0.4538F, 0.0F, 0.1745F));
		PartDefinition rHorn03 = rHorn02.addOrReplaceChild("rHorn03", CubeListBuilder.create().texOffs(118, 7).mirror(true).addBox(-1.01F, -3.0F, -0.99F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, -0.4363F, 0.0F, 0.0F));
		PartDefinition rHorn04a = rHorn03.addOrReplaceChild("rHorn04a", CubeListBuilder.create().mirror(true).addBox(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-0.2F, -2.7F, 0.3F, -0.6981F, 0.0F, 0.4014F));
		rHorn04a.addOrReplaceChild("rHorn04b", CubeListBuilder.create().mirror(true).addBox(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rHorn04a.addOrReplaceChild("rHorn04c", CubeListBuilder.create().mirror(true).addBox(-0.7F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rHorn04d = rHorn04a.addOrReplaceChild("rHorn04d", CubeListBuilder.create().mirror(true).addBox(-0.3F, -3.0F, -0.3F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rHorn05 = rHorn04d.addOrReplaceChild("rHorn05", CubeListBuilder.create().texOffs(0, 4).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.8F, 0.0F, -0.4363F, 0.0F, -0.2269F));
		rHorn05.addOrReplaceChild("rHorn06", CubeListBuilder.create().mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.4887F, 0.0F, -0.2269F));
		realHead.addOrReplaceChild("lHair", CubeListBuilder.create().texOffs(45, 0).addBox(-2.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F), PartPose.offsetAndRotation(-2.4F, -6.3F, 1.1F, 0.3491F, 0.0F, 0.3142F));
		realHead.addOrReplaceChild("rHair", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F), PartPose.offsetAndRotation(2.4F, -6.3F, 1.1F, 0.3491F, 0.0F, -0.3142F));
		PartDefinition crown00 = realHead.addOrReplaceChild("crown00", CubeListBuilder.create().texOffs(27, 0).addBox(-3.0F, -2.5F, 0.0F, 6.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -8.0F, -3.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition crownL01 = crown00.addOrReplaceChild("crownL01", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-0.2F, -1.5F, -0.49F, 2.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(2.3F, -1.0F, 0.5F, 0.0F, 0.0F, -0.6981F));
		PartDefinition crownL02 = crownL01.addOrReplaceChild("crownL02", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-0.7F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(1.5F, -0.1F, 0.1F, 0.0F, 0.0F, -0.4189F));
		PartDefinition crownL03 = crownL02.addOrReplaceChild("crownL03", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-0.7F, -1.0F, -0.49F, 3.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(2.2F, -0.2F, 0.0F, 0.0F, 0.0F, -0.6109F));
		PartDefinition crownL04 = crownL03.addOrReplaceChild("crownL04", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-0.1F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(2.0F, -0.3F, 0.0F, 0.0F, 0.0F, -0.4363F));
		crownL04.addOrReplaceChild("crownL05", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-0.7F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.1F, 0.6F, 0.0F, 0.0F, 0.0F, -0.2094F));
		PartDefinition crownR01 = crown00.addOrReplaceChild("crownR01", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-1.8F, -1.5F, -0.49F, 2.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-2.3F, -1.0F, 0.5F, 0.0F, 0.0F, 0.6981F));
		PartDefinition crownR02 = crownR01.addOrReplaceChild("crownR02", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-2.3F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-1.5F, -0.1F, 0.1F, 0.0F, 0.0F, 0.4189F));
		PartDefinition crownR03 = crownR02.addOrReplaceChild("crownR03", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-2.3F, -1.0F, -0.49F, 3.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-2.2F, -0.2F, 0.0F, 0.0F, 0.0F, 0.6109F));
		PartDefinition crownR04 = crownR03.addOrReplaceChild("crownR04", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-2.9F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-2.0F, -0.3F, 0.0F, 0.0F, 0.0F, 0.4363F));
		crownR04.addOrReplaceChild("crownR05", CubeListBuilder.create().texOffs(27, 0).mirror(true).addBox(-2.3F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-0.1F, 0.6F, 0.0F, 0.0F, 0.0F, 0.2094F));
		PartDefinition HAIR = realHead.addOrReplaceChild("HAIR", CubeListBuilder.create().texOffs(56, 0).addBox(-4.0F, 6.0F, 1.5F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -8.0F, 1.8F, 0.0F, 0.0F, 0.0F));
		HAIR.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(85, 0).addBox(-5.0F, -2.0F, -1.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, 3.0F, -3.0F, 0.4363F, 0.7854F, 0.3054F));
		PartDefinition bipedRightLeg = root.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create().texOffs(0, 16).mirror(true).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 12.0F, 5.0F), PartPose.offsetAndRotation(-2.3F, 1.9F, 1.2F, 0.0F, 0.0F, 0.0F));
		PartDefinition rLeg02 = bipedRightLeg.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(0, 34).mirror(true).addBox(-2.0F, -1.2F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 11.5F, -0.2F, 0.0873F, 0.0F, -0.0349F));
		rLeg02.addOrReplaceChild("rTalon01", CubeListBuilder.create().texOffs(0, 52).mirror(true).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-1.1F, 9.7F, -1.4F, 0.0349F, 0.1745F, 0.0F));
		rLeg02.addOrReplaceChild("rTalon02", CubeListBuilder.create().texOffs(0, 52).mirror(true).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 9.7F, -1.7F, 0.0349F, 0.0F, 0.0F));
		rLeg02.addOrReplaceChild("rTalon03", CubeListBuilder.create().texOffs(0, 52).mirror(true).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(1.1F, 9.7F, -1.4F, 0.0349F, -0.1745F, 0.0F));
		rLeg02.addOrReplaceChild("rTalon04", CubeListBuilder.create().texOffs(0, 58).mirror(true).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 9.7F, 1.6F, -0.0349F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 128, 64);
	}

	@Override
	public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		realArm = false;
		super.setupAnim(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(realHead, super.head);
		copyRotation(realBody, super.body);
		copyRotation(bipedLeftArm, super.leftArm);
		bipedLeftArm.zRot -= 0.1f;
		copyRotation(bipedRightArm, super.rightArm);
		bipedRightArm.zRot += 0.1f;
		copyRotation(bipedLeftLeg, super.leftLeg);
		bipedLeftLeg.xRot /= 2;
		bipedLeftLeg.xRot -= 0.0698f;
		bipedLeftLeg.zRot -= 0.0349f;
		copyRotation(bipedRightLeg, super.rightLeg);
		bipedRightLeg.xRot /= 2;
		bipedRightLeg.xRot -= 0.0698f;
		bipedRightLeg.zRot += 0.0349f;
		lWing01.yRot = Mth.cos(animationProgress / 16) / 3 + 1 / 3f;
		rWing01.yRot = -lWing01.yRot;
		frontLoincloth.xRot = Math.min(bipedLeftLeg.xRot, bipedRightLeg.xRot);
		backLoincloth.xRot = Math.max(bipedLeftLeg.xRot, bipedRightLeg.xRot);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		realHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		realBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedLeftArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedRightArm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedLeftLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bipedRightLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected ModelPart getArm(HumanoidArm arm) {
		return realArm ? (arm == HumanoidArm.LEFT ? bipedLeftArm : bipedRightArm) : super.getArm(arm);
	}

	private void copyRotation(ModelPart to, ModelPart from) {
		to.xRot = from.xRot;
		to.yRot = from.yRot;
		to.zRot = from.zRot;
	}
}
