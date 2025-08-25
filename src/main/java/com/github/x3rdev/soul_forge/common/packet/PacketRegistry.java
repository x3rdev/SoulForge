package com.github.x3rdev.soul_forge.common.packet;

import com.github.x3rdev.soul_forge.common.packet.handler.ClientPayloadHandler;
import com.github.x3rdev.soul_forge.common.packet.handler.ServerPayloadHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketRegistry {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(
                UpdateResearchPayload.TYPE,
                UpdateResearchPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        (payload, context) -> {},
                        ServerPayloadHandler::handleUpdateResearch
                )
        );
        registrar.playToServer(
                SubmitResearchPayload.TYPE,
                SubmitResearchPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        (payload, context) -> {},
                        ServerPayloadHandler::handleSubmitResearch
                )
        );
        registrar.playToClient(
                SendParticlePayload.TYPE,
                SendParticlePayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleSendParticle,
                        (payload, context) -> {}
                )
        );
        registrar.playToClient(
                SendResearchDataPayload.TYPE,
                SendResearchDataPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleSendResearchData,
                        (payload, context) -> {}
                )
        );
        registrar.playToServer(
                StartSoulAnvilPayload.TYPE,
                StartSoulAnvilPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        (payload, context) -> {},
                        ServerPayloadHandler::handleStartSoulAnvil
                )
        );
    }
}
