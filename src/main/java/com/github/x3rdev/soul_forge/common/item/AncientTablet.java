package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import com.github.x3rdev.soul_forge.common.research.WordList;
import com.github.x3rdev.soul_forge.common.world.AncientTabletCounter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AncientTablet extends Item {

    public AncientTablet() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1)
                .component(DataComponentRegistry.ANCIENT_TABLET_SEED, -1)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int ancient_tablet_seed = stack.get(DataComponentRegistry.ANCIENT_TABLET_SEED);
        tooltipComponents.add(Component.translatable("item.soul_forge.ancient_tablet.tooltip",
                ancient_tablet_seed == -1 ? "???" : ancient_tablet_seed));
    }

    // if the tablet is meant to be readable before being used (such as from a research table), this method
    // can be called as long as the level is not null (hence why this logic isn't in the constructor)
    public void generateTabletSeed(ServerLevel level, ItemStack itemstack) {
        if(itemstack.get(DataComponentRegistry.ANCIENT_TABLET_SEED) == -1) {
            DimensionDataStorage dataStorage = level.getServer().overworld().getDataStorage();
            AncientTabletCounter incrementer = dataStorage.computeIfAbsent(
                    new SavedData.Factory<>(
                            AncientTabletCounter::create,
                            AncientTabletCounter::load),
                    "ancient_tablet_incrementer");

            itemstack.set(DataComponentRegistry.ANCIENT_TABLET_SEED, incrementer.getCount());

            incrementer.increment();
        }
    }

    public List<String> getTabletWordList(ItemStack itemstack, int seed) {
        // set word list for new tablet
        WordList wordList = itemstack.getItem().builtInRegistryHolder().getData(WordList.DATA_MAP_TYPE);
        List<String> commonWords = wordList.common();
        List<String> uncommonWords = wordList.uncommon();
        List<String> rareWords = wordList.rare();
        List<String> epicWords = wordList.epic();

        List<String> words = new ArrayList<>();
        // assign words to tablet
        Random random = new Random(seed);
        // shifted binomial distribution (produces 3 to 8 words)
        // words : 0  1  2  3  4  5  6  7  8  9 10
        // chance: 0  0  0  1  5 10 10  5  1  0  0 (out of 32)
        int tries = cumulative(5, random.nextInt() % 32) + 3;
        for (int i = 0; i < tries; i++) {
            float roll = random.nextFloat();
            if(roll < 1/40F) {
                words.add(epicWords.get(random.nextInt(epicWords.size())));
            } else if(roll < 4/40F) {
                words.add(rareWords.get(random.nextInt(rareWords.size())));
            } else if(roll < 13/40F) {
                words.add(uncommonWords.get(random.nextInt(uncommonWords.size())));
            } else {
                words.add(commonWords.get(random.nextInt(commonWords.size())));
            }
        }

        return words;
    }

    private static int cumulative(int n, int i) {
        int sum = 0;
        for (int k = 0; k <= i; k++) {
            sum += combination(n, k);
            if (i <= sum) return k;
        }
        return n;
    }

    private static int combination(int n, int i) {
        int nCk = 1;
        for (int k = 0; k < i; k++) {
            nCk = nCk * (n - k) / (k + 1);
        }
        return nCk;
    }

}
