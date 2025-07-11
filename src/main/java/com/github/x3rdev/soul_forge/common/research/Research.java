package com.github.x3rdev.soul_forge.common.research;

import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record Research(ResourceKey<Research> parent, String title, ItemStack iconItemStack, ItemStack unlockItemStack, boolean fake) {

    public static final ResourceKey<Research> EMPTY = ResourceKey.create(DatapackRegistry.RESEARCH_KEY, ResourceLocation.withDefaultNamespace("empty"));

    private static Research emptyResearch;

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(DatapackRegistry.RESEARCH_KEY).fieldOf("parent").orElse(EMPTY).forGetter(Research::parent),
                    Codec.STRING.fieldOf("title").forGetter(Research::title),
                    ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("icon_item_stack").forGetter(Research::iconItemStack),
                    ItemStack.OPTIONAL_CODEC.fieldOf("unlock_item_stack").orElse(ItemStack.EMPTY).forGetter(Research::unlockItemStack),
                    Codec.BOOL.fieldOf("fake").orElse(false).forGetter(Research::fake)
    ).apply(instance, Research::new));

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
            Research::fake,
            Research::new
    );

    public static Research getEmptyResearch() {
        if(emptyResearch == null) {
            emptyResearch = new Research(EMPTY, "empty", Items.AIR.getDefaultInstance(), ItemStack.EMPTY, true);
        }
        return emptyResearch;
    }

    public Research getParent(RegistryAccess access) {
        if(parent.equals(EMPTY)) {
            return getEmptyResearch();
        }
        return access.lookup(DatapackRegistry.RESEARCH_KEY).orElseThrow().get(parent).orElseThrow().value();
    }
}
