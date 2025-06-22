package com.github.x3rdev.soul_forge.common.research;

import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record Research(Optional<ResourceKey<Research>> parent, ItemStack icon) {

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(DatapackRegistry.RESEARCH_KEY).optionalFieldOf("parent").forGetter(Research::parent),
                    ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("icon").forGetter(Research::icon)
    ).apply(instance, Research::new));

    public Optional<Research> getParent(RegistryAccess access) {
        return parent.map(researchResourceKey -> access.lookup(DatapackRegistry.RESEARCH_KEY).orElseThrow().get(researchResourceKey).orElseThrow().value());
    }
}
