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

public record WordList(List<String> commonWords, List<String> uncommonWords, List<String> rareWords, List<String> epicWords) {
    public static final Codec<WordList> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(Codec.STRING).fieldOf("commonWords").forGetter(WordList::commonWords),
                    Codec.list(Codec.STRING).fieldOf("uncommonWords").forGetter(WordList::uncommonWords),
                    Codec.list(Codec.STRING).fieldOf("rareWords").forGetter(WordList::rareWords),
                    Codec.list(Codec.STRING).fieldOf("epicWords").forGetter(WordList::epicWords)
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Common: [");
        stringFrom(s, commonWords);
        s.append("Uncommon: [");
        stringFrom(s, uncommonWords);
        s.append("Rare: [");
        stringFrom(s, rareWords);
        s.append("Epic: [");
        stringFrom(s, epicWords);
        return s.toString();
    }

    private void stringFrom(StringBuilder s, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            s.append(list.get(i));
            if (i == list.size() - 1) {
                s.append("]\n");
                break;
            }
            s.append(", ");
        }
    }
}
