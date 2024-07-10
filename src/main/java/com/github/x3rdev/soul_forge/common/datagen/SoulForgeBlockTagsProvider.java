package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SoulForgeBlockTagsProvider extends BlockTagsProvider {
    public SoulForgeBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SoulForge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.STONE_BRICKS).add(BlockRegistry.SOUL_BRICKS.get());
        tag(BlockTags.STAIRS).add(BlockRegistry.SOUL_BRICK_STAIRS.get());
        tag(BlockTags.SLABS).add(BlockRegistry.SOUL_BRICK_SLAB.get());
        tag(BlockTags.WALLS).add(BlockRegistry.SOUL_BRICK_WALL.get());
    }
}
