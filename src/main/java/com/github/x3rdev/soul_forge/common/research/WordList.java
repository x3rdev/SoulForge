package com.github.x3rdev.soul_forge.common.research;

import com.github.x3rdev.soul_forge.SoulForge;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.List;

public record WordList(List<String> common, List<String> uncommon, List<String> rare, List<String> epic) {
    public static final Codec<WordList> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(Codec.STRING).fieldOf("common").forGetter(WordList::common),
                    Codec.list(Codec.STRING).fieldOf("uncommon").forGetter(WordList::uncommon),
                    Codec.list(Codec.STRING).fieldOf("rare").forGetter(WordList::rare),
                    Codec.list(Codec.STRING).fieldOf("epic").forGetter(WordList::epic)
            ).apply(instance, WordList::new)
    );

    public static final DataMapType<Item, WordList> DATA_MAP_TYPE =
            AdvancedDataMapType.builder(
                    ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "word_list_map"),
                    Registries.ITEM,
                    CODEC
            ).synced(CODEC, true).build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(WordList.DATA_MAP_TYPE);
    }
}
