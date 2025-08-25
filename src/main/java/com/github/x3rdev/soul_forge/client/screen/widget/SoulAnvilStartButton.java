package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.client.screen.SoulAnvilScreen;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilRecipe;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;

public class SoulAnvilStartButton extends Button {

    private final SoulAnvilScreen screen;

    public SoulAnvilStartButton(SoulAnvilScreen screen, int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.screen = screen;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if(screen.canPressHammer()) {
            super.onClick(mouseX, mouseY);
        }
    }

    @Override
    public void playDownSound(SoundManager handler) {
        if(screen.canPressHammer()) {
            super.playDownSound(handler);
        }
    }
}
