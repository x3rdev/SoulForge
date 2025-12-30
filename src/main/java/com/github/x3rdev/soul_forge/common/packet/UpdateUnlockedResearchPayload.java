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

public record UpdateUnlockedResearchPayload(ResourceKey<Research> research, int containerId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateUnlockedResearchPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "update_research"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateUnlockedResearchPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY),
            UpdateUnlockedResearchPayload::research,
            ByteBufCodecs.VAR_INT,
            UpdateUnlockedResearchPayload::containerId,
            UpdateUnlockedResearchPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
