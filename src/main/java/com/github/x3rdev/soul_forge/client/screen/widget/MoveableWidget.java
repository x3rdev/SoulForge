package com.github.x3rdev.soul_forge.client.screen.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class MoveableWidget extends AbstractWidget {

    private final int anchorX;
    private final int anchorY;

    protected MoveableWidget(int anchorX, int anchorY, int width, int height, Component message) {
        super(anchorX, anchorY, width, height, message);
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public int getAnchorX() {
        return anchorX;
    }

    public int getAnchorY() {
        return anchorY;
    }

}
