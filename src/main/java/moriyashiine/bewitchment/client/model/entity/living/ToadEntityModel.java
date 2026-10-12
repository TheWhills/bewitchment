/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ToadEntityModel<T extends ToadEntity> extends AgeableListModel<T> {
	private final ModelPart body;
	private final ModelPart lLeg00;
	private final ModelPart lLeg01;
	private final ModelPart lFoot;
	private final ModelPart rLeg00;
	private final ModelPart rLeg01;
	private final ModelPart rFoot;
	private final ModelPart lArm00;
	private final ModelPart lHand;
	private final ModelPart rArm00;
	private final ModelPart rHand;
	private final ModelPart head;

	public ToadEntityModel(ModelPart root) {
		body = root.getChild("body");
		lLeg00 = body.getChild("lLeg00");
		lLeg01 = lLeg00.getChild("lLeg01");
		lFoot = lLeg01.getChild("lFoot");
		rLeg00 = body.getChild("rLeg00");
		rLeg01 = rLeg00.getChild("rLeg01");
		rFoot = rLeg01.getChild("rFoot");
		lArm00 = body.getChild("lArm00");
		lHand = lArm00.getChild("lArm01").getChild("lHand");
		rArm00 = body.getChild("rArm00");
		rHand = rArm00.getChild("rArm01").getChild("rHand");
		head = body.getChild("head");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().addBox(-3.5F, -2.0F, -3.5F, 7.0F, 4.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 18.8F, 0.7F, -0.4014F, 0.0F, 0.0F));
		PartDefinition lLeg00 = body.addOrReplaceChild("lLeg00", CubeListBuilder.create().texOffs(0, 13).addBox(-2.0F, -1.0F, -1.6F, 2.0F, 5.0F, 3.0F), PartPose.offsetAndRotation(3.7F, -0.2795F, 3.6907F, -0.7854F, -0.3491F, -0.1396F));
		PartDefinition lLeg01 = lLeg00.addOrReplaceChild("lLeg01", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-1.1F, 3.2F, 0.6F, 0.8727F, 0.0F, 0.0F));
		lLeg01.addOrReplaceChild("lFoot", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 1.3F, 3.4F, 0.3142F, 0.0F, 0.0F));
		PartDefinition rLeg00 = body.addOrReplaceChild("rLeg00", CubeListBuilder.create().texOffs(0, 13).mirror(true).addBox(0.0F, -1.0F, -1.6F, 2.0F, 5.0F, 3.0F), PartPose.offsetAndRotation(-3.7F, -0.2795F, 3.6907F, -0.7854F, 0.3491F, 0.1396F));
		PartDefinition rLeg01 = rLeg00.addOrReplaceChild("rLeg01", CubeListBuilder.create().texOffs(0, 21).mirror(true).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(1.1F, 3.2F, 0.6F, 0.8727F, 0.0F, 0.0F));
		rLeg01.addOrReplaceChild("rFoot", CubeListBuilder.create().texOffs(0, 27).mirror(true).addBox(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 1.3F, 3.4F, 0.3142F, 0.0F, 0.0F));
		PartDefinition lArm00 = body.addOrReplaceChild("lArm00", CubeListBuilder.create().texOffs(10, 13).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(4.1F, 0.5F, -2.0F, 0.3491F, 0.0F, -0.3491F));
		PartDefinition lArm01 = lArm00.addOrReplaceChild("lArm01", CubeListBuilder.create().texOffs(11, 18).addBox(0.0F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(-1.3F, 1.4F, 0.3F, -0.3491F, 0.0F, 0.2618F));
		lArm01.addOrReplaceChild("lHand", CubeListBuilder.create().texOffs(-2, 0).addBox(-1.0F, 0.2F, -2.7F, 3.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 3.8F, -0.3F, 0.4538F, -0.1745F, 0.0F));
		PartDefinition rArm00 = body.addOrReplaceChild("rArm00", CubeListBuilder.create().texOffs(10, 13).mirror(true).addBox(0.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(-4.1F, 0.5F, -2.0F, 0.3491F, 0.0F, 0.3491F));
		PartDefinition rArm01 = rArm00.addOrReplaceChild("rArm01", CubeListBuilder.create().texOffs(11, 18).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(1.3F, 1.4F, 0.3F, -0.3491F, 0.0F, -0.2618F));
		rArm01.addOrReplaceChild("rHand", CubeListBuilder.create().texOffs(-2, 0).mirror(true).addBox(-2.0F, 0.2F, -2.7F, 3.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 3.8F, -0.3F, 0.4538F, 0.1745F, 0.0F));
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 20).addBox(-2.5F, -1.5F, -5.0F, 5.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -0.9F, -2.9F, 0.4014F, 0.0F, 0.0F));
		PartDefinition throat = head.addOrReplaceChild("throat", CubeListBuilder.create().texOffs(18, 14).addBox(-2.49F, -0.5F, -1.5F, 5.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.9F, -0.4F, 0.0F, 0.0F, 0.0F));
		throat.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(13, 28).addBox(-2.1F, -0.5F, -2.0F, 4.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 32, 32);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		boolean altLegs = false;
		head.xRot = (float) (headPitch * (Math.PI / 180f)) + 0.5f;
		head.yRot = (float) (headYaw * (Math.PI / 180f)) * 1 / 3f;
		if (entity.isInSittingPose() || !entity.onGround()) {
			head.xRot -= 1 / 3f;
			body.xRot = 0;
			lArm00.xRot = -2 / 3f;
			lHand.xRot = 1.5f;
			lLeg00.xRot = -1 / 3f;
			lLeg00.yRot = 0;
			if (!entity.onGround()) {
				lLeg00.xRot = 1;
				altLegs = true;
			}
		} else {
			body.xRot = -0.4014f;
			lArm00.xRot = 0.3491f;
			lHand.xRot = 0.4538f;
			lLeg00.xRot = -0.7854f;
			lLeg00.yRot = -0.3491f;
		}
		if (altLegs) {
			lLeg01.xRot = -0.5f;
			lFoot.xRot = 1.5f;
		} else {
			lLeg01.xRot = 0.8727f;
			lFoot.xRot = 0.3142f;
		}
		rArm00.xRot = lArm00.xRot;
		rHand.xRot = lHand.xRot;
		rLeg00.xRot = lLeg00.xRot;
		rLeg00.yRot = -lLeg00.yRot;
		rLeg01.xRot = lLeg01.xRot;
		rFoot.xRot = lFoot.xRot;
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(body);
	}
}