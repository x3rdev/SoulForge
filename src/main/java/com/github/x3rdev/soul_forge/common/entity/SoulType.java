package com.github.x3rdev.soul_forge.common.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum SoulType {
    EMPTY("empty", 0, 0xFFFFFF),
    SOUL("soul", 1, 0x98caff),
    UNDEAD_SOUL("undead_soul", 1, 0xb9ff98),
    NETHER_SOUL("nether_soul", 1, 0xf98d1b),
    ENDER_SOUL("ender_soul", 1, 0xb398ff),
    DRAGON_SOUL("dragon_soul", 20, 0x611a75);

    public static final Codec<SoulType> CODEC = Codec.STRING.xmap(
            SoulType::getSoulTypeForString,
            SoulType::toString
    );
    public static final StreamCodec<ByteBuf, SoulType> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
            .map(s -> SoulType.valueOf(s.toUpperCase()), SoulType::toString);

    private final String name;
    private final int size;
    private final int color;

    SoulType(String name, int size, int color) {
        this.name = name;
        this.size = size;
        this.color = color;
    }

    public int size() {
        return size;
    }

    public int color() {
        return color;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public String toString() {
        return name;
    }

    public static SoulType getSoulTypeForString(String name) {
        return SoulType.valueOf(name.toUpperCase());
    }
}
