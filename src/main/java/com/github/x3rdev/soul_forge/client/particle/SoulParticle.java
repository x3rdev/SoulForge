package com.github.x3rdev.soul_forge.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.Nullable;

public class SoulParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected SoulParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime = 30;
        this.sprites = sprites;
        this.setSpriteFromAge(this.sprites);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SoulParticle(clientLevel, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
