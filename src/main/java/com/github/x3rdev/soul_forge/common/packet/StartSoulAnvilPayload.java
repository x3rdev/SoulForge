package com.github.x3rdev.soul_forge.common.packet;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public record StartSoulAnvilPayload(int containerId) implements CustomPacketPayload {


    public static final CustomPacketPayload.Type<StartSoulAnvilPayload> TYPE = new StartSoulAnvilPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "start_soul_anvil"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StartSoulAnvilPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            StartSoulAnvilPayload::containerId,
            StartSoulAnvilPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
