package com.github.x3rdev.soul_forge.client.renderer.entity;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DragonSoulRenderer extends BaseSoulRenderer {

    private static final ResourceLocation FLAME_TEXTURE = new ResourceLocation(SoulForge.MOD_ID, "textures/entity/dragon_soul_flame.png");
    private static final ResourceLocation HEAD_TEXTURE = new ResourceLocation(SoulForge.MOD_ID, "textures/entity/dragon_soul_head.png");
    private static final ResourceLocation PARTICLE_TEXTURE = new ResourceLocation(SoulForge.MOD_ID, "textures/entity/dragon_soul_particles.png");


    public DragonSoulRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(SoulForge.MOD_ID, "dragon_soul")));
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureOverrideForBone(GeoBone bone, SoulEntity animatable, float partialTick) {
        if(bone.getName().equals("flame")) {
            AnimatableTexture.setAndUpdate(FLAME_TEXTURE);
            return FLAME_TEXTURE;
        }
        if(bone.getName().equals("head"))  {
            return HEAD_TEXTURE;
        }
        if(bone.getName().equals("particle")) {
            AnimatableTexture.setAndUpdate(PARTICLE_TEXTURE);
            return PARTICLE_TEXTURE;
        }
        return super.getTextureOverrideForBone(bone, animatable, partialTick);
    }
}
