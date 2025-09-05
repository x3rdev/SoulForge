package com.github.x3rdev.soul_forge.mixin;

import com.github.x3rdev.soul_forge.client.ClientSetup;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PostPass.class)
public abstract class PostPassMixin {

    @Shadow @Final private EffectInstance effect;

    @Inject(method = {"process", "method_1293"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getInstance()Lnet/minecraft/client/Minecraft;"))
    public void process(float partialTicks, CallbackInfo ci) {
        if(this.effect.getName().equals("soul_forge:researcher_glasses") && ClientSetup.getOrCreateOverlayTarget() != null) {
            this.effect.setSampler("EntityMaskSampler", ClientSetup.getOrCreateOverlayTarget()::getColorTextureId);
        }
    }



}
