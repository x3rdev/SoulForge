package com.github.x3rdev.soul_forge.common.research;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record Research(ResourceKey<Research> parent, String title, ItemStack iconItemStack, ItemStack unlockItemStack, boolean inactive) {

    public static final ResourceKey<Research> EMPTY_RESOURCE_KEY = ResourceKey.create(DatapackRegistry.RESEARCH_KEY, ResourceLocation.withDefaultNamespace("empty"));
    public static final ResourceKey<Research> HEAD_RESOURCE_KEY = ResourceKey.create(DatapackRegistry.RESEARCH_KEY, ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "head"));

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(DatapackRegistry.RESEARCH_KEY).fieldOf("parent").orElse(EMPTY_RESOURCE_KEY).forGetter(Research::parent),
                    Codec.STRING.fieldOf("title").forGetter(Research::title),
                    ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("icon_item_stack").orElse(ItemStack.EMPTY).forGetter(Research::iconItemStack),
                    ItemStack.STRICT_CODEC.fieldOf("unlock_item_stack").orElse(ItemStack.EMPTY).forGetter(Research::unlockItemStack),
                    Codec.BOOL.fieldOf("inactive").orElse(false).forGetter(Research::inactive)
            ).apply(instance, Research::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Research> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY),
            Research::parent,
            ByteBufCodecs.STRING_UTF8,
            Research::title,
            ItemStack.STREAM_CODEC,
            Research::iconItemStack,
            ItemStack.OPTIONAL_STREAM_CODEC,
            Research::unlockItemStack,
            ByteBufCodecs.BOOL,
            Research::inactive,
            Research::new
    );

    public static Holder.Reference<Research> getEmptyResearch(RegistryAccess access) {
        return access.holder(EMPTY_RESOURCE_KEY).orElseThrow();
    }

    public static Holder.Reference<Research> getHeadResearch(RegistryAccess access) {
        return access.holder(HEAD_RESOURCE_KEY).orElseThrow();
    }


    public Holder.Reference<Research> getParent(RegistryAccess access) {
        return access.holder(parent).orElse(getEmptyResearch(access));
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj; // Evaluate reference
    }


}
