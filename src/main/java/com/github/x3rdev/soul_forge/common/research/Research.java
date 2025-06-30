package com.github.x3rdev.soul_forge.common.research;

import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record Research(Optional<ResourceKey<Research>> parent, String title, ItemStack iconStack) {

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(DatapackRegistry.RESEARCH_KEY).optionalFieldOf("parent").forGetter(Research::parent),
                    Codec.STRING.fieldOf("title").forGetter(Research::title),
                    ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("icon_item_stack").forGetter(Research::iconStack)
    ).apply(instance, Research::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Research> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY)),
            Research::parent,
            ByteBufCodecs.STRING_UTF8,
            Research::title,
            ItemStack.STREAM_CODEC,
            Research::iconStack,
            Research::new
    );

    public Optional<Research> getParent(RegistryAccess access) {
        return parent.map(researchResourceKey -> access.lookup(DatapackRegistry.RESEARCH_KEY).orElseThrow().get(researchResourceKey).orElseThrow().value());
    }
}
