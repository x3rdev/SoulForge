package com.github.x3rdev.soul_forge.common.entity;

public enum SoulTypes implements SoulType {
    SOUL("soul", 1, 0x98caff),
    UNDEAD_SOUL("undead_soul", 1, 0xb9ff98),
    NETHER_SOUL("nether_soul", 1, 0xf98d1b),
    ENDER_SOUL("ender_soul", 1, 0xb398ff),
    DRAGON_SOUL("dragon_soul", 20, 0x611a75);

    private final String name;
    private final int size;
    private final int color;

    SoulTypes(String name, int size, int color){
        this.name = name;
        this.size = size;
        this.color = color;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int color() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }
}
