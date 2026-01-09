package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.codec.AncientTabletWordListCodecs;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import com.github.x3rdev.soul_forge.common.research.WordList;
import com.github.x3rdev.soul_forge.common.world.AncientTabletIncrementer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AncientTablet extends Item {
    List<String> words = new ArrayList<>();

    boolean hasData;
    long randomSeed;

    public AncientTablet() {
        super(new Item.Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1)
                .component(DataComponentRegistry.ANCIENT_TABLET_WORDS, new AncientTabletWordListCodecs(0, false))
        );
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        this.hasData = itemstack.get(DataComponentRegistry.ANCIENT_TABLET_WORDS).hasData();
        InteractionResultHolder<ItemStack> result = generateWords(level, itemstack);
        if (!level.isClientSide()) System.out.println(itemstack.get(DataComponentRegistry.ANCIENT_TABLET_WORDS));
        return result;
    }

    public static long getWorldSeed(Level level) {
        long seed = 0;
        MinecraftServer server = Minecraft.getInstance().level.getServer();
        IntegratedServer singlePlayerServer = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) seed = server.getWorldData().worldGenOptions().seed() % 16777216;
        else if (singlePlayerServer != null) seed = singlePlayerServer.getWorldData().worldGenOptions().seed() % 16777216;
        return seed;
    }

    public static DimensionDataStorage getDataStorage(Level level) {
        DimensionDataStorage dataStorage = null;
        MinecraftServer server = Minecraft.getInstance().level.getServer();
        IntegratedServer singlePlayerServer = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) dataStorage = server.getLevel(level.dimension()).getDataStorage();
        else if (singlePlayerServer != null) dataStorage = singlePlayerServer.getLevel(level.dimension()).getDataStorage();
        return dataStorage;
    }

    // if the tablet is meant to be readable before being used (such as from a research table), this method
    // can be called as long as the level is not null (hence why this logic isn't in the constructor)
    public InteractionResultHolder<ItemStack> generateWords(Level level, ItemStack itemstack) {
        // skip if tablet words have been loaded already
        if (hasData || level.isClientSide()) return InteractionResultHolder.sidedSuccess(itemstack, !level.isClientSide());

        DimensionDataStorage dataStorage = getDataStorage(level);
        AncientTabletIncrementer incrementer = dataStorage.computeIfAbsent(new SavedData.Factory<>(AncientTabletIncrementer::create, AncientTabletIncrementer::load), "");
        this.randomSeed = (getWorldSeed(level) + incrementer.getSequencerPosition()) % 16777216;
        this.words = generateWordListFromSeed(itemstack, (int) this.randomSeed);

        itemstack.set(DataComponentRegistry.ANCIENT_TABLET_WORDS, new AncientTabletWordListCodecs(this.randomSeed, true));
        incrementer.increment();
        return InteractionResultHolder.sidedSuccess(itemstack, !level.isClientSide());
    }

    // static stateless method for generating word lists given an itemstack and seed value
    public static List<String> generateWordListFromSeed(ItemStack itemstack, int seedValue) {
        // set word list for new tablet
        WordList wordList = itemstack.getItem().builtInRegistryHolder().getData(WordList.DATA_MAP_TYPE);
        List<String> commonWords = wordList.common();
        List<String> uncommonWords = wordList.uncommon();
        List<String> rareWords = wordList.rare();
        List<String> epicWords = wordList.epic();

        List<String> words = new ArrayList<>();
        // assign words to tablet
        Random random = new Random(seedValue);
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
