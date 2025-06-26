package com.github.x3rdev.soul_forge.common.item;

import net.minecraft.world.item.Item;

public class RuneItem extends Item {

    private final RuneColor color;
    private final String[] shape;

    public RuneItem(Properties properties, RuneColor color, String[] shape) {
        super(properties.stacksTo(1));
        this.color = color;
        this.shape = shape;
    }

    public RuneColor getColor() {
        return color;
    }

    public String[] getShape() {
        return shape;
    }

    public enum RuneColor {
        RED(0xFF0000),
        BLUE(0x00FF00);

        private final int color;

        RuneColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
}
