package com.github.x3rdev.soul_forge.common.packet;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SendResearchDataPayload(List<ResourceKey<Research>> keys) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SendResearchDataPayload> TYPE = new SendResearchDataPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "send_research_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SendResearchDataPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY).apply(ByteBufCodecs.list()),
            SendResearchDataPayload::keys,
            SendResearchDataPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
