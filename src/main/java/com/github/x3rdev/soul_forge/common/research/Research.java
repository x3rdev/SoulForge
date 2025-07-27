package com.github.x3rdev.soul_forge.common.research;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.recipe.RitualInput;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

public record Research(ResourceKey<Research> parent, String title, String description, ItemStack iconItemStack, ItemStack unlockItemStack, Optional<ResourceLocation> ritualReward, boolean inactive) {

    public static final ResourceKey<Research> EMPTY_RESOURCE_KEY = ResourceKey.create(DatapackRegistry.RESEARCH_KEY, ResourceLocation.withDefaultNamespace("empty"));
    public static final ResourceKey<Research> HEAD_RESOURCE_KEY = ResourceKey.create(DatapackRegistry.RESEARCH_KEY, ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "head"));

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(DatapackRegistry.RESEARCH_KEY).fieldOf("parent").orElse(EMPTY_RESOURCE_KEY).forGetter(Research::parent),
                    Codec.STRING.fieldOf("title").forGetter(Research::title),
                    Codec.STRING.fieldOf("description").forGetter(Research::description),
                    ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("icon_item_stack").orElse(ItemStack.EMPTY).forGetter(Research::iconItemStack),
                    ItemStack.STRICT_CODEC.fieldOf("unlock_item_stack").orElse(ItemStack.EMPTY).forGetter(Research::unlockItemStack),
                    ResourceLocation.CODEC.optionalFieldOf("ritual_reward").forGetter(Research::ritualReward),
                    Codec.BOOL.fieldOf("inactive").orElse(false).forGetter(Research::inactive)
            ).apply(instance, Research::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Research> STREAM_CODEC = StreamCodec.of(
            (buffer, value) -> {
                ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY).encode(buffer, value.parent);
                ByteBufCodecs.STRING_UTF8.encode(buffer, value.title);
                ByteBufCodecs.STRING_UTF8.encode(buffer, value.description);
                ItemStack.STREAM_CODEC.encode(buffer, value.iconItemStack);
                ItemStack.STREAM_CODEC.encode(buffer, value.unlockItemStack);
                ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs::optional).encode(buffer, value.ritualReward);
                ByteBufCodecs.BOOL.encode(buffer, value.inactive);
            },
            buffer -> {
                ResourceKey<Research> decode = ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY).decode(buffer);
                String title = ByteBufCodecs.STRING_UTF8.decode(buffer);
                String description = ByteBufCodecs.STRING_UTF8.decode(buffer);
                ItemStack iconItemStack = ItemStack.STREAM_CODEC.decode(buffer);
                ItemStack unlockItemStack = ItemStack.STREAM_CODEC.decode(buffer);
                Optional<ResourceLocation> ritualReward = ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs::optional).decode(buffer);
                boolean inactive = ByteBufCodecs.BOOL.decode(buffer);
                return new Research(decode, title, description, iconItemStack, unlockItemStack, ritualReward, inactive);
            }
    );


    public static Holder.Reference<Research> getEmptyResearch(RegistryAccess access) {
        return access.holder(EMPTY_RESOURCE_KEY).orElseThrow();
    }

    public static Holder.Reference<Research> getHeadResearch(RegistryAccess access) {
        return access.holder(HEAD_RESOURCE_KEY).orElseThrow();
    }


    public Holder.Reference<Research> getParent(RegistryAccess access) {
        if(parent == null) {
            return getEmptyResearch(access);
        }
        return access.holder(parent).orElseThrow();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj; // Evaluate reference
    }


}
