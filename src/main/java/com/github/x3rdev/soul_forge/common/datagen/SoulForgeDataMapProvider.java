package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.research.WordList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SoulForgeDataMapProvider extends DataMapProvider {
    protected SoulForgeDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
//        Currently this is hardcoded because it was easier for me

//        List<String> common = new ArrayList<>();
//        List<String> uncommon = new ArrayList<>();
//        List<String> rare = new ArrayList<>();
//        List<String> epic = new ArrayList<>();
//        // placeholders
//        common.add("narg");
//        common.add("it's morbin' time");
//        uncommon.add("yee");
//        rare.add("doohickey");
//        epic.add("eBic");
//
//        this.builder(WordList.DATA_MAP_TYPE)
//                .add(ItemRegistry.ANCIENT_TABLET, new WordList(common, uncommon, rare, epic), false);
    }
}
