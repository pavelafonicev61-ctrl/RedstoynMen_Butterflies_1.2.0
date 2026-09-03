package com.redstoynmen.butterflies.client.render;

import com.redstoynmen.butterflies.ButterflyEntity;
import com.redstoynmen.butterflies.ModEntityTypes;
import com.redstoynmen.butterflies.RedstoynMenButterflies;
import com.redstoynmen.butterflies.client.RedstoynMenButterfliesClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public final class ButterflyRenderer extends MobRenderer<ButterflyEntity, ButterflyRenderState, ButterflyModel> {
	private static final Identifier COMMON_TEXTURE = Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, "textures/entity/butterfly/common.png");
	private static final Identifier GREEN_TEXTURE = Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, "textures/entity/butterfly/green.png");
	private static final Identifier RED_TEXTURE = Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, "textures/entity/butterfly/red.png");

	public ButterflyRenderer(EntityRendererProvider.Context context) {
		super(context, new ButterflyModel(context.bakeLayer(RedstoynMenButterfliesClient.BUTTERFLY)), 0.2f);
	}

	@Override
	public ButterflyRenderState createRenderState() {
		return new ButterflyRenderState();
	}

	@Override
	public Identifier getTextureLocation(ButterflyRenderState state) {
		return state.texture == null ? COMMON_TEXTURE : state.texture;
	}

	@Override
	public void extractRenderState(ButterflyEntity entity, ButterflyRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		if (entity.getType() == ModEntityTypes.GREEN_BUTTERFLY) {
			state.texture = GREEN_TEXTURE;
		} else if (entity.getType() == ModEntityTypes.RED_BUTTERFLY) {
			state.texture = RED_TEXTURE;
		} else {
			state.texture = COMMON_TEXTURE;
		}
	}
}