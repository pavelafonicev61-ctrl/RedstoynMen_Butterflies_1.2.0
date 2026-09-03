package com.redstoynmen.butterflies.client;

import com.redstoynmen.butterflies.ModEntityTypes;
import com.redstoynmen.butterflies.RedstoynMenButterflies;
import com.redstoynmen.butterflies.client.render.ButterflyModel;
import com.redstoynmen.butterflies.client.render.ButterflyRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRenderers;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public final class RedstoynMenButterfliesClient implements ClientModInitializer {
    public static final ModelLayerLocation BUTTERFLY = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(RedstoynMenButterflies.MOD_ID, "butterfly"), "main");

    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(BUTTERFLY, ButterflyModel::createBodyLayer);
        EntityRenderers.register(ModEntityTypes.COMMON_BUTTERFLY, ButterflyRenderer::new);
        EntityRenderers.register(ModEntityTypes.GREEN_BUTTERFLY, ButterflyRenderer::new);
        EntityRenderers.register(ModEntityTypes.RED_BUTTERFLY, ButterflyRenderer::new);
    }
}
