package com.github.x3rdev.soul_forge.common.packet;

import com.github.x3rdev.soul_forge.SoulForge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SendWordStoneSeedPayload(int containerId, int seed) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SendWordStoneSeedPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "send_word_stone_seed"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SendWordStoneSeedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SendWordStoneSeedPayload::containerId,
            ByteBufCodecs.VAR_INT,
            SendWordStoneSeedPayload::seed,
            SendWordStoneSeedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
