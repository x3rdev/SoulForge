package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.research.WordList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AncientTablet extends Item {
    List<String> commonWords;
    List<String> uncommonWords;
    List<String> rareWords;
    List<String> epicWords;

    long seed;
    Random random;

    public AncientTablet(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        WordList wordList = this.builtInRegistryHolder().getData(WordList.DATA_MAP_TYPE);
        if (wordList != null) {
            commonWords = wordList.common();
            uncommonWords = wordList.uncommon();
            rareWords = wordList.rare();
            epicWords = wordList.epic();
        }
        else {
            List<String> nullList = new ArrayList<>();
            commonWords = nullList;
            uncommonWords = nullList;
            rareWords = nullList;
            epicWords = nullList;
            SoulForge.LOGGER.error("Word list not loaded");
        }

        MinecraftServer server = Minecraft.getInstance().level.getServer();
        IntegratedServer singlePlayerServer = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) {
            seed = server.getWorldData().worldGenOptions().seed();
        }
        else if (singlePlayerServer != null) {
            seed = singlePlayerServer.getWorldData().worldGenOptions().seed();
        }
        else {
            seed = 0;
            SoulForge.LOGGER.error("Seed not loaded");
        }

        random = new Random(seed);

        return InteractionResultHolder.sidedSuccess(itemstack, !level.isClientSide());
    }


}
