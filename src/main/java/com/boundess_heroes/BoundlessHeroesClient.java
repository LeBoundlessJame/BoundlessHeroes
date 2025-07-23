package com.boundess_heroes;

import com.boundess_heroes.entity.GrappleEntityRenderer;
import com.boundess_heroes.registry.EntityRegistry;
import com.boundess_heroes.registry.PayloadRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class BoundlessHeroesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.GRAPPLE_ENTITY, GrappleEntityRenderer::new);
        PayloadRegistry.registerS2CPackets();
    }
}
