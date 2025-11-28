package com.github.x3rdev.soul_forge.mixin;

import com.github.x3rdev.soul_forge.common.compat.PatchouliCompat;
import com.github.x3rdev.soul_forge.common.item.Necronomicon;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoItem;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;clearGuiLayers(Lnet/minecraft/client/Minecraft;)V"))
    public void setScreen(Screen guiScreen, CallbackInfo ci) {
        if(Minecraft.getInstance().player == null) {
            return;
        }
        if(PatchouliCompat.PatchouliIsPresent() && ItemRegistry.NECRONOMICON.getId().equals(PatchouliCompat.getAPI().getOpenBookGui()) && guiScreen == null) {
            for (InteractionHand value : InteractionHand.values()) {
                ItemStack stack = Minecraft.getInstance().player.getItemInHand(value);
                if(stack.is(ItemRegistry.NECRONOMICON.get())) {
                    stack.set(DataComponentRegistry.NECRONOMICON_OPEN, false);
                    ((Necronomicon) stack.getItem()).triggerAnim(Minecraft.getInstance().player, GeoItem.getId(stack), "c", "closing");
                }
            }
        }
    }
}
