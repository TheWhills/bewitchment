/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.SnakeEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class SnakeEntityModel<T extends SnakeEntity> extends AgeableListModel<T> {
	private final ModelPart bodyBase;
	private final ModelPart tail01;
	private final ModelPart tail02;
	private final ModelPart tail03;
	private final ModelPart tail04;
	private final ModelPart tail05;
	private final ModelPart tail06;
	private final ModelPart neck00;
	private final ModelPart neck01;
	private final ModelPart head;
	private final ModelPart lowerJaw;
	private final ModelPart lFang;
	private final ModelPart rFang;
	private final ModelPart tongue;

	public SnakeEntityModel(ModelPart root) {
		bodyBase = root.getChild("bodyBase");
		tail01 = bodyBase.getChild("tail01");
		tail02 = tail01.getChild("tail02");
		tail03 = tail02.getChild("tail03");
		tail04 = tail03.getChild("tail04");
		tail05 = tail04.getChild("tail05");
		tail06 = tail05.getChild("tail06");
		neck00 = bodyBase.getChild("neck00");
		neck01 = neck00.getChild("neck01");
		head = neck01.getChild("head");
		lowerJaw = head.getChild("lowerJaw");
		lFang = head.getChild("snout").getChild("lFang");
		rFang = head.getChild("snout").getChild("rFang");
		tongue = head.getChild("tongue");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		PartDefinition bodyBase = root.addOrReplaceChild("bodyBase", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 23.0F, -1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail01 = bodyBase.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(0, 21).addBox(-0.99F, -0.99F, 0.0F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail02 = tail01.addOrReplaceChild("tail02", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail03 = tail02.addOrReplaceChild("tail03", CubeListBuilder.create().texOffs(13, 21).addBox(-0.99F, -0.99F, 0.0F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail04 = tail03.addOrReplaceChild("tail04", CubeListBuilder.create().texOffs(24, 20).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail05 = tail04.addOrReplaceChild("tail05", CubeListBuilder.create().texOffs(35, 20).addBox(-0.49F, -0.99F, 0.0F, 1.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 3.3F, 0.0F, 0.0F, 0.0F));
		tail05.addOrReplaceChild("tail06", CubeListBuilder.create().texOffs(46, 21).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.4F, 3.8F, 0.0F, 0.0F, 0.0F));
		PartDefinition neck00 = bodyBase.addOrReplaceChild("neck00", CubeListBuilder.create().texOffs(0, 7).addBox(-0.99F, -0.99F, -3.0F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition neck01 = neck00.addOrReplaceChild("neck01", CubeListBuilder.create().addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -2.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition head = neck01.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 0).addBox(-1.5F, -1.52F, -2.1F, 3.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -2.6F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(19, 13).addBox(-1.09F, -0.5F, -3.8F, 2.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.9F, -0.4F, 0.0F, 0.0F, 0.0F));
		PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(20, 7).addBox(-1.0F, -1.0F, -2.9F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -0.6F, -2.0F, 0.2618F, 0.0F, 0.0F));
		snout.addOrReplaceChild("lFang", CubeListBuilder.create().texOffs(16, 0).addBox(-0.3F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-0.7F, 0.1F, -2.1F, -0.2618F, 0.0F, 0.0F));
		snout.addOrReplaceChild("rFang", CubeListBuilder.create().texOffs(16, 0).mirror(true).addBox(0.1F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.7F, 0.1F, -2.1F, -0.2618F, 0.0F, 0.0F));
		head.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(31, 0).addBox(-0.5F, 0.0F, -2.7F, 1.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.9F, -4.7F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 64, 32);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.xRot = (float) (headPitch * (Math.PI / 180f));
		head.yRot = (float) (headYaw * (Math.PI / 180f)) * 0.5f;
		tongue.xRot = Mth.cos(animationProgress * 3 + entity.getId()) / 3;
		tongue.visible = entity.tongueFlick > 0;
		boolean attacking = entity.attackTick > 0;
		lFang.visible = rFang.visible = attacking;
		if (attacking) {
			head.xRot = -1;
			lowerJaw.xRot = 1;
		} else {
			head.xRot = 0;
			lowerJaw.xRot = 0;
		}
		if (entity.isInSittingPose()) {
			neck00.yRot = -0.35f;
			neck01.yRot = -0.7f;
			tail01.yRot = 0.7f;
			tail02.yRot = 0.6f;
			tail03.yRot = 0.6f;
			tail04.yRot = 0.5f;
			tail05.yRot = 1.2f;
			tail06.yRot = 1.075f;
		} else {
			bodyBase.yRot = Mth.cos(limbAngle + entity.getId());
			neck00.yRot = -bodyBase.yRot / 3;
			neck01.yRot = neck00.yRot;
			tail01.yRot = neck01.yRot;
			tail02.yRot = tail01.yRot;
			tail03.yRot = tail02.yRot;
			tail04.yRot = tail03.yRot;
			tail05.yRot = tail04.yRot;
			tail06.yRot = tail05.yRot;
		}
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(bodyBase);
	}
}
