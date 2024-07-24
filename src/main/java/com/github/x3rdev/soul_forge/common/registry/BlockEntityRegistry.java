package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.DarkTombBlockEntity;
import com.github.x3rdev.soul_forge.common.block_entity.StatueBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SoulForge.MOD_ID);

    public static final RegistryObject<BlockEntityType<DarkTombBlockEntity>> DARK_TOMB = BLOCK_ENTITIES.register("dark_tomb",
            () -> BlockEntityType.Builder.of(DarkTombBlockEntity::new, BlockRegistry.DARK_TOMB.get()).build(null));

    public static final RegistryObject<BlockEntityType<StatueBlockEntity>> STATUE = BLOCK_ENTITIES.register("statue",
            () -> BlockEntityType.Builder.of(StatueBlockEntity::new, BlockRegistry.STATUE.get()).build(null));
}
