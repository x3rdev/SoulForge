package com.github.x3rdev.soul_forge.common.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public record AncientTabletWordListCodecs(List<String> words, boolean hasData) {
    public static final Codec<AncientTabletWordListCodecs> WORD_LIST_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(Codec.STRING).fieldOf("words").forGetter(AncientTabletWordListCodecs::words),
                    Codec.BOOL.fieldOf("has_data").forGetter(AncientTabletWordListCodecs::hasData)
            ).apply(instance, AncientTabletWordListCodecs::new)
    );

    public static final StreamCodec<ByteBuf, AncientTabletWordListCodecs> WORD_LIST_STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.collection(
                            ArrayList::new,
                            ByteBufCodecs.STRING_UTF8,
                            64
                    ), AncientTabletWordListCodecs::words,
                    ByteBufCodecs.BOOL, AncientTabletWordListCodecs::hasData,
                    AncientTabletWordListCodecs::new
            );

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Has data: ");
        s.append(hasData);
        s.append("\nWords: \n");
        for (int  i = 0; i < words.size(); i++) {
            s.append(words.get(i));
            if (i != words.size() - 1) {
                s.append(", ");
            }
        }
        return s.toString();
    }
}
