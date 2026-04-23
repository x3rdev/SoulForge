package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class SoulForgeLootTableProvider extends LootTableProvider {

    public SoulForgeLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(SoulForgeBlockSubProvider::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(SoulForgeEntitySubProvider::new, LootContextParamSets.ENTITY)
        ), registries);
    }

    private static class SoulForgeBlockSubProvider extends BlockLootSubProvider {

        protected SoulForgeBlockSubProvider(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BlockRegistry.BLOCKS.getEntries().stream()
                    .map(blockDeferredHolder -> ((Block) blockDeferredHolder.get())).toList();
        }

        @Override
        protected void generate() {
            dropSelf(BlockRegistry.SOUL_BRICKS.get());
            dropSelf(BlockRegistry.SOUL_BRICK_STAIRS.get());
            dropSelf(BlockRegistry.CRACKED_SOUL_BRICKS.get());
            dropSelf(BlockRegistry.SOUL_BRICK_SLAB.get());
            dropSelf(BlockRegistry.SOUL_BRICK_WALL.get());
            dropSelf(BlockRegistry.CHISELED_SOUL_BRICKS.get());
            dropSelf(BlockRegistry.SOUL_BRICK_COLUMN.get());
            dropSelf(BlockRegistry.SOUL_BRICK_DOOR.get());
            dropSelf(BlockRegistry.SOUL_STEEL_BARS.get());
            dropSelf(BlockRegistry.SOUL_CANDLESTICK.get());
            dropSelf(BlockRegistry.SOUL_CANDELABRA.get());
            add(BlockRegistry.DARK_TOMB.get(), noDrop());
            dropSelf(BlockRegistry.STATUE.get());
            dropSelf(BlockRegistry.SOUL_CHANDELIER.get());
            add(BlockRegistry.SOUL_CRYSTAL_ORE.get(), createOreDrop(BlockRegistry.SOUL_CRYSTAL_ORE.get(), ItemRegistry.SOUL_CRYSTAL.get()));
            dropSelf(BlockRegistry.SOUL_CRYSTAL_BLOCK.get());
            add(BlockRegistry.SOULWOOD_LEAVES.get(), createLeavesDrops(BlockRegistry.SOULWOOD_LEAVES.get(), BlockRegistry.SOULWOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
            dropSelf(BlockRegistry.SOULWOOD_SAPLING.get());
            dropSelf(BlockRegistry.SOULWOOD_LIANA.get());
            dropOther(BlockRegistry.SOULWOOD_LIANA_BODY.get(), BlockRegistry.SOULWOOD_LIANA.get());
            dropSelf(BlockRegistry.SOULWOOD_LOG.get());
            dropSelf(BlockRegistry.SOULWOOD_PLANKS.get());
            dropSelf(BlockRegistry.SOULWOOD_STAIRS.get());
            dropSelf(BlockRegistry.PEDESTAL.get());
            dropSelf(BlockRegistry.SOUL_CAULDRON.get());
            dropSelf(BlockRegistry.RESEARCH_TABLE.get());
            dropSelf(BlockRegistry.SOUL_ANVIL.get());
            dropSelf(BlockRegistry.RITUAL_ALTAR.get());
        }
    }

    private static class SoulForgeEntitySubProvider extends EntityLootSubProvider {

        protected SoulForgeEntitySubProvider(HolderLookup.Provider registries) {
            super(FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return EntityRegistry.ENTITIES.getEntries()
                    .stream()
                    .map(DeferredHolder::get);
        }

        @Override
        public void generate() {
            this.add(
                    EntityRegistry.GHOST.get(),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                            .add(
                                                    LootItem.lootTableItem(ItemRegistry.ECTOPLASM.get())
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                            )

                            )
            );
            this.add(EntityRegistry.NERGAL.get(), LootTable.lootTable());
        }
    }
}
