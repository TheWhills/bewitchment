/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.HerneEntity;
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

public class HerneEntityModel<T extends HerneEntity> extends HumanoidModel<T> {
	public static final ItemStack HORNED_SPEAR = new ItemStack(BWObjects.HORNED_SPEAR.get());

	private final ModelPart chest;
	private final ModelPart moss01;
	private final ModelPart moss02;
	private final ModelPart mossL;
	private final ModelPart mossR;
	private final ModelPart bipedLeftArm;
	private final ModelPart bipedRightArm;
	private final ModelPart bipedLeftLeg;
	private final ModelPart bipedRightLeg;
	private final ModelPart neck00;

	private boolean realArm = false;

	public HerneEntityModel(ModelPart root) {
		super(root);
		chest = root.getChild("chest");
		moss01 = chest.getChild("stomach").getChild("moss01");
		moss02 = chest.getChild("stomach").getChild("moss02");
		mossL = chest.getChild("stomach").getChild("mossL");
		mossR = chest.getChild("stomach").getChild("mossR");
		bipedLeftArm = root.getChild("bipedLeftArm");
		bipedRightArm = root.getChild("bipedRightArm");
		bipedLeftLeg = root.getChild("bipedLeftLeg");
		bipedRightLeg = root.getChild("bipedRightLeg");
		neck00 = root.getChild("neck00");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = data.getRoot();
		PartDefinition bipedLeftLeg = root.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -0.7F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(-0.2F, -0.2F, -0.2F)), PartPose.offsetAndRotation(2.5F, -0.2F, 0.6F, 0.0F, 0.0F, 0.0F));
		PartDefinition lLeg02 = bipedLeftLeg.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(-0.3F, -0.3F, -0.3F)), PartPose.offsetAndRotation(0.0F, 9.5F, -0.7F, 0.733F, 0.0F, 0.0873F));
		PartDefinition lLeg03 = lLeg02.addOrReplaceChild("lLeg03", CubeListBuilder.create().texOffs(0, 47).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(-0.15F, -0.15F, -0.15F)), PartPose.offsetAndRotation(0.0F, 7.1F, 0.1F, -0.4538F, 0.0F, 0.0F));
		PartDefinition lHoofClaw01a = lLeg03.addOrReplaceChild("lHoofClaw01a", CubeListBuilder.create().texOffs(9, 60).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.7F, 8.4F, -1.4F, 0.0524F, -0.1047F, 0.0F));
		lHoofClaw01a.addOrReplaceChild("lHoofClaw01b", CubeListBuilder.create().texOffs(0, 59).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -0.8F, 0.3491F, 0.0F, 0.0F));
		PartDefinition lHoofClaw02a = lLeg03.addOrReplaceChild("lHoofClaw02a", CubeListBuilder.create().texOffs(9, 60).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-0.6F, 8.4F, -1.4F, 0.0524F, 0.0524F, 0.0F));
		lHoofClaw02a.addOrReplaceChild("lHoofClaw02b", CubeListBuilder.create().texOffs(0, 59).addBox(-0.49F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -0.8F, 0.3491F, 0.0F, 0.0F));
		PartDefinition bipedRightArm = root.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(58, 18).mirror(true).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 20.0F, 4.0F), PartPose.offsetAndRotation(-5.5F, -15.7F, 1.0F, 0.0F, 0.0F, 0.0F));
		bipedRightArm.addOrReplaceChild("rMantle", CubeListBuilder.create().texOffs(69, 45).mirror(true).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(0.1F, -0.3F, -0.5F, 0.0F, 0.0F, 0.1309F));
		PartDefinition chest = root.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(23, 15).addBox(-4.9F, -8.0F, -3.5F, 10.0F, 8.0F, 7.0F), PartPose.offsetAndRotation(0.0F, -9.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		chest.addOrReplaceChild("lPec", CubeListBuilder.create().texOffs(19, 50).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F), PartPose.offsetAndRotation(2.6F, -4.8F, -1.2F, 0.0F, -0.1047F, 0.0873F));
		chest.addOrReplaceChild("rPec", CubeListBuilder.create().texOffs(19, 50).mirror(true).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 3.0F), PartPose.offsetAndRotation(-2.6F, -4.8F, -1.2F, 0.0F, 0.1047F, -0.0873F));
		PartDefinition stomach = chest.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(19, 31).addBox(-4.5F, -6.5F, -3.0F, 9.0F, 10.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 6.2F, 0.5F, 0.0F, 0.0F, 0.0F));
		stomach.addOrReplaceChild("moss01", CubeListBuilder.create().texOffs(92, 45).addBox(-5.0F, 0.0F, -0.5F, 10.0F, 9.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 1.4F, -2.5F, -0.3665F, 0.0F, 0.0F));
		stomach.addOrReplaceChild("moss02", CubeListBuilder.create().texOffs(39, 53).addBox(-5.0F, 0.0F, -0.5F, 10.0F, 9.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 1.4F, 2.8F, 0.0F, 0.0F, 0.0F));
		stomach.addOrReplaceChild("mossL", CubeListBuilder.create().texOffs(111, 48).mirror(true).addBox(-0.5F, -0.7F, -3.5F, 1.0F, 9.0F, 7.0F), PartPose.offsetAndRotation(-4.7F, 2.1F, -0.1F, -0.1745F, 0.0F, 0.1745F));
		stomach.addOrReplaceChild("mossR", CubeListBuilder.create().texOffs(111, 48).addBox(-0.5F, -0.7F, -3.5F, 1.0F, 9.0F, 7.0F), PartPose.offsetAndRotation(4.7F, 2.1F, -0.1F, -0.1745F, 0.0F, -0.1745F));
		chest.addOrReplaceChild("mMantle", CubeListBuilder.create().texOffs(75, 26).addBox(-6.0F, 0.0F, -4.5F, 12.0F, 10.0F, 9.0F), PartPose.offsetAndRotation(0.0F, -8.8F, -0.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition bipedLeftArm = root.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create().texOffs(58, 18).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 20.0F, 4.0F), PartPose.offsetAndRotation(5.5F, -15.7F, 1.0F, 0.0F, 0.0F, 0.0F));
		bipedLeftArm.addOrReplaceChild("lMantle", CubeListBuilder.create().texOffs(69, 45).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(-0.1F, -0.3F, -0.5F, 0.0F, 0.0F, -0.1309F));
		PartDefinition bipedRightLeg = root.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create().texOffs(0, 16).mirror(true).addBox(-2.5F, -0.7F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(-0.2F, -0.2F, -0.2F)), PartPose.offsetAndRotation(-2.5F, -0.2F, 0.6F, 0.0F, 0.0F, 0.0F));
		PartDefinition rLeg02 = bipedRightLeg.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(0, 34).mirror(true).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(-0.3F, -0.3F, -0.3F)), PartPose.offsetAndRotation(0.0F, 9.5F, -0.7F, 0.733F, 0.0F, -0.0873F));
		PartDefinition rLeg03 = rLeg02.addOrReplaceChild("rLeg03", CubeListBuilder.create().texOffs(0, 47).mirror(true).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(-0.15F, -0.15F, -0.15F)), PartPose.offsetAndRotation(0.0F, 7.1F, 0.1F, -0.4538F, 0.0F, 0.0F));
		PartDefinition rHoofClaw01a = rLeg03.addOrReplaceChild("rHoofClaw01a", CubeListBuilder.create().texOffs(9, 60).mirror(true).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(-0.7F, 8.4F, -1.4F, 0.0524F, 0.1047F, 0.0F));
		rHoofClaw01a.addOrReplaceChild("rHoofClaw01b", CubeListBuilder.create().texOffs(0, 59).mirror(true).addBox(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -0.8F, 0.3491F, 0.0F, 0.0F));
		PartDefinition rHoofClaw02a = rLeg03.addOrReplaceChild("rHoofClaw02a", CubeListBuilder.create().texOffs(9, 60).mirror(true).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.6F, 8.4F, -1.4F, 0.0524F, -0.0524F, 0.0F));
		rHoofClaw02a.addOrReplaceChild("rHoofClaw02b", CubeListBuilder.create().texOffs(0, 59).mirror(true).addBox(-0.51F, -1.1F, -1.2F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -0.8F, 0.3491F, 0.0F, 0.0F));
		PartDefinition neck00 = root.addOrReplaceChild("neck00", CubeListBuilder.create().texOffs(51, 0).addBox(-3.5F, -1.7F, -3.0F, 7.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -17.5F, -0.1F, 0.0F, 0.0F, 0.0F));
		PartDefinition neck01 = neck00.addOrReplaceChild("neck01", CubeListBuilder.create().texOffs(28, 0).addBox(-3.11F, -3.0F, -2.5F, 6.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -1.2206F, -0.584F, 0.2182F, 0.0F, 0.0F));
		PartDefinition realHead = neck01.addOrReplaceChild("realHead", CubeListBuilder.create().addBox(-3.0F, -4.8F, -3.6F, 6.0F, 5.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -2.8F, 0.2F, -0.1396F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(78, 0).addBox(-2.0F, -1.35F, -3.9F, 4.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -3.2F, -4.1F, 0.2793F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(97, 10).addBox(-2.0F, -0.4F, -3.6F, 4.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -0.5F, -3.9F, 0.0F, 0.0F, 0.0F));
		realHead.addOrReplaceChild("lEar", CubeListBuilder.create().texOffs(20, 0).mirror(true).addBox(0.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(2.3F, -3.4F, 0.9F, 0.1745F, -0.3491F, -0.4538F));
		realHead.addOrReplaceChild("rEar", CubeListBuilder.create().texOffs(20, 0).mirror(true).addBox(-3.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-2.3F, -3.4F, 0.9F, 0.1745F, 0.3491F, 0.4538F));
		PartDefinition lAntler01 = realHead.addOrReplaceChild("lAntler01", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-1.0F, -2.3F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(1.5F, -4.6F, -0.6F, -0.3491F, 0.0F, 0.2793F));
		PartDefinition lAntler02a = lAntler01.addOrReplaceChild("lAntler02a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.2F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F), PartPose.offsetAndRotation(-0.2F, -1.7F, -0.3F, 0.7854F, 0.5236F, 0.1396F));
		PartDefinition lAntler02b = lAntler02a.addOrReplaceChild("lAntler02b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.7F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lAntler03 = lAntler02b.addOrReplaceChild("lAntler03", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.2F, 0.0F, 5.4F, 0.6109F, -0.2618F, 0.0F));
		PartDefinition lAntler04 = lAntler03.addOrReplaceChild("lAntler04", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.8F, -0.5236F, 0.0F, 0.0F));
		lAntler04.addOrReplaceChild("lAntler04b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.8F, 0.3491F, -0.2618F, 0.0F));
		PartDefinition lAntler07a = lAntler03.addOrReplaceChild("lAntler07a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.1F, 1.0F, 0.8727F, 0.0F, -0.5934F));
		lAntler07a.addOrReplaceChild("lAntler07b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.9F, 0.0F, 0.2793F, 0.0F, 0.0F));
		PartDefinition lAntler08a = lAntler03.addOrReplaceChild("lAntler08a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.1F, 1.2F, 0.6981F, 0.0F, 0.3491F));
		lAntler08a.addOrReplaceChild("lAntler08b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.9F, 0.0F, 0.2793F, 0.0F, 0.3491F));
		PartDefinition lAntler09a = lAntler03.addOrReplaceChild("lAntler09a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -0.4F, -1.0F, 0.6109F, -0.1222F, 0.4363F));
		lAntler09a.addOrReplaceChild("lAntler09b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 4.8F, 0.0F, 0.2793F, 0.0F, 0.3491F));
		PartDefinition lAntler05a = lAntler02a.addOrReplaceChild("lAntler05a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.5F, -0.4F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.4F, 4.8F, -0.5236F, 0.0F, 0.3665F));
		PartDefinition lAntler05b = lAntler05a.addOrReplaceChild("lAntler05b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.5F, -0.9F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lAntler05c = lAntler05b.addOrReplaceChild("lAntler05c", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(-0.1F, -2.9F, -0.2F, 0.0F, 0.0F, -0.1745F));
		lAntler05c.addOrReplaceChild("lAntler05d", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.1F, -3.1F, 0.0F, 0.0F, 0.0F, -0.3491F));
		PartDefinition lAntler10a = lAntler02a.addOrReplaceChild("lAntler10a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.4F, -0.4F, 0.7F, 0.0F, 0.0F, 0.3665F));
		lAntler10a.addOrReplaceChild("lAntler10b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.1F, -3.1F, 0.0F, 0.0F, 0.0F, -0.3142F));
		PartDefinition lAntler11a = lAntler02a.addOrReplaceChild("lAntler11a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.4F, 0.0F, -0.2F, 0.4363F, 0.0F, 0.4363F));
		lAntler11a.addOrReplaceChild("lAntler11b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.1F, -2.8F, 0.0F, 0.0F, 0.0F, -0.3142F));
		PartDefinition rAntler01 = realHead.addOrReplaceChild("rAntler01", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-1.0F, -2.2F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(-1.5F, -4.7F, -0.6F, -0.3491F, 0.0F, -0.2793F));
		PartDefinition rAntler02a = rAntler01.addOrReplaceChild("rAntler02a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.2F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F), PartPose.offsetAndRotation(0.2F, -1.7F, -0.3F, 0.7854F, -0.5236F, -0.1396F));
		PartDefinition rAntler02b = rAntler02a.addOrReplaceChild("rAntler02b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.7F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rAntler03 = rAntler02b.addOrReplaceChild("rAntler03", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(-0.2F, 0.0F, 5.4F, 0.6109F, 0.2618F, 0.0F));
		PartDefinition rAntler04 = rAntler03.addOrReplaceChild("rAntler04", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.8F, -0.5236F, 0.0F, 0.0F));
		rAntler04.addOrReplaceChild("rAntler04b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.8F, 0.3491F, 0.2618F, 0.0F));
		PartDefinition rAntler07a = rAntler03.addOrReplaceChild("rAntler07a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.1F, 1.0F, 0.8727F, 0.0F, 0.5934F));
		rAntler07a.addOrReplaceChild("rAntler07b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.9F, 0.0F, 0.2793F, 0.0F, 0.0F));
		PartDefinition rAntler08a = rAntler03.addOrReplaceChild("rAntler08a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.1F, 1.2F, 0.6981F, 0.0F, -0.3491F));
		rAntler08a.addOrReplaceChild("rAntler08b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 2.9F, 0.0F, 0.2793F, 0.0F, -0.3491F));
		PartDefinition rAntler09a = rAntler03.addOrReplaceChild("rAntler09a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -0.4F, -1.0F, 0.6109F, 0.1222F, -0.4363F));
		rAntler09a.addOrReplaceChild("rAntler09b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 4.8F, 0.0F, 0.2793F, 0.0F, -0.3491F));
		PartDefinition rAntler05a = rAntler02a.addOrReplaceChild("rAntler05a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.5F, -0.4F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.4F, 4.8F, -0.5236F, 0.0F, -0.3665F));
		PartDefinition rAntler05b = rAntler05a.addOrReplaceChild("rAntler05b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.5F, -0.9F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rAntler05c = rAntler05b.addOrReplaceChild("rAntler05c", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.1F, -2.9F, -0.2F, 0.0F, 0.0F, 0.1745F));
		rAntler05c.addOrReplaceChild("rAntler05d", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-0.1F, -3.1F, 0.0F, 0.0F, 0.0F, 0.3491F));
		PartDefinition rAntler10a = rAntler02a.addOrReplaceChild("rAntler10a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(-0.4F, -0.4F, 0.7F, 0.0F, 0.0F, -0.3665F));
		rAntler10a.addOrReplaceChild("rAntler10b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-0.1F, -3.1F, 0.0F, 0.0F, 0.0F, 0.3142F));
		PartDefinition rAntler11a = rAntler02a.addOrReplaceChild("rAntler11a", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-0.4F, 0.0F, -0.2F, 0.4363F, 0.0F, -0.4363F));
		rAntler11a.addOrReplaceChild("rAntler11b", CubeListBuilder.create().texOffs(107, 0).mirror(true).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.1F, -2.8F, 0.0F, 0.0F, 0.0F, 0.3142F));
		realHead.addOrReplaceChild("upperJaw00", CubeListBuilder.create().texOffs(76, 8).addBox(-2.5F, -0.7F, -3.7F, 5.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -2.0F, -4.1F, 0.0F, 0.0F, 0.0F));
		neck00.addOrReplaceChild("neckFur01", CubeListBuilder.create().texOffs(79, 17).addBox(-2.0F, -1.0F, -1.8F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -1.75F, -1.9F, -0.6109F, 0.0F, 0.0F));
		neck00.addOrReplaceChild("neckFur02", CubeListBuilder.create().texOffs(110, 16).addBox(-3.0F, -1.0F, -1.9F, 6.0F, 8.0F, 2.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, 0.5F, -2.9F, -0.1309F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 128, 64);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float realHeadYaw, float realHeadPitch) {
		entity.setItemInHand(InteractionHand.MAIN_HAND, HORNED_SPEAR);
		realArm = false;
		super.setupAnim(entity, limbAngle, limbDistance, animationProgress, realHeadYaw, realHeadPitch);
		realArm = true;
		copyRotation(neck00, super.head);
		copyRotation(chest, super.body);
		copyRotation(bipedLeftArm, super.leftArm);
		bipedLeftArm.zRot -= 0.1309f;
		copyRotation(bipedRightArm, super.rightArm);
		bipedRightArm.zRot += 0.1309f;
		copyRotation(bipedLeftLeg, super.leftLeg);
		bipedLeftLeg.xRot /= 2;
		bipedLeftLeg.xRot -= 0.2793f;
		bipedLeftLeg.zRot -= 0.1047f;
		copyRotation(bipedRightLeg, super.rightLeg);
		bipedRightLeg.xRot /= 2;
		bipedRightLeg.xRot -= 0.2793f;
		bipedRightLeg.zRot += 0.1047f;
		moss01.xRot = Math.min(bipedLeftLeg.xRot, bipedRightLeg.xRot);
		mossL.xRot = moss01.xRot;
		mossR.xRot = moss01.xRot;
		moss02.xRot = Math.max(bipedLeftLeg.xRot, bipedRightLeg.xRot) + 1 / 3f;
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		neck00.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		chest.render(matrices, vertices, light, overlay, red, green, blue, alpha);
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
