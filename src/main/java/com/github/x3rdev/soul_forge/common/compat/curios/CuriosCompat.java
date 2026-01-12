package com.github.x3rdev.soul_forge.common.compat.curios;

import com.github.x3rdev.soul_forge.client.renderer.item.ResearcherGlassesRenderer;
import com.github.x3rdev.soul_forge.common.item.ResearcherGlasses;
import com.github.x3rdev.soul_forge.common.item.SoulMagnet;
import com.github.x3rdev.soul_forge.common.item.WispAmulet;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class CuriosCompat {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                CuriosCapability.ITEM,
                (stack, context) -> new ICurio() {

                    @Override
                    public ItemStack getStack() {
                        return stack;
                    }

                    @Override
                    public void curioTick(SlotContext slotContext) {
                        WispAmulet.tick(getStack(), slotContext.entity().level(), slotContext.entity());
                    }
                },
                ItemRegistry.WISP_AMULET.get()
        );
        event.registerItem(
                CuriosCapability.ITEM,
                (stack, context) -> new ICurio() {
                    @Override
                    public ItemStack getStack() {
                        return stack;
                    }
                },
                ItemRegistry.RESEARCHER_GLASSES.get()
        );
        event.registerItem(
                CuriosCapability.ITEM,
                (stack, context) -> new ICurio() {
                    @Override
                    public ItemStack getStack() {
                        return stack;
                    }

                    @Override
                    public void curioTick(SlotContext slotContext) {
                        SoulMagnet.tick(stack, slotContext.entity().level(), slotContext.entity());
                    }
                },
                ItemRegistry.SOUL_MAGNET.get()
        );
        event.registerItem(
                CuriosCapability.ITEM,
                (stack, context) -> new ICurio() {
                    @Override
                    public ItemStack getStack() {return stack;}
                },
                ItemRegistry.OCCULT_NECKLACE.get()
        );
    }

    public static boolean playerHasResearcherGlassesInCuriosSlots(Player player) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
        if(curiosInventory.isPresent()) {
            IItemHandlerModifiable equippedCurios = curiosInventory.get().getEquippedCurios();
            for (int i = 0; i < equippedCurios.getSlots(); i++) {
                ItemStack stackInSlot = equippedCurios.getStackInSlot(i);
                if(stackInSlot.getItem() instanceof ResearcherGlasses) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void registerCuriosRenderers() {
        CuriosRendererRegistry.register(ItemRegistry.RESEARCHER_GLASSES.get(), ResearcherGlassesCurioRenderer::new);
    }
}
