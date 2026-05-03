package com.github.x3rdev.soul_forge.common.packet;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public record UpdateActiveResearchPayload(ResourceKey<Research> research, int containerId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateActiveResearchPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "update_active_research"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateActiveResearchPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY),
            UpdateActiveResearchPayload::research,
            ByteBufCodecs.VAR_INT,
            UpdateActiveResearchPayload::containerId,
            UpdateActiveResearchPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
