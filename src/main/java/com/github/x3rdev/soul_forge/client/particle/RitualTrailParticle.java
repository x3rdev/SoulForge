package com.github.x3rdev.soul_forge.client.particle;

import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class RitualTrailParticle extends TextureSheetParticle {

    protected RitualTrailParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.gravity = 0F;
        this.lifetime = 40;
        this.friction = 1F;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return 0.1F;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ShaderRegistry.RITUAL_TRAIL;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RitualTrailParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

}
