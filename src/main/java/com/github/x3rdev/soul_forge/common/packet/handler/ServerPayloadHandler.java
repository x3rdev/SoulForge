package com.github.x3rdev.soul_forge.common.packet.handler;

import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.packet.SubmitResearchPayload;
import com.github.x3rdev.soul_forge.common.packet.UpdateResearchPayload;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void handleUpdateResearch(UpdateResearchPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            AbstractContainerMenu containerMenu = context.player().containerMenu;
            RegistryAccess registryAccess = context.player().level().registryAccess();
            if(containerMenu.containerId == payload.containerId() && containerMenu instanceof ResearchTableMenu researchTableMenu) {
                researchTableMenu.setActiveResearch(registryAccess.holder(payload.research()).orElseThrow());
            }
        });
    }

    public static void handleSubmitResearch(SubmitResearchPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            AbstractContainerMenu containerMenu = context.player().containerMenu;
            if(containerMenu.containerId == payload.containerId() && containerMenu instanceof ResearchTableMenu researchTableMenu) {
                researchTableMenu.submitResearch();
            }
        });
    }
}
