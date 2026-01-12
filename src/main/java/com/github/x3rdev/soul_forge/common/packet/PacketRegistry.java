package com.github.x3rdev.soul_forge.common.packet;

import com.github.x3rdev.soul_forge.common.packet.handler.ClientPayloadHandler;
import com.github.x3rdev.soul_forge.common.packet.handler.ServerPayloadHandler;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PacketRegistry {

    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(
                PickWordPayload.TYPE,
                PickWordPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        (payload, context) -> {},
                        ServerPayloadHandler::handlePickWord
                )
        );
        registrar.playToClient(
                SendDiscoveredCharsPayload.TYPE,
                SendDiscoveredCharsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleSendDiscoveredChars,
                        (payload, context) -> {}
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
        registrar.playToServer(
                UpdateUnlockedResearchPayload.TYPE,
                UpdateUnlockedResearchPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        (payload, context) -> {},
                        ServerPayloadHandler::handleUpdateResearch
                )
        );
    }
}
