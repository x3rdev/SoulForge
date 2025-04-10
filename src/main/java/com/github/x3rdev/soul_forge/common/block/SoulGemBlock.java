package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.world.level.block.Block;

public class SoulGemBlock extends Block {
    public SoulGemBlock(Properties pProperties) {
        super(pProperties.lightLevel(value -> 1).strength(4.0F, 4.0F).requiresCorrectToolForDrops());
    }
}
