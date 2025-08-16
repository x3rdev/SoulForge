package com.github.x3rdev.soul_forge.common.packet.handler;

import com.github.x3rdev.soul_forge.common.packet.SendParticlePayload;
import com.github.x3rdev.soul_forge.common.packet.SendResearchDataPayload;
import com.github.x3rdev.soul_forge.common.registry.DataAttachmentRegistry;
import com.google.common.collect.ImmutableList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

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
        });
    }
}
