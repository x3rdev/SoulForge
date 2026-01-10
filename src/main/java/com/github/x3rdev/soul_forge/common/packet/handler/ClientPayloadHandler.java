package com.github.x3rdev.soul_forge.common.packet.handler;

import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.packet.SendDiscoveredCharsPayload;
import com.github.x3rdev.soul_forge.common.packet.SendParticlePayload;
import com.github.x3rdev.soul_forge.common.packet.SendResearchDataPayload;
import com.github.x3rdev.soul_forge.common.packet.SendWordStoneSeedPayload;
import com.github.x3rdev.soul_forge.common.registry.DataAttachmentRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientPayloadHandler {

    public static void handleSendParticle(SendParticlePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> context.player().level().addParticle(
                payload.options(),
                payload.x(),
                payload.y(),
                payload.z(),
                payload.xSpeed(),
                payload.ySpeed(),
                payload.zSpeed()
        ));
    }

    public static void handleSendResearchData(SendResearchDataPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().setData(DataAttachmentRegistry.UNLOCKED_RESEARCH, ImmutableList.copyOf(payload.keys()));
            Research.clearCache();
        });
    }

    public static void handleSendDiscoveredChars(SendDiscoveredCharsPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Map<ResourceKey<Research>, List<Character>> data = new HashMap<>(context.player().getData(DataAttachmentRegistry.RESEARCH_DISCOVERED_CHARS));
            data.put(payload.research(), payload.discoveredChars());
            context.player().setData(DataAttachmentRegistry.RESEARCH_DISCOVERED_CHARS, data);
        });
    }
}
