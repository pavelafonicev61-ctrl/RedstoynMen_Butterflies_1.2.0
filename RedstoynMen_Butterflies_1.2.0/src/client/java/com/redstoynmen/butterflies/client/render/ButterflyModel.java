package com.redstoynmen.butterflies.client.render;

import com.redstoynmen.butterflies.client.RedstoynMenButterfliesClient;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public final class ButterflyModel extends EntityModel<ButterflyRenderState> {
	private final ModelPart body;
	private final ModelPart leftWing;
	private final ModelPart rightWing;

	public ButterflyModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.leftWing = root.getChild("left_wing");
		this.rightWing = root.getChild("right_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, -1.0f, -2.0f, 2.0f, 2.0f, 4.0f),
				PartPose.offset(0.0f, 20.0f, 0.0f));
		root.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 8).addBox(0.0f, -1.0f, -1.0f, 5.0f, 1.0f, 4.0f),
				PartPose.offset(1.0f, 19.0f, 0.0f));
		root.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 13).addBox(-5.0f, -1.0f, -1.0f, 5.0f, 1.0f, 4.0f),
				PartPose.offset(-1.0f, 19.0f, 0.0f));
		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public void setupAnim(ButterflyRenderState state) {
		super.setupAnim(state);
		float flap = Mth.cos(state.ageInTicks * 1.8f) * 0.58f;
		this.leftWing.zRot = flap;
		this.rightWing.zRot = -flap;
		this.body.yRot = state.bodyRot * Mth.DEG_TO_RAD;
	}
}