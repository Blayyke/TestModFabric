package me.xa5.tmf.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import me.xa5.tmf.TMFBlocks;
import me.xa5.tmf.entity.EntityAntimatter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class EntityRendererPrimedAntimatter extends EntityRenderer<EntityAntimatter> {
    public EntityRendererPrimedAntimatter(EntityRenderDispatcher entityRenderDispatcher_1) {
        super(entityRenderDispatcher_1);
        this.field_4673 = 0.5F;
    }

    @Override
    public void render(EntityAntimatter primedEntity, double double_1, double double_2, double double_3, float float_1, float float_2) {
        BlockRenderManager blockRenderManager_1 = MinecraftClient.getInstance().getBlockRenderManager();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) double_1, (float) double_2 + 0.5F, (float) double_3);
        float float_5;
        if ((float) primedEntity.getFuseTimer() - float_2 + 1.0F < 10.0F) {
            float_5 = 1.0F - ((float) primedEntity.getFuseTimer() - float_2 + 1.0F) / 10.0F;
            float_5 = MathHelper.clamp(float_5, 0.0F, 1.0F);
            float_5 *= float_5;
            float_5 *= float_5;
            float float_4 = 1.0F + float_5 * 0.3F;
            GlStateManager.scalef(float_4, float_4, float_4);
        }

        float_5 = (1.0F - ((float) primedEntity.getFuseTimer() - float_2 + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(primedEntity);
        GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
        blockRenderManager_1.renderDynamic(TMFBlocks.getInstance().blockAntimatter.getDefaultState(), primedEntity.method_5718());
        GlStateManager.translatef(0.0F, 0.0F, 1.0F);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getOutlineColor(primedEntity));
            blockRenderManager_1.renderDynamic(TMFBlocks.getInstance().blockAntimatter.getDefaultState(), 1.0F);
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        } else if (primedEntity.getFuseTimer() / 5 % 2 == 0) {
            GlStateManager.disableTexture();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.DST_ALPHA);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, float_5);
            GlStateManager.polygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            blockRenderManager_1.renderDynamic(TMFBlocks.getInstance().blockAntimatter.getDefaultState(), 1.0F);
            GlStateManager.polygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture();
        }

        GlStateManager.popMatrix();
        super.render(primedEntity, double_1, double_2, double_3, float_1, float_2);
    }

    @Override
    protected Identifier getTexture(EntityAntimatter primedTNTEntity_1) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEX;
    }
}