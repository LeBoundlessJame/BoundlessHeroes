package com.boundess_heroes;

import com.boundess_heroes.entity.GrappleEntityRenderer;
import com.boundess_heroes.registry.CustomRenderLogicRegistry;
import com.boundess_heroes.registry.EntityRegistry;
import com.boundess_heroes.registry.PayloadRegistry;
import com.boundless.entity.hero_action.HeroActionRenderer;
import com.boundless.registry.EntityRenderRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class BoundlessHeroesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomRenderLogicRegistry.initialize();
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.GRAPPLE_ENTITY, GrappleEntityRenderer::new);
        PayloadRegistry.registerS2CPackets();
    }
}
