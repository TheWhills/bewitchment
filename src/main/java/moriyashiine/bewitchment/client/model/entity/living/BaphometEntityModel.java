/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class BaphometEntityModel<T extends BaphometEntity> extends HumanoidModel<T> {
	private static final ItemStack CADUCEUS = new ItemStack(BWObjects.CADUCEUS.get());

	private final ModelPart realBody;
	private final ModelPart frontLoincloth;
	private final ModelPart backLoincloth;
	private final ModelPart rWing01;
	private final ModelPart lWing01;
	private final ModelPart BipedLeftArm;
	private final ModelPart BipedRightArm;
	private final ModelPart BipedLeftLeg;
	private final ModelPart BipedRightLeg;
	private final ModelPart realHead;

	private boolean realArm = false;

	public BaphometEntityModel(ModelPart root) {
		super(root);
		realBody = root.getChild("realBody");
		frontLoincloth = realBody.getChild("stomach").getChild("hips").getChild("frontLoincloth");
		backLoincloth = realBody.getChild("stomach").getChild("hips").getChild("backLoincloth");
		rWing01 = realBody.getChild("rWing01");
		lWing01 = realBody.getChild("lWing01");
		BipedLeftArm = root.getChild("BipedLeftArm");
		BipedRightArm = root.getChild("BipedRightArm");
		BipedLeftLeg = root.getChild("BipedLeftLeg");
		BipedRightLeg = root.getChild("BipedRightLeg");
		realHead = root.getChild("realHead");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition realBody = root.addOrReplaceChild("realBody", CubeListBuilder.create().texOffs(20, 19).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 6.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -12.7F, 0.0F, 0.0F, 0.0F, 0.0F));
		realBody.addOrReplaceChild("boobLeft", CubeListBuilder.create().texOffs(18, 52).mirror(true).addBox(-1.5F, 0.5F, -1.8F, 3.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(1.6F, 1.9F, -0.8F, -0.6981F, -0.1745F, 0.0F));
		realBody.addOrReplaceChild("boobRight", CubeListBuilder.create().texOffs(18, 52).addBox(-1.5F, 0.5F, -1.8F, 3.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(-1.6F, 1.9F, -0.8F, -0.6981F, 0.1745F, 0.0F));
		PartDefinition stomach = realBody.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(19, 31).addBox(-3.5F, 0.0F, -2.5F, 7.0F, 6.0F, 5.0F).texOffs(90, 48).addBox(-4.5F, 3.7F, -3.0F, 9.0F, 3.0F, 2.0F).texOffs(108, 18).addBox(-4.0F, 1.7F, -3.0F, 8.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition hips = stomach.addOrReplaceChild("hips", CubeListBuilder.create().texOffs(16, 43).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		hips.addOrReplaceChild("frontLoincloth", CubeListBuilder.create().texOffs(65, 0).addBox(-5.0F, 0.4698F, -0.329F, 10.0F, 10.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		hips.addOrReplaceChild("backLoincloth", CubeListBuilder.create().texOffs(91, 0).addBox(-4.5F, 0.0F, -1.0F, 9.0F, 10.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 1.1F, 1.8F, 0.0F, 0.0F, 0.0F));
		PartDefinition rWing01 = realBody.addOrReplaceChild("rWing01", CubeListBuilder.create().texOffs(57, 40).mirror(true).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(-1.8F, 2.8F, 1.4F, 0.3491F, -0.6458F, 0.0F));
		PartDefinition rWing02 = rWing01.addOrReplaceChild("rWing02", CubeListBuilder.create().texOffs(43, 41).mirror(true).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F), PartPose.offsetAndRotation(-0.1F, 0.2F, 5.6F, 0.5236F, -0.1396F, 0.0F));
		PartDefinition rWing03 = rWing02.addOrReplaceChild("rWing03", CubeListBuilder.create().texOffs(42, 53).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.offsetAndRotation(-0.1F, 0.4F, 8.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition rWing04 = rWing03.addOrReplaceChild("rWing04", CubeListBuilder.create().texOffs(51, 52).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 8.6F, 0.2F, -0.4712F, 0.0F, 0.0F));
		rWing04.addOrReplaceChild("rWing05", CubeListBuilder.create().texOffs(61, 5).mirror(true).addBox(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		rWing03.addOrReplaceChild("rFeathers02", CubeListBuilder.create().texOffs(90, 13).mirror(true).addBox(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F), PartPose.offsetAndRotation(0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
		rWing02.addOrReplaceChild("rFeathers01", CubeListBuilder.create().texOffs(65, 31).mirror(true).addBox(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F), PartPose.offsetAndRotation(-0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition lWing01 = realBody.addOrReplaceChild("lWing01", CubeListBuilder.create().texOffs(57, 40).mirror(true).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(1.8F, 2.8F, 1.4F, 0.3491F, 0.6458F, 0.0F));
		PartDefinition lWing02 = lWing01.addOrReplaceChild("lWing02", CubeListBuilder.create().texOffs(43, 41).mirror(true).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 9.0F), PartPose.offsetAndRotation(0.1F, 0.2F, 5.6F, 0.5236F, 0.1396F, 0.0F));
		PartDefinition lWing03 = lWing02.addOrReplaceChild("lWing03", CubeListBuilder.create().texOffs(42, 53).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.offsetAndRotation(0.1F, 0.4F, 8.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition lWing04 = lWing03.addOrReplaceChild("lWing04", CubeListBuilder.create().texOffs(51, 52).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 11.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 8.6F, 0.2F, -0.4712F, 0.0F, 0.0F));
		lWing04.addOrReplaceChild("lWing05", CubeListBuilder.create().texOffs(61, 5).mirror(true).addBox(0.0F, 0.0F, -13.0F, 0.0F, 20.0F, 13.0F), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		lWing03.addOrReplaceChild("lFeathers02", CubeListBuilder.create().texOffs(90, 13).mirror(true).addBox(-0.5F, -0.6F, -13.7F, 1.0F, 16.0F, 16.0F), PartPose.offsetAndRotation(-0.1F, 2.3F, 0.0F, -0.4363F, 0.0F, 0.0F));
		lWing02.addOrReplaceChild("lFeathers01", CubeListBuilder.create().texOffs(65, 31).mirror(true).addBox(-0.5F, -0.6F, -11.1F, 1.0F, 10.0F, 22.0F), PartPose.offsetAndRotation(0.2F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition BipedRightLeg = root.addOrReplaceChild("BipedRightLeg", CubeListBuilder.create().texOffs(0, 16).mirror(true).addBox(-2.5F, -1.6F, -2.9F, 5.0F, 11.0F, 5.0F), PartPose.offsetAndRotation(-2.4F, 2.1F, 0.3F, 0.0F, 0.0F, 0.0F));
		PartDefinition rLeg02 = BipedRightLeg.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(0, 32).mirror(true).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(-0.2F, 7.6F, -1.3F, 0.7679F, 0.1396F, 0.0436F));
		PartDefinition rLeg03 = rLeg02.addOrReplaceChild("rLeg03", CubeListBuilder.create().texOffs(0, 44).mirror(true).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 6.0F, 0.2F, -0.4887F, 0.0F, -0.1047F));
		PartDefinition rHoofClaw01a = rLeg03.addOrReplaceChild("rHoofClaw01a", CubeListBuilder.create().texOffs(9, 58).mirror(true).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-0.5F, 9.5F, -1.3F, 0.0F, 0.1396F, 0.0349F));
		rHoofClaw01a.addOrReplaceChild("rHoofClaw01b", CubeListBuilder.create().texOffs(0, 57).mirror(true).addBox(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition rHoofClaw02a = rLeg03.addOrReplaceChild("rHoofClaw02a", CubeListBuilder.create().texOffs(9, 58).mirror(true).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.5F, 9.5F, -1.3F, 0.0F, -0.1396F, -0.0349F));
		rHoofClaw02a.addOrReplaceChild("rHoofClaw02b", CubeListBuilder.create().texOffs(0, 57).mirror(true).addBox(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		root.addOrReplaceChild("BipedRightArm", CubeListBuilder.create().texOffs(46, 19).mirror(true).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), PartPose.offsetAndRotation(-5.0F, -10.8F, 0.0F, 0.0F, 0.0F, 0.1F));
		PartDefinition BipedLeftLeg = root.addOrReplaceChild("BipedLeftLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -1.6F, -2.9F, 5.0F, 11.0F, 5.0F), PartPose.offsetAndRotation(2.6F, 2.1F, 0.3F, 0.0F, 0.0F, 0.0F));
		PartDefinition lLeg02 = BipedLeftLeg.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(0.2F, 7.6F, -1.3F, 0.7679F, -0.1396F, -0.0436F));
		PartDefinition lLeg03 = lLeg02.addOrReplaceChild("lLeg03", CubeListBuilder.create().texOffs(0, 44).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 10.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 6.0F, 0.2F, -0.4887F, 0.0F, 0.1047F));
		PartDefinition lHoofClaw01a = lLeg03.addOrReplaceChild("lHoofClaw01a", CubeListBuilder.create().texOffs(9, 58).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.5F, 9.5F, -1.3F, 0.0F, -0.1396F, -0.0349F));
		lHoofClaw01a.addOrReplaceChild("lHoofClaw01b", CubeListBuilder.create().texOffs(0, 57).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition lHoofClaw02a = lLeg03.addOrReplaceChild("lHoofClaw02a", CubeListBuilder.create().texOffs(9, 58).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-0.5F, 9.5F, -1.3F, 0.0F, 0.1396F, 0.0349F));
		lHoofClaw02a.addOrReplaceChild("lHoofClaw02b", CubeListBuilder.create().texOffs(0, 57).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition realHead = root.addOrReplaceChild("realHead", CubeListBuilder.create().texOffs(1, 2).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F), PartPose.offsetAndRotation(0.0F, -12.7F, 1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rHorn01 = realHead.addOrReplaceChild("rHorn01", CubeListBuilder.create().texOffs(23, 0).mirror(true).addBox(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(-2.9F, -7.2F, -0.2F, -0.1745F, -0.1396F, -0.4014F));
		PartDefinition rHorn02 = rHorn01.addOrReplaceChild("rHorn02", CubeListBuilder.create().mirror(true).addBox(-0.4F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.3F, 0.3F, 0.3F)), PartPose.offsetAndRotation(0.0F, -2.2F, 0.0F, -0.1745F, 0.0F, -0.2618F));
		PartDefinition rHorn03 = rHorn02.addOrReplaceChild("rHorn03", CubeListBuilder.create().texOffs(0, 4).mirror(true).addBox(-0.3F, -3.0F, -0.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, -0.1047F, 0.0F, -0.1745F));
		PartDefinition rHorn04 = rHorn03.addOrReplaceChild("rHorn04", CubeListBuilder.create().mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.8F, 0.0F, 0.0524F, 0.0F, -0.1396F));
		rHorn04.addOrReplaceChild("rHorn05", CubeListBuilder.create().mirror(true).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.0524F, 0.0F, 0.1396F));
		PartDefinition lHorn01 = realHead.addOrReplaceChild("lHorn01", CubeListBuilder.create().texOffs(23, 0).addBox(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(2.9F, -7.2F, -0.2F, -0.1745F, 0.1396F, 0.4014F));
		PartDefinition lHorn02 = lHorn01.addOrReplaceChild("lHorn02", CubeListBuilder.create().addBox(-0.6F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.3F, 0.3F, 0.3F)), PartPose.offsetAndRotation(0.0F, -2.2F, 0.0F, -0.1745F, 0.0F, 0.2618F));
		PartDefinition lHorn03 = lHorn02.addOrReplaceChild("lHorn03", CubeListBuilder.create().texOffs(0, 4).addBox(-0.7F, -3.0F, -0.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, -0.1047F, 0.0F, 0.1745F));
		PartDefinition lHorn04 = lHorn03.addOrReplaceChild("lHorn04", CubeListBuilder.create().addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.8F, 0.0F, 0.0524F, 0.0F, 0.1396F));
		lHorn04.addOrReplaceChild("lHorn05", CubeListBuilder.create().addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.0524F, 0.0F, -0.1396F));
		realHead.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(29, 2).addBox(-2.0F, -1.9F, -5.1F, 4.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -4.6F, -2.5F, 0.5236F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("jawUpper00", CubeListBuilder.create().texOffs(43, 11).addBox(-2.5F, -1.0F, -5.0F, 5.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -2.5F, -2.2F, 0.0698F, 0.0F, 0.0F));
		PartDefinition jawLower = realHead.addOrReplaceChild("jawLower", CubeListBuilder.create().texOffs(48, 5).addBox(-2.0F, -0.5F, -4.0F, 4.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -1.0F, -3.0F, -0.0349F, 0.0F, 0.0F));
		jawLower.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(18, 59).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.3F, -2.4F, -0.0349F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("lEar", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(2.6F, -6.0F, 0.8F, -0.3491F, 0.0F, 0.3142F));
		realHead.addOrReplaceChild("rEar", CubeListBuilder.create().texOffs(48, 0).mirror(true).addBox(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-2.6F, -6.0F, 0.8F, -0.3491F, 0.0F, -0.3142F));
		PartDefinition torch00 = realHead.addOrReplaceChild("torch00", CubeListBuilder.create().texOffs(117, 0).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -6.6F, -1.2F, 0.0F, -0.7854F, 0.0F));
		PartDefinition torch01a = torch00.addOrReplaceChild("torch01a", CubeListBuilder.create().texOffs(117, 6).addBox(-0.8F, -3.8F, -0.8F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch01a.addOrReplaceChild("torch01b", CubeListBuilder.create().texOffs(117, 6).addBox(-0.2F, -3.8F, -0.8F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch01a.addOrReplaceChild("torch01c", CubeListBuilder.create().texOffs(117, 6).addBox(-0.8F, -3.8F, -0.2F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch01a.addOrReplaceChild("torch01d", CubeListBuilder.create().texOffs(117, 6).addBox(-0.2F, -3.8F, -0.2F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition torch02a = torch01a.addOrReplaceChild("torch02a", CubeListBuilder.create().texOffs(115, 12).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -3.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch02a.addOrReplaceChild("torch03a", CubeListBuilder.create().texOffs(122, 6).mirror(true).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-0.6F, -1.5F, -0.6F, 0.2443F, -0.1396F, -0.2793F));
		torch02a.addOrReplaceChild("torch03b", CubeListBuilder.create().texOffs(122, 6).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.6F, -1.5F, -0.6F, 0.2443F, 0.2094F, 0.2793F));
		torch02a.addOrReplaceChild("torch03d", CubeListBuilder.create().texOffs(122, 10).mirror(true).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-0.6F, -1.5F, 0.6F, -0.2443F, 0.2094F, -0.2793F));
		torch02a.addOrReplaceChild("torch03c", CubeListBuilder.create().texOffs(122, 10).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.6F, -1.5F, 0.6F, -0.2443F, -0.1396F, 0.2793F));
		torch02a.addOrReplaceChild("flame00", CubeListBuilder.create().texOffs(89, 10).addBox(0.0F, -6.2F, -2.0F, 0.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -1.6F, 0.0F, 0.0F, 0.0F, 0.0F));
		torch02a.addOrReplaceChild("flame01", CubeListBuilder.create().texOffs(89, 17).addBox(0.0F, -6.2F, -2.0F, 0.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -1.6F, 0.0F, 0.0F, -1.5708F, 0.0F));
		realHead.addOrReplaceChild("lCheekFur", CubeListBuilder.create().texOffs(31, 54).addBox(0.0F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F), PartPose.offsetAndRotation(2.4F, -2.4F, -0.3F, -0.6981F, -0.3491F, 0.576F));
		realHead.addOrReplaceChild("rCheekFur", CubeListBuilder.create().texOffs(31, 54).mirror(true).addBox(-5.0F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F), PartPose.offsetAndRotation(-2.4F, -2.4F, -0.3F, -0.6981F, 0.3491F, -0.576F));
		root.addOrReplaceChild("BipedLeftArm", CubeListBuilder.create().texOffs(46, 19).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 16.0F, 4.0F), PartPose.offsetAndRotation(5.0F, -10.8F, 0.0F, 0.0F, 0.0F, -0.1F));
		return LayerDefinition.create(data, 128, 64);
	}

	@Override
	public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		entity.setItemInHand(InteractionHand.MAIN_HAND, CADUCEUS);
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
		BipedLeftLeg.xRot -= 0.2793f;
		BipedLeftLeg.zRot -= 0.096f;
		copyRotation(BipedRightLeg, super.rightLeg);
		BipedRightLeg.xRot /= 2;
		BipedRightLeg.xRot -= 0.2793f;
		BipedRightLeg.zRot += 0.096f;
		lWing01.yRot = Mth.cos(animationProgress / 16) / 3 + 1 / 3f;
		rWing01.yRot = -lWing01.yRot;
		frontLoincloth.xRot = Math.min(BipedLeftLeg.xRot, BipedRightLeg.xRot) + 1 / 3f - 0.3491f;
		backLoincloth.xRot = Math.max(BipedLeftLeg.xRot, BipedRightLeg.xRot) + 1 / 3.75f;
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
