package com.github.x3rdev.soul_forge.common.packet;

import com.github.x3rdev.soul_forge.SoulForge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SubmitResearchPayload(int containerId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SubmitResearchPayload> TYPE = new SubmitResearchPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "submit_research"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SubmitResearchPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SubmitResearchPayload::containerId,
            SubmitResearchPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
