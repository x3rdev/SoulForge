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

import java.util.List;

public record SendDiscoveredCharsPayload(ResourceKey<Research> research, List<Character> discoveredChars) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SendDiscoveredCharsPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "send_discovered_chars"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SendDiscoveredCharsPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY),
            SendDiscoveredCharsPayload::research,
            ByteBufCodecs.stringUtf8(1).map(string -> string.charAt(0), String::valueOf).apply(ByteBufCodecs.list()),
            SendDiscoveredCharsPayload::discoveredChars,
            SendDiscoveredCharsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
