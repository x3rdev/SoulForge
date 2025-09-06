package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
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
        addBlock(BlockRegistry.SOUL_CAULDRON, "Soul Storage");
        addBlock(BlockRegistry.RESEARCH_TABLE, "Research Table");
        addBlock(BlockRegistry.SOUL_ANVIL, "Soul Anvil");


        // Item
        addItem(ItemRegistry.SOUL_SCYTHE, "Soul Scythe");
        addItem(ItemRegistry.SMALL_SOUL_BOTTLE, "Small Soul Bottle");
        addItem(ItemRegistry.LARGE_SOUL_BOTTLE, "Large Soul Bottle");
        addItem(ItemRegistry.SOUL_STEEL_HELMET, "Soul Steel Helmet");
        addItem(ItemRegistry.SOUL_STEEL_CHESTPLATE, "Soul Steel Chestplate");
        addItem(ItemRegistry.SOUL_STEEL_LEGGINGS, "Soul Steel Leggings");
        addItem(ItemRegistry.SOUL_STEEL_BOOTS, "Soul Steel Boots");
        addItem(ItemRegistry.AWAKENED_SOUL_STEEL_HELMET, "Awakened Soul Steel Helmet");
        addItem(ItemRegistry.AWAKENED_SOUL_STEEL_CHESTPLATE, "Awakened Soul Steel Chestplate");
        addItem(ItemRegistry.AWAKENED_SOUL_STEEL_LEGGINGS, "Awakened Soul Steel Leggings");
        addItem(ItemRegistry.AWAKENED_SOUL_STEEL_BOOTS, "Awakened Soul Steel Boots");
        addItem(ItemRegistry.WISP_AMULET, "Wisp Amulet");
        addItem(ItemRegistry.GHOST_SPAWN_EGG, "Ghost Spawn Egg");
        addItem(ItemRegistry.NERGAL_SPAWN_EGG, "Nergal Spawn Egg");
        addItem(ItemRegistry.SOUL_CRYSTAL, "Soul Crystal");
        addItem(ItemRegistry.EMPOWERED_SOUL_CRYSTAL, "Empowered Soul Crystal");
        addItem(ItemRegistry.ECTOPLASM, "Ectoplasm");
        addItem(ItemRegistry.MURKY_LENS, "Murky Lens");
        addItem(ItemRegistry.RESEARCHER_GLASSES, "Researcher Glasses");
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
        addItem(ItemRegistry.SOUL_STEEL_SWORD, "Soulsteel Sword");
        addItem(ItemRegistry.AWAKENED_SOUL_STEEL_SWORD, "Awakened Soulsteel Sword");
        addItem(ItemRegistry.SOUL_STEEL_SHIELD, "Soulsteel Shield");
        addItem(ItemRegistry.AWAKENED_SOUL_STEEL_SHIELD, "Awakened Soulsteel Shield");

        // Item Group
        add("itemGroup.soul_forge", "Soul Forge");

        // Entity
        addEntityType(EntityRegistry.SOUL, "Soul");
        addEntityType(EntityRegistry.UNDEAD_SOUL, "Undead Soul");
        addEntityType(EntityRegistry.NETHER_SOUL, "Nether Soul");
        addEntityType(EntityRegistry.ENDER_SOUL, "Ender Soul");
        addEntityType(EntityRegistry.DRAGON_SOUL, "Dragon Soul");
        addEntityType(EntityRegistry.SOUL_SCYTHE_PROJECTILE, "Soul Scythe");
        addEntityType(EntityRegistry.WISP, "Wisp");
        addEntityType(EntityRegistry.GHOST, "Ghost");
        addEntityType(EntityRegistry.NERGAL, "Nergal");
        addEntityType(EntityRegistry.NERGAL_SPAWN, "Nergal Spawn");

        // Tooltip
        add("item.soul_forge.soul_bottle.tooltip", "Contains %s soul(s)");
        add("item.soul_forge.soul_bottle.tooltip.empty", "Contains nothing");

        //Container
        add("container.soul_forge.soul_anvil", "Soul Anvil");
    }
}
