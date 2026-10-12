/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.equipment.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class WitchArmorModel<T extends LivingEntity> extends HumanoidModel<T> {
	public final ModelPart hat1;
	public final ModelPart hood01;
	public final ModelPart lowerLeftSkirt;
	public final ModelPart armorLeftBoot;
	public final ModelPart lowerRightSkirt;
	public final ModelPart armorRightBoot;

	public WitchArmorModel(ModelPart root) {
		super(root, RenderType::armorCutoutNoCull);
		young = false;
		hat1 = head.getChild("armorHead").getChild("hat1");
		hood01 = head.getChild("armorHead").getChild("hood01");
		lowerLeftSkirt = leftLeg.getChild("armorLeftLeg").getChild("lowerLeftSkirt");
		armorLeftBoot = leftLeg.getChild("armorLeftBoot");
		lowerRightSkirt = rightLeg.getChild("armorRightLeg").getChild("lowerRightSkirt");
		armorRightBoot = rightLeg.getChild("armorRightBoot");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition head = data.getRoot().getChild(PartNames.HEAD);
		PartDefinition body = data.getRoot().getChild(PartNames.BODY);
		PartDefinition rightLeg = data.getRoot().getChild(PartNames.RIGHT_LEG);
		PartDefinition leftLeg = data.getRoot().getChild(PartNames.LEFT_LEG);
		PartDefinition rightArm = data.getRoot().getChild(PartNames.RIGHT_ARM);
		PartDefinition leftArm = data.getRoot().getChild(PartNames.LEFT_ARM);

		PartDefinition armorRightLeg = rightLeg.addOrReplaceChild("armorRightLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(3.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorRightLeg.addOrReplaceChild("tunicRightFront", CubeListBuilder.create().texOffs(53, 65).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(-2.01F, 0.0F, -2.1F, -0.1222F, 0.0F, 0.0F));
		armorRightLeg.addOrReplaceChild("tunicRight", CubeListBuilder.create().texOffs(24, 65).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(-6.01F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		PartDefinition tunicRightBack = armorRightLeg.addOrReplaceChild("tunicRightBack", CubeListBuilder.create().texOffs(53, 65).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(-2.01F, 0.0F, 2.1F, 0.1222F, 0.0F, 0.0F));
		tunicRightBack.addOrReplaceChild("tunicRightBack2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition lowerRightSkirt = armorRightLeg.addOrReplaceChild("lowerRightSkirt", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tunicLowerFront2 = lowerRightSkirt.addOrReplaceChild("tunicLowerFront2", CubeListBuilder.create().texOffs(53, 71).addBox(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(1.99F, 0.0F, -2.1F, -0.1222F, 0.0F, 0.0F));
		tunicLowerFront2.addOrReplaceChild("tunicRightFront4", CubeListBuilder.create().texOffs(53, 77).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition tunicRight3 = lowerRightSkirt.addOrReplaceChild("tunicRight3", CubeListBuilder.create().texOffs(24, 72).addBox(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-2.01F, 0.0F, -2.1F, -0.1222F, 1.5708F, 0.0F));
		tunicRight3.addOrReplaceChild("tunicRight4", CubeListBuilder.create().texOffs(23, 78).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition tunicRightBack3 = lowerRightSkirt.addOrReplaceChild("tunicRightBack3", CubeListBuilder.create().texOffs(53, 72).addBox(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(1.99F, 0.0F, 2.1F, 0.1222F, 0.0F, 0.0F));
		tunicRightBack3.addOrReplaceChild("tunicRightBack4", CubeListBuilder.create().texOffs(53, 77).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		body.addOrReplaceChild("armorBody", CubeListBuilder.create().texOffs(0, 81).addBox(-4.5F, -0.01F, -2.5F, 9.0F, 12.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition armorLeftLeg = leftLeg.addOrReplaceChild("armorLeftLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tunicLeftFront = armorLeftLeg.addOrReplaceChild("tunicLeftFront", CubeListBuilder.create().texOffs(53, 65).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(1.99F, 0.0F, -2.1F, -3.0194F, 0.0F, 3.1416F));
		tunicLeftFront.addOrReplaceChild("tunicFront", CubeListBuilder.create(), PartPose.offsetAndRotation(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition tunicLeft = armorLeftLeg.addOrReplaceChild("tunicLeft", CubeListBuilder.create().texOffs(23, 65).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(6.01F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLeft.addOrReplaceChild("tunicLeft2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition tunicLeftBack = armorLeftLeg.addOrReplaceChild("tunicLeftBack", CubeListBuilder.create().texOffs(53, 65).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(1.99F, 0.0F, 2.1F, 3.0194F, 0.0F, 3.1416F));
		tunicLeftBack.addOrReplaceChild("tunicLeftBack2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition lowerLeftSkirt = armorLeftLeg.addOrReplaceChild("lowerLeftSkirt", CubeListBuilder.create(), PartPose.offsetAndRotation(11.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tunicLowerFront = lowerLeftSkirt.addOrReplaceChild("tunicLowerFront", CubeListBuilder.create().texOffs(53, 72).addBox(-1.01F, -3.1F, -0.77F, 4.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-6.0F, 10.0F, -3.1F, -3.0194F, 0.0F, -3.1416F));
		tunicLowerFront.addOrReplaceChild("tunicFrontFringe_r1", CubeListBuilder.create().texOffs(53, 77).addBox(-2.0F, 0.8F, -0.6F, 4.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(1.0F, -1.0F, -0.4F, 0.2618F, 0.0F, 0.0F));
		PartDefinition tunicLowerLeft = lowerLeftSkirt.addOrReplaceChild("tunicLowerLeft", CubeListBuilder.create().texOffs(23, 72).addBox(-4.0F, 7.0F, -1.0F, 4.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-4.99F, 0.0F, -2.1F, 0.1222F, 1.5708F, 0.0F));
		tunicLowerLeft.addOrReplaceChild("tunicLeft4", CubeListBuilder.create().texOffs(23, 78).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.01F, 10.0F, -1.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition tunicLeftBack3 = lowerLeftSkirt.addOrReplaceChild("tunicLeftBack3", CubeListBuilder.create().texOffs(53, 72).addBox(-4.0F, 7.0F, 0.0F, 4.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-9.01F, 0.0F, 2.1F, 3.0194F, 0.0F, 3.1416F));
		tunicLeftBack3.addOrReplaceChild("tunicLeftBack4", CubeListBuilder.create().texOffs(53, 77).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.01F, 10.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition armorHead = head.addOrReplaceChild("armorHead", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition hat1 = armorHead.addOrReplaceChild("hat1", CubeListBuilder.create().texOffs(49, 111).addBox(-4.5F, -9.5F, -4.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition hat2 = hat1.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(89, 114).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -9.5F, -3.0F, -0.2618F, 0.0F, 0.0F));
		hat2.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(110, 112).addBox(0.0F, -4.0F, 0.0F, 3.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(-1.4F, -4.0F, 1.5F, -0.3491F, 0.0F, 0.0F));
		hat1.addOrReplaceChild("hatWing", CubeListBuilder.create().texOffs(0, 112).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 1.0F, 12.0F), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0349F, 0.0F, 0.0698F));
		PartDefinition hood01 = armorHead.addOrReplaceChild("hood01", CubeListBuilder.create().texOffs(70, 0).addBox(-4.5F, -8.6F, -4.7F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		hood01.addOrReplaceChild("hoodFringe01", CubeListBuilder.create().texOffs(66, 0).addBox(-3.0F, -8.9F, -5.4F, 6.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.0F, 0.0F, 0.0F));
		hood01.addOrReplaceChild("hoodFringeL01", CubeListBuilder.create().texOffs(77, 24).mirror(true).addBox(0.0F, -0.3F, -5.39F, 2.0F, 8.0F, 10.0F), PartPose.offsetAndRotation(-4.1F, -8.2F, 0.1F, 0.0F, 0.0F, 0.2793F));
		hood01.addOrReplaceChild("hoodFringeR01", CubeListBuilder.create().texOffs(77, 24).addBox(-2.0F, -0.3F, -5.39F, 2.0F, 8.0F, 10.0F), PartPose.offsetAndRotation(4.1F, -8.2F, 0.1F, 0.0F, 0.0F, -0.2793F));
		hood01.addOrReplaceChild("hoodFringeL02", CubeListBuilder.create().texOffs(97, 33).addBox(0.0F, -0.9F, -5.37F, 5.0F, 2.0F, 10.0F), PartPose.offsetAndRotation(0.85F, 0.55F, 0.1F, 0.0F, 0.0F, -0.3142F));
		hood01.addOrReplaceChild("hoodFringeR02", CubeListBuilder.create().texOffs(97, 33).mirror(true).addBox(-5.0F, -0.9F, -5.37F, 5.0F, 2.0F, 10.0F), PartPose.offsetAndRotation(-0.85F, 0.55F, 0.1F, 0.0F, 0.0F, 0.3142F));
		hood01.addOrReplaceChild("hoodShade", CubeListBuilder.create().texOffs(105, 0).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -7.3F, -5.2F, 0.0524F, 0.0F, 0.0F));
		hood01.addOrReplaceChild("hoodFringeL04", CubeListBuilder.create().texOffs(57, 20).addBox(-0.81F, -1.0F, -5.37F, 4.0F, 2.0F, 11.0F), PartPose.offsetAndRotation(0.3F, -8.9F, 0.1F, 0.0F, 0.0F, 0.3142F));
		hood01.addOrReplaceChild("hoodFringeR04", CubeListBuilder.create().texOffs(57, 20).mirror(true).addBox(-3.0F, -1.0F, -5.38F, 4.0F, 2.0F, 11.0F), PartPose.offsetAndRotation(-0.8F, -8.9F, 0.1F, 0.0F, 0.0F, -0.3142F));
		PartDefinition hoodPipe01 = hood01.addOrReplaceChild("hoodPipe01", CubeListBuilder.create().texOffs(65, 43).addBox(-3.0F, -2.5F, 0.0F, 6.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -7.0F, 4.2F, -0.4014F, 0.0F, 0.0F));
		hoodPipe01.addOrReplaceChild("hoodPipe02", CubeListBuilder.create().texOffs(65, 54).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -0.3F, 3.1F, -0.4538F, 0.0F, 0.0F));
		PartDefinition armorLeftBoot = leftLeg.addOrReplaceChild("armorLeftBoot", CubeListBuilder.create().texOffs(86, 81).addBox(-2.5F, 6.0F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)).texOffs(110, 89).addBox(-2.5F, 11.0F, -4.25F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorLeftBoot.addOrReplaceChild("lBootClaw03_r1", CubeListBuilder.create().texOffs(87, 94).addBox(-1.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(-0.5F, 8.75F, -2.25F, 0.6545F, 0.1745F, 0.0F));
		armorLeftBoot.addOrReplaceChild("lBootClaw02_r1", CubeListBuilder.create().texOffs(87, 94).addBox(-1.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(1.5F, 8.75F, -2.25F, 0.6545F, -0.1745F, 0.0F));
		armorLeftBoot.addOrReplaceChild("lBootClaw01_r1", CubeListBuilder.create().texOffs(87, 94).addBox(-1.0F, 0.0F, -4.25F, 1.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(0.5F, 8.75F, -2.25F, 0.6545F, 0.0F, 0.0F));
		PartDefinition armorRightArm = rightArm.addOrReplaceChild("armorRightArm", CubeListBuilder.create().texOffs(47, 82).addBox(-4.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorRightArm.addOrReplaceChild("rShoulder_r1", CubeListBuilder.create().texOffs(0, 68).addBox(0.25F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 3.1416F, 0.0873F));
		armorRightArm.addOrReplaceChild("lArmSleeve02_r1", CubeListBuilder.create().texOffs(50, 100).mirror(true).addBox(-2.3F, -2.2F, -0.5F, 4.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, 7.0F, 2.0F, 0.5236F, 0.0F, 0.0F));
		PartDefinition armorRightBoot = rightLeg.addOrReplaceChild("armorRightBoot", CubeListBuilder.create().texOffs(86, 81).mirror(true).addBox(-2.5F, 6.0F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)).texOffs(110, 89).addBox(-2.5F, 11.0F, -4.25F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorRightBoot.addOrReplaceChild("rBootClaw03_r1", CubeListBuilder.create().texOffs(87, 94).mirror(true).addBox(0.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(0.5F, 8.75F, -2.25F, 0.6545F, -0.1745F, 0.0F));
		armorRightBoot.addOrReplaceChild("rBootClaw02_r1", CubeListBuilder.create().texOffs(87, 94).mirror(true).addBox(0.0F, 0.0F, -4.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(-1.5F, 8.75F, -2.25F, 0.6545F, 0.1745F, 0.0F));
		armorRightBoot.addOrReplaceChild("rBootClaw01_r1", CubeListBuilder.create().texOffs(87, 94).mirror(true).addBox(0.0F, 0.0F, -4.25F, 1.0F, 2.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(-0.5F, 8.75F, -2.25F, 0.6545F, 0.0F, 0.0F));
		PartDefinition armorLeftArm = leftArm.addOrReplaceChild("armorLeftArm", CubeListBuilder.create().texOffs(47, 82).mirror(true).addBox(-0.5F, -2.9F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		armorLeftArm.addOrReplaceChild("lShoulder_r1", CubeListBuilder.create().texOffs(0, 68).addBox(0.25F, -2.0F, -3.0F, 5.0F, 5.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
		armorLeftArm.addOrReplaceChild("lArmSleeve02_r2", CubeListBuilder.create().texOffs(50, 100).addBox(-1.7F, -2.2F, -0.5F, 4.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(2.0F, 7.0F, 2.0F, 0.5236F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 128, 128);
	}
}
