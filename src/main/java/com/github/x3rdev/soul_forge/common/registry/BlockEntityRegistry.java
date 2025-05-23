package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.DarkTombBlockEntity;
import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.block_entity.StatueBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DarkTombBlockEntity>> DARK_TOMB = BLOCK_ENTITIES.register("dark_tomb",
            () -> BlockEntityType.Builder.of(DarkTombBlockEntity::new, BlockRegistry.DARK_TOMB.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StatueBlockEntity>> STATUE = BLOCK_ENTITIES.register("statue",
            () -> BlockEntityType.Builder.of(StatueBlockEntity::new, BlockRegistry.STATUE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register("pedestal",
            () -> BlockEntityType.Builder.of(PedestalBlockEntity::new, BlockRegistry.PEDESTAL.get()).build(null));
}
