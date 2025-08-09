package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class SoulForgeLanguageProvider extends LanguageProvider {

    public SoulForgeLanguageProvider(PackOutput output) {
        super(output, SoulForge.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Block
        addBlock(BlockRegistry.SOUL_BRICKS, "Soul Bricks");
        addBlock(BlockRegistry.CRACKED_SOUL_BRICKS, "Cracked Soul Blocks");
        addBlock(BlockRegistry.SOUL_BRICK_STAIRS, "Soul Brick Stairs");
        addBlock(BlockRegistry.SOUL_BRICK_SLAB, "Soul Brick Slab");
        addBlock(BlockRegistry.SOUL_BRICK_WALL, "Soul Brick Wall");
        addBlock(BlockRegistry.CHISELED_SOUL_BRICKS, "Chiseled Soul Bricks");
        addBlock(BlockRegistry.SOUL_BRICK_COLUMN, "Soul Brick Column");
        addBlock(BlockRegistry.SOUL_BRICK_DOOR, "Soul Brick Door");
        addBlock(BlockRegistry.SOUL_STEEL_BARS, "Soul Steel Bars");
        addBlock(BlockRegistry.SOUL_CANDLESTICK, "Soul Candlestick");
        addBlock(BlockRegistry.SOUL_CANDELABRA, "Soul Candelabra");
        addBlock(BlockRegistry.DARK_TOMB, "Dark Tomb");
        addBlock(BlockRegistry.STATUE, "Statue");
        addBlock(BlockRegistry.SOUL_CHANDELIER, "Soul Chandelier");
        addBlock(BlockRegistry.SOUL_CRYSTAL_ORE, "Soul Crystal Ore");
        addBlock(BlockRegistry.SOUL_CRYSTAL_BLOCK, "Soul Crystal Block");
        addBlock(BlockRegistry.SOULWOOD_LEAVES, "Soulwood Leaves");
        addBlock(BlockRegistry.SOULWOOD_SAPLING, "Soulwood Sapling");
        addBlock(BlockRegistry.SOULWOOD_LIANA, "Soulwood Liana");
        addBlock(BlockRegistry.SOULWOOD_LOG, "Soulwood Log");
        addBlock(BlockRegistry.SOULWOOD_PLANKS, "Soulwood Planks");
        addBlock(BlockRegistry.PEDESTAL, "Pedestal");
        addBlock(BlockRegistry.SOUL_STORAGE, "Soul Storage");
        addBlock(BlockRegistry.RESEARCH_TABLE, "Research Table");


        // Item
        addItem(ItemRegistry.SOUL_SCYTHE, "Soul Scythe");
        addItem(ItemRegistry.SMALL_SOUL_BOTTLE, "Small Soul Bottle");
        addItem(ItemRegistry.LARGE_SOUL_BOTTLE, "Large Soul Bottle");
        addItem(ItemRegistry.SOUL_STEEL_HELMET, "Soul Steel Helmet");
        addItem(ItemRegistry.SOUL_STEEL_CHESTPLATE, "Soul Steel Chestplate");
        addItem(ItemRegistry.SOUL_STEEL_LEGGINGS, "Soul Steel Leggings");
        addItem(ItemRegistry.SOUL_STEEL_BOOTS, "Soul Steel Boots");
        addItem(ItemRegistry.WISP_AMULET, "Wisp Amulet");
        addItem(ItemRegistry.GHOST_SPAWN_EGG, "Ghost Spawn Egg");
        addItem(ItemRegistry.NERGAL_SPAWN_EGG, "Nergal Spawn Egg");
        addItem(ItemRegistry.SOUL_CRYSTAL, "Soul Crystal");
        addItem(ItemRegistry.EMPOWERED_SOUL_CRYSTAL, "Empowered Soul Crystal");
        addItem(ItemRegistry.ECTOPLASM, "Ectoplasm");
        addItem(ItemRegistry.MURKY_LENS, "Murky Lens");
        addItem(ItemRegistry.RESEARCHER_GLASSES, "Researcher Glasses");
        addItem(ItemRegistry.CURSED_SWORD, "Cursed Sword");
        addItem(ItemRegistry.NECRONOMICON, "Necronomicon");
        addItem(ItemRegistry.SCROLL_OF_SHADOWS, "Scroll Of Shadows");
        addItem(ItemRegistry.SOUL_STEEL_INGOT, "Soul Steel Ingot");
        addItem(ItemRegistry.WOODEN_SCYTHE, "Wooden Scythe");
        addItem(ItemRegistry.STONE_SCYTHE, "Stone Scythe");
        addItem(ItemRegistry.IRON_SCYTHE, "Iron Scythe");
        addItem(ItemRegistry.GOLDEN_SCYTHE, "Golden Scythe");
        addItem(ItemRegistry.DIAMOND_SCYTHE, "Diamond Scythe");
        addItem(ItemRegistry.NETHERITE_SCYTHE, "Netherite Scythe");
        addItem(ItemRegistry.SOULWOOD_STICK, "Soulwood Stick");

        // Item Group
        add("itemGroup.soul_forge", "Soul Forge");

        // Tooltip
        add("item.soul_forge.soul_bottle.tooltip", "Contains %s soul(s)");
        add("item.soul_forge.soul_bottle.tooltip.empty", "Contains nothing");
    }
}
