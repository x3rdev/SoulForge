package com.github.x3rdev.soul_forge.common.packet.handler;

import com.github.x3rdev.soul_forge.common.packet.SendParticlePayload;
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
}
