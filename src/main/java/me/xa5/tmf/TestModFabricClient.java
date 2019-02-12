package me.xa5.tmf;

import me.xa5.tmf.entity.EntityAntimatter;
import me.xa5.tmf.renderer.entity.EntityRendererPrimedAntimatter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;

public class TestModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(EntityAntimatter.class, ((entityRenderDispatcher, context) -> new EntityRendererPrimedAntimatter(entityRenderDispatcher)));
    }
}