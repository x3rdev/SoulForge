package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, SoulForge.MOD_ID);

    public static final DeferredHolder<Block, Block> SOUL_BRICKS = BLOCKS.register("soul_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredHolder<Block, StairBlock> SOUL_BRICK_STAIRS = BLOCKS.register("soul_brick_stairs",
            () -> new StairBlock(SOUL_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS)));
    public static final DeferredHolder<Block, Block> CRACKED_SOUL_BRICKS = BLOCKS.register("cracked_soul_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_STONE_BRICKS)));
    public static final DeferredHolder<Block, SlabBlock> SOUL_BRICK_SLAB = BLOCKS.register("soul_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_SLAB)));
    public static final DeferredHolder<Block, WallBlock> SOUL_BRICK_WALL = BLOCKS.register("soul_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_WALL)));
    public static final DeferredHolder<Block, Block> CHISELED_SOUL_BRICKS = BLOCKS.register("chiseled_soul_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_STONE_BRICKS)));
    public static final DeferredHolder<Block, RotatedPillarBlock> SOUL_BRICK_COLUMN = BLOCKS.register("soul_brick_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredHolder<Block, DoorBlock> SOUL_BRICK_DOOR = BLOCKS.register("soul_brick_door",
            () -> new DoorBlock(BlockSetType.POLISHED_BLACKSTONE, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)));
    public static final DeferredHolder<Block, IronBarsBlock> SOUL_STEEL_BARS = BLOCKS.register("soul_steel_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));
    public static final DeferredHolder<Block, SoulCandleBlock> SOUL_CANDLESTICK = BLOCKS.register("soul_candlestick",
            () -> new SoulCandleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));
    public static final DeferredHolder<Block, SoulCandelabraBlock> SOUL_CANDELABRA = BLOCKS.register("soul_candelabra",
            () -> new SoulCandelabraBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));
    public static final DeferredHolder<Block, ObeliskBlock> OBELISK = BLOCKS.register("obelisk",
            () -> new ObeliskBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)));
    public static final DeferredHolder<Block, DarkTombBlock> DARK_TOMB = BLOCKS.register("dark_tomb",
            () -> new DarkTombBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)));
    public static final DeferredHolder<Block, StatueBlock> STATUE = BLOCKS.register("statue",
            () -> new StatueBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredHolder<Block, SoulChandelierBlock> SOUL_CHANDELIER = BLOCKS.register("soul_chandelier",
            () -> new SoulChandelierBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));
    public static final DeferredHolder<Block, SoulCrystalOre> SOUL_CRYSTAL_ORE = BLOCKS.register("soul_crystal_ore",
            () -> new SoulCrystalOre(BlockBehaviour.Properties.of()));
    public static final DeferredHolder<Block, SoulGemBlock> SOUL_GEM_BLOCK = BLOCKS.register("soul_gem_block",
            () -> new SoulGemBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));
    public static final DeferredHolder<Block, LeavesBlock> SOULWOOD_LEAVES = BLOCKS.register("soulwood_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final DeferredHolder<Block, SoulwoodLianaBlock> SOULWOOD_LIANA = BLOCKS.register("soulwood_liana",
            () -> new SoulwoodLianaBlock(BlockBehaviour.Properties.of().randomTicks().noCollission().instabreak().sound(SoundType.WEEPING_VINES).pushReaction(PushReaction.DESTROY)));
    public static final DeferredHolder<Block, SoulwoodLianaBodyBlock> SOULWOOD_LIANA_BODY = BLOCKS.register("soulwood_liana_body",
            () -> new SoulwoodLianaBodyBlock(BlockBehaviour.Properties.of().randomTicks().noCollission().instabreak().sound(SoundType.WEEPING_VINES).pushReaction(PushReaction.DESTROY)));
    public static final DeferredHolder<Block, Block> SOULWOOD_LOG = BLOCKS.register("soulwood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, Block> SOULWOOD_PLANKS = BLOCKS.register("soulwood_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
}