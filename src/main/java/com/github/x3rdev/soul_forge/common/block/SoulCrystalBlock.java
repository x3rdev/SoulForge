package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.world.level.block.Block;

public class SoulCrystalBlock extends Block {
    public SoulCrystalBlock(Properties pProperties) {
        super(pProperties.lightLevel(value -> 1).emissiveRendering((state, level, pos) -> true).strength(4.0F, 4.0F).requiresCorrectToolForDrops());
    }
}
