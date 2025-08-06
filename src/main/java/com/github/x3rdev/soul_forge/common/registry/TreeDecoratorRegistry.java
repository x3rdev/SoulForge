package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block.SoulwoodSaplingBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TreeDecoratorRegistry {

    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<?>> SOULWOOD_LIANA_DECORATOR = TREE_DECORATORS.register("soulwood_liana",
            () -> new TreeDecoratorType<>(SoulwoodSaplingBlock.SoulwoodLianaDecorator.CODEC));
}
