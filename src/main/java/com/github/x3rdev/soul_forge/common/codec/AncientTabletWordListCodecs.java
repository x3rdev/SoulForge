package com.github.x3rdev.soul_forge.common.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record AncientTabletWordListCodecs(long randomSeed, boolean hasData) {
    public static final Codec<AncientTabletWordListCodecs> WORD_LIST_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.LONG.fieldOf("words").forGetter(AncientTabletWordListCodecs::randomSeed),
                    Codec.BOOL.fieldOf("has_data").forGetter(AncientTabletWordListCodecs::hasData)
            ).apply(instance, AncientTabletWordListCodecs::new)
    );

    public static final StreamCodec<ByteBuf, AncientTabletWordListCodecs> WORD_LIST_STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_LONG, AncientTabletWordListCodecs::randomSeed,
                    ByteBufCodecs.BOOL, AncientTabletWordListCodecs::hasData,
                    AncientTabletWordListCodecs::new
            );
}
