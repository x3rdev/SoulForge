package com.github.x3rdev.soul_forge.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.Nullable;

public class SoulParticle extends TextureSheetParticle {

    protected SoulParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, x, xSpeed, ySpeed, zSpeed);
        this.gravity = 0;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class MyParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public MyParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientlevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SoulParticle(clientlevel, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
