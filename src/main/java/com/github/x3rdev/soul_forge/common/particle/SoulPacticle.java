package com.github.x3rdev.soul_forge.common.particle;

import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SoulPacticle extends TextureSheetParticle {
        private final SpriteSet spriteSet;
        protected SoulPacticle (ClientLevel level, double x,double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
            super(level, x, y, x, xSpeed, ySpeed, zSpeed);
            this.spriteSet = spriteSet;
            this.gravity = 0;
            this.setSpriteFromAge(spriteSet);

        }
@Override
        public ParticleRenderType getRenderType(){
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
}
public static class Provider implements ParticleProvider<SimpleParticleType>{
            private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new SoulPacticle(level, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
    }
}
}
