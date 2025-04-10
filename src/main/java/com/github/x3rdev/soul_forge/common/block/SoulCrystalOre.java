package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.world.level.block.Block;

public class SoulCrystalOre extends Block {
    public SoulCrystalOre(Properties pProperties) {
        super(pProperties.lightLevel(value -> 1).strength(3.0f));
    }
}