package com.github.x3rdev.soul_forge.common.packet.handler;

import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.menu.SoulAnvilMenu;
import com.github.x3rdev.soul_forge.common.packet.PickWordPayload;
import com.github.x3rdev.soul_forge.common.packet.StartSoulAnvilPayload;
import com.github.x3rdev.soul_forge.common.packet.UpdateUnlockedResearchPayload;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void handleStartSoulAnvil(StartSoulAnvilPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            AbstractContainerMenu containerMenu = context.player().containerMenu;
            if(containerMenu.containerId == payload.containerId() && containerMenu instanceof SoulAnvilMenu soulAnvilMenu) {
                soulAnvilMenu.startAnvil();
            }
        });
    }

    public static void handleUpdateResearch(UpdateUnlockedResearchPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            AbstractContainerMenu containerMenu = context.player().containerMenu;
            RegistryAccess registryAccess = context.player().level().registryAccess();
            if(containerMenu.containerId == payload.containerId() && containerMenu instanceof ResearchTableMenu researchTableMenu) {
                if(!Research.playerHasResearchUnlocked(context.player(), registryAccess.holder(payload.research()).orElseThrow())) {
                    DisconnectionDetails details =
                            new DisconnectionDetails(Component.translatable("network.soul_forge.update_research_failed"));
                    context.connection().disconnect(details);
                    return;
                }
                researchTableMenu.setActiveResearch(registryAccess.holder(payload.research()).orElseThrow());
            }
        });
    }

    public static void handlePickWord(PickWordPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            AbstractContainerMenu containerMenu = context.player().containerMenu;
            if(containerMenu.containerId == payload.containerId() && containerMenu instanceof ResearchTableMenu researchTableMenu) {
                researchTableMenu.pickWord(payload.index());
            }
        });
    }


}
