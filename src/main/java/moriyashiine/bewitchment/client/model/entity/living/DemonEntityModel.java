/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
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

public class DemonEntityModel<T extends DemonEntity> extends HumanoidModel<T> {
	private final ModelPart realBody;
	private final ModelPart tail01;
	private final ModelPart lWing01;
	private final ModelPart rWing01;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;
	private final ModelPart realHead;

	private boolean realArm = false;

	public DemonEntityModel(ModelPart root, boolean male) {
		super(root);
		realBody = root.getChild("realBody");
		lWing01 = realBody.getChild("lWing01");
		rWing01 = realBody.getChild("rWing01");
		BipedLeftArm = root.getChild("BipedLeftArm");
		BipedRightArm = root.getChild("BipedRightArm");
		BipedLeftLeg = root.getChild("BipedLeftLeg");
		BipedRightLeg = root.getChild("BipedRightLeg");
		realHead = root.getChild("realHead");
		if (male) {
			tail01 = realBody.getChild("tail01");
		} else {
			tail01 = realBody.getChild("stomach").getChild("tail01");
		}
	}

	public static LayerDefinition getTexturedModelDataMale() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition realBody = root.addOrReplaceChild("realBody", CubeListBuilder.create().texOffs(19, 17).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 13.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -4.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail01 = realBody.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(13, 37).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 10.1F, 1.3F, -0.8727F, 0.0F, 0.0F));
		PartDefinition tail02 = tail01.addOrReplaceChild("tail02", CubeListBuilder.create().texOffs(13, 37).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 3.8F, -0.1396F, 0.0F, 0.0F));
		PartDefinition tail03 = tail02.addOrReplaceChild("tail03", CubeListBuilder.create().texOffs(15, 45).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.9F, 0.0698F, 0.0F, 0.0F));
		PartDefinition tail04 = tail03.addOrReplaceChild("tail04", CubeListBuilder.create().texOffs(15, 45).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.9F, 0.1396F, 0.0F, 0.0F));
		PartDefinition tail05 = tail04.addOrReplaceChild("tail05", CubeListBuilder.create().texOffs(15, 45).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.9F, 0.2269F, 0.0F, 0.0F));
		PartDefinition tailTip01 = tail05.addOrReplaceChild("tailTip01", CubeListBuilder.create().texOffs(16, 53).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.5F, 0.2618F, 0.0F, 0.0F));
		tailTip01.addOrReplaceChild("tailTip02", CubeListBuilder.create().texOffs(15, 58).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.1F, 0.8F, 0.0F, -0.7854F, 0.0F));
		PartDefinition lWing01 = realBody.addOrReplaceChild("lWing01", CubeListBuilder.create().texOffs(26, 35).mirror(true).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(2.5F, 2.1F, 1.4F, 0.2731F, 0.5236F, 0.0F));
		PartDefinition lWing02 = lWing01.addOrReplaceChild("lWing02", CubeListBuilder.create().texOffs(27, 44).mirror(true).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F), PartPose.offsetAndRotation(0.1F, 0.2F, 4.3F, 0.5236F, 0.0F, 0.0F));
		PartDefinition lWing03 = lWing02.addOrReplaceChild("lWing03", CubeListBuilder.create().texOffs(29, 54).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(0.1F, -0.5F, 7.1F, 0.2094F, 0.0F, 0.0F));
		lWing03.addOrReplaceChild("lWing04", CubeListBuilder.create().texOffs(24, 55).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 7.7F, 0.0F, -0.4189F, 0.0F, 0.0F));
		lWing02.addOrReplaceChild("lWingMembrane", CubeListBuilder.create().texOffs(41, 26).mirror(true).addBox(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0911F, 0.0F, 0.0F));
		PartDefinition rWing01 = realBody.addOrReplaceChild("rWing01", CubeListBuilder.create().texOffs(26, 35).mirror(true).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(-2.5F, 2.1F, 1.4F, 0.2731F, -0.5236F, 0.0F));
		PartDefinition rWing02 = rWing01.addOrReplaceChild("rWing02", CubeListBuilder.create().texOffs(27, 44).mirror(true).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F), PartPose.offsetAndRotation(-0.1F, 0.2F, 4.3F, 0.5236F, 0.0F, 0.0F));
		PartDefinition rWing03 = rWing02.addOrReplaceChild("rWing03", CubeListBuilder.create().texOffs(29, 54).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(-0.1F, -0.5F, 7.1F, 0.2094F, 0.0F, 0.0F));
		rWing03.addOrReplaceChild("rWing04", CubeListBuilder.create().texOffs(24, 55).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 7.7F, 0.0F, -0.4189F, 0.0F, 0.0F));
		rWing02.addOrReplaceChild("rWingMembrane", CubeListBuilder.create().texOffs(41, 26).mirror(true).addBox(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0911F, 0.0F, 0.0F));
		PartDefinition realHead = root.addOrReplaceChild("realHead", CubeListBuilder.create().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -4.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rHorn01 = realHead.addOrReplaceChild("rHorn01", CubeListBuilder.create().texOffs(32, 0).mirror(true).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-2.9F, -7.4F, -1.3F, 0.1047F, 0.0F, -0.4189F));
		PartDefinition rHorn02 = rHorn01.addOrReplaceChild("rHorn02", CubeListBuilder.create().texOffs(32, 0).mirror(true).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, -0.1047F, 0.0F, 0.192F));
		PartDefinition rHorn03 = rHorn02.addOrReplaceChild("rHorn03", CubeListBuilder.create().texOffs(35, 5).mirror(true).addBox(-0.4F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, -1.6F, 0.0F, -0.1396F, 0.0F, 0.0698F));
		rHorn03.addOrReplaceChild("rHorn04", CubeListBuilder.create().texOffs(35, 10).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, -0.1396F, 0.0F, 0.1047F));
		PartDefinition lHorn01 = realHead.addOrReplaceChild("lHorn01", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(2.9F, -7.4F, -1.3F, 0.1047F, 0.0F, 0.4189F));
		PartDefinition lHorn02 = lHorn01.addOrReplaceChild("lHorn02", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, -0.1047F, 0.0F, -0.192F));
		PartDefinition lHorn03 = lHorn02.addOrReplaceChild("lHorn03", CubeListBuilder.create().texOffs(35, 5).addBox(-0.6F, -2.0F, -0.4F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, -1.6F, 0.0F, -0.1396F, 0.0F, -0.0698F));
		lHorn03.addOrReplaceChild("lHorn04", CubeListBuilder.create().texOffs(35, 10).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, -0.1396F, 0.0F, -0.1047F));
		PartDefinition BipedRightLeg = root.addOrReplaceChild("BipedRightLeg", CubeListBuilder.create().texOffs(0, 17).mirror(true).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F), PartPose.offsetAndRotation(-2.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.0F));
		PartDefinition rLeg02 = BipedRightLeg.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(0, 30).mirror(true).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 5.7F, -0.4F, 0.6981F, 0.0F, -0.1047F));
		PartDefinition rLeg03 = rLeg02.addOrReplaceChild("rLeg03", CubeListBuilder.create().texOffs(0, 41).mirror(true).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 5.2F, 0.2F, -0.4189F, 0.0F, 0.0F));
		PartDefinition rHoofClaw01a = rLeg03.addOrReplaceChild("rHoofClaw01a", CubeListBuilder.create().texOffs(0, 57).mirror(true).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-0.5F, 7.4F, -1.3F, 0.1745F, 0.1396F, 0.0349F));
		rHoofClaw01a.addOrReplaceChild("rHoofClaw01b", CubeListBuilder.create().texOffs(7, 56).mirror(true).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition rHoofClaw02a = rLeg03.addOrReplaceChild("rHoofClaw02a", CubeListBuilder.create().texOffs(0, 57).mirror(true).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.5F, 7.4F, -1.3F, 0.1745F, -0.0873F, -0.0349F));
		rHoofClaw02a.addOrReplaceChild("rHoofClaw02b", CubeListBuilder.create().texOffs(7, 56).mirror(true).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		root.addOrReplaceChild("BipedLeftArm", CubeListBuilder.create().texOffs(44, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F), PartPose.offsetAndRotation(5.0F, -2.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("BipedRightArm", CubeListBuilder.create().texOffs(44, 16).mirror(true).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 14.0F, 4.0F), PartPose.offsetAndRotation(-5.0F, -2.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition BipedLeftLeg = root.addOrReplaceChild("BipedLeftLeg", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F), PartPose.offsetAndRotation(2.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.0F));
		PartDefinition lLeg02 = BipedLeftLeg.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(0, 30).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 5.7F, -0.4F, 0.6981F, 0.0F, 0.1047F));
		PartDefinition lLeg03 = lLeg02.addOrReplaceChild("lLeg03", CubeListBuilder.create().texOffs(0, 41).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 5.2F, 0.2F, -0.4189F, 0.0F, 0.0F));
		PartDefinition lHoofClaw01a = lLeg03.addOrReplaceChild("lHoofClaw01a", CubeListBuilder.create().texOffs(0, 57).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.5F, 7.4F, -1.3F, 0.1745F, -0.1396F, -0.0349F));
		lHoofClaw01a.addOrReplaceChild("lHoofClaw01b", CubeListBuilder.create().texOffs(7, 56).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition lHoofClaw02a = lLeg03.addOrReplaceChild("lHoofClaw02a", CubeListBuilder.create().texOffs(0, 57).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-0.5F, 7.4F, -1.3F, 0.1745F, 0.0873F, 0.0349F));
		lHoofClaw02a.addOrReplaceChild("lHoofClaw02b", CubeListBuilder.create().texOffs(7, 56).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 64, 64);
	}

	public static LayerDefinition getTexturedModelDataFemale() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition realBody = root.addOrReplaceChild("realBody", CubeListBuilder.create().texOffs(19, 17).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -6.2F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lWing01 = realBody.addOrReplaceChild("lWing01", CubeListBuilder.create().texOffs(50, 52).mirror(true).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(2.5F, 3.2F, 1.4F, 0.2731F, 0.5236F, 0.0F));
		PartDefinition lWing02 = lWing01.addOrReplaceChild("lWing02", CubeListBuilder.create().texOffs(38, 52).mirror(true).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F), PartPose.offsetAndRotation(0.1F, 0.2F, 4.3F, 0.5236F, 0.0F, 0.0F));
		PartDefinition lWing03 = lWing02.addOrReplaceChild("lWing03", CubeListBuilder.create().texOffs(29, 54).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(0.1F, -0.5F, 7.1F, 0.2094F, 0.0F, 0.0F));
		lWing03.addOrReplaceChild("lWing04", CubeListBuilder.create().texOffs(25, 55).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 7.7F, 0.0F, -0.4189F, 0.0F, 0.0F));
		lWing02.addOrReplaceChild("lWingMembrane", CubeListBuilder.create().texOffs(41, 26).mirror(true).addBox(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0911F, 0.0F, 0.0F));
		PartDefinition rWing01 = realBody.addOrReplaceChild("rWing01", CubeListBuilder.create().texOffs(50, 52).mirror(true).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(-2.5F, 3.2F, 1.4F, 0.2731F, -0.5236F, 0.0F));
		PartDefinition rWing02 = rWing01.addOrReplaceChild("rWing02", CubeListBuilder.create().texOffs(38, 52).mirror(true).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F), PartPose.offsetAndRotation(-0.1F, 0.2F, 4.3F, 0.5236F, 0.0F, 0.0F));
		PartDefinition rWing03 = rWing02.addOrReplaceChild("rWing03", CubeListBuilder.create().texOffs(29, 54).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(-0.1F, -0.5F, 7.1F, 0.2094F, 0.0F, 0.0F));
		rWing03.addOrReplaceChild("rWing04", CubeListBuilder.create().texOffs(25, 55).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 7.7F, 0.0F, -0.4189F, 0.0F, 0.0F));
		rWing02.addOrReplaceChild("rWingMembrane", CubeListBuilder.create().texOffs(41, 26).mirror(true).addBox(0.0F, 0.4F, -2.2F, 0.0F, 14.0F, 11.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0911F, 0.0F, 0.0F));
		realBody.addOrReplaceChild("boobs", CubeListBuilder.create().texOffs(0, 57).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 1.9F, -0.3F, -0.6109F, 0.0F, 0.0F));
		PartDefinition stomach = realBody.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(19, 27).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 7.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition loincloth = stomach.addOrReplaceChild("loincloth", CubeListBuilder.create().texOffs(48, 0).addBox(-3.0F, 0.0F, -0.5F, 6.0F, 8.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 4.6F, -1.9F, -0.2269F, 0.0F, 0.0F));
		loincloth.addOrReplaceChild("loincloth02", CubeListBuilder.create().texOffs(48, 8).addBox(-3.0F, 0.05F, -0.53F, 6.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 7.8F, 0.0F, 0.2269F, 0.0F, 0.0F));
		PartDefinition tail01 = stomach.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(33, 11).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 5.5F, 1.3F, -0.8727F, 0.0F, 0.0F));
		PartDefinition tail02 = tail01.addOrReplaceChild("tail02", CubeListBuilder.create().texOffs(33, 11).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 3.8F, -0.1396F, 0.0F, 0.0F));
		PartDefinition tail03 = tail02.addOrReplaceChild("tail03", CubeListBuilder.create().texOffs(13, 58).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.9F, 0.0698F, 0.0F, 0.0F));
		PartDefinition tail04 = tail03.addOrReplaceChild("tail04", CubeListBuilder.create().texOffs(13, 58).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.9F, 0.1396F, 0.0F, 0.0F));
		PartDefinition tail05 = tail04.addOrReplaceChild("tail05", CubeListBuilder.create().texOffs(13, 58).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.9F, 0.2269F, 0.0F, 0.0F));
		PartDefinition tailTip01 = tail05.addOrReplaceChild("tailTip01", CubeListBuilder.create().texOffs(16, 54).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.5F, 0.2618F, 0.0F, 0.0F));
		tailTip01.addOrReplaceChild("tailTip02", CubeListBuilder.create().texOffs(36, 52).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.1F, 0.8F, 0.0F, -0.7854F, 0.0F));
		PartDefinition BipedRightLeg = root.addOrReplaceChild("BipedRightLeg", CubeListBuilder.create().texOffs(0, 17).mirror(true).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F), PartPose.offsetAndRotation(-2.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.0F));
		PartDefinition rLeg02 = BipedRightLeg.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(0, 30).mirror(true).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 5.7F, -0.4F, 0.6981F, 0.0F, -0.1047F));
		PartDefinition rLeg03 = rLeg02.addOrReplaceChild("rLeg03", CubeListBuilder.create().texOffs(0, 41).mirror(true).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 5.2F, 0.2F, -0.4189F, 0.0F, 0.0F));
		PartDefinition rHoofClaw01a = rLeg03.addOrReplaceChild("rHoofClaw01a", CubeListBuilder.create().texOffs(0, 54).mirror(true).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-0.5F, 7.4F, -1.3F, 0.1745F, 0.1396F, 0.0349F));
		rHoofClaw01a.addOrReplaceChild("rHoofClaw01b", CubeListBuilder.create().texOffs(7, 53).mirror(true).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition rHoofClaw02a = rLeg03.addOrReplaceChild("rHoofClaw02a", CubeListBuilder.create().texOffs(0, 54).mirror(true).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.5F, 7.4F, -1.3F, 0.1745F, -0.0873F, -0.0349F));
		rHoofClaw02a.addOrReplaceChild("rHoofClaw02b", CubeListBuilder.create().texOffs(7, 53).mirror(true).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		root.addOrReplaceChild("BipedRightArm", CubeListBuilder.create().texOffs(44, 16).mirror(true).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F), PartPose.offsetAndRotation(-5.0F, -4.2F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition BipedLeftLeg = root.addOrReplaceChild("BipedLeftLeg", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 8.0F, 5.0F), PartPose.offsetAndRotation(2.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.0F));
		PartDefinition lLeg02 = BipedLeftLeg.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(0, 30).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 5.7F, -0.4F, 0.6981F, 0.0F, 0.1047F));
		PartDefinition lLeg03 = lLeg02.addOrReplaceChild("lLeg03", CubeListBuilder.create().texOffs(0, 41).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 5.2F, 0.2F, -0.4189F, 0.0F, 0.0F));
		PartDefinition lHoofClaw01a = lLeg03.addOrReplaceChild("lHoofClaw01a", CubeListBuilder.create().texOffs(0, 54).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.5F, 7.4F, -1.3F, 0.1745F, -0.1396F, -0.0349F));
		lHoofClaw01a.addOrReplaceChild("lHoofClaw01b", CubeListBuilder.create().texOffs(7, 53).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition lHoofClaw02a = lLeg03.addOrReplaceChild("lHoofClaw02a", CubeListBuilder.create().texOffs(0, 54).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-0.5F, 7.4F, -1.3F, 0.1745F, 0.0873F, 0.0349F));
		lHoofClaw02a.addOrReplaceChild("lHoofClaw02b", CubeListBuilder.create().texOffs(7, 53).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition realHead = root.addOrReplaceChild("realHead", CubeListBuilder.create().texOffs(0, 1).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -6.2F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rHorn01 = realHead.addOrReplaceChild("rHorn01", CubeListBuilder.create().mirror(true).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-2.9F, -7.4F, -0.5F, -0.1745F, -0.1396F, -0.1396F));
		PartDefinition rHorn02a = rHorn01.addOrReplaceChild("rHorn02a", CubeListBuilder.create().texOffs(0, 4).mirror(true).addBox(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.25F, 0.25F, 0.25F)), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, -0.1745F, 0.0F, -0.0524F));
		PartDefinition rHorn03a = rHorn02a.addOrReplaceChild("rHorn03a", CubeListBuilder.create().texOffs(0, 4).mirror(true).addBox(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, -1.6F, 0.0F, -0.1047F, 0.0F, -0.1047F));
		PartDefinition rHorn04 = rHorn03a.addOrReplaceChild("rHorn04", CubeListBuilder.create().texOffs(4, 4).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, 0.0524F, 0.0F, -0.1396F));
		rHorn04.addOrReplaceChild("rHorn05", CubeListBuilder.create().texOffs(43, 0).mirror(true).addBox(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.0524F, 0.0F, 0.1396F));
		PartDefinition lHorn01 = realHead.addOrReplaceChild("lHorn01", CubeListBuilder.create().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(2.9F, -7.4F, -0.5F, -0.1745F, 0.1396F, 0.1396F));
		PartDefinition lHorn02 = lHorn01.addOrReplaceChild("lHorn02", CubeListBuilder.create().texOffs(0, 4).addBox(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.25F, 0.25F, 0.25F)), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, -0.1745F, 0.0F, 0.0524F));
		PartDefinition lHorn03 = lHorn02.addOrReplaceChild("lHorn03", CubeListBuilder.create().texOffs(0, 4).addBox(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, -1.6F, 0.0F, -0.1047F, 0.0F, 0.1047F));
		PartDefinition lHorn04 = lHorn03.addOrReplaceChild("lHorn04", CubeListBuilder.create().texOffs(4, 4).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, 0.0524F, 0.0F, 0.1396F));
		lHorn04.addOrReplaceChild("lHorn05", CubeListBuilder.create().texOffs(43, 0).addBox(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.0524F, 0.0F, -0.1396F));
		PartDefinition HAIR = realHead.addOrReplaceChild("HAIR", CubeListBuilder.create().texOffs(25, 0).addBox(-4.0F, 7.0F, 1.5F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -8.8F, 2.0F, 0.0F, 0.0F, 0.0F));
		HAIR.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(14, 39).addBox(-5.0F, 0.0F, -1.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, 0.4363F, 0.7854F, 0.3054F));
		root.addOrReplaceChild("BipedLeftArm", CubeListBuilder.create().texOffs(44, 16).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F), PartPose.offsetAndRotation(5.0F, -4.2F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 64, 64);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
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
		BipedLeftLeg.xRot -= 0.2618f;
		BipedLeftLeg.zRot -= 0.1047f;
		copyRotation(BipedRightLeg, super.rightLeg);
		BipedRightLeg.xRot /= 2;
		BipedRightLeg.xRot -= 0.2618f;
		BipedRightLeg.zRot += 0.1047f;
		lWing01.yRot = Mth.cos(animationProgress / 16) / 3 + 1 / 3f;
		rWing01.yRot = -lWing01.yRot;
		tail01.zRot = Mth.sin(animationProgress / 8) / 8;
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
