package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.common.compat.ModCompatibility;
import com.github.x3rdev.soul_forge.common.compat.jei.SoulForgePlugin;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Objects;

public class ClickableRitualWidget extends AbstractWidget {

    private final ResearchTableScreen screen;

    public ClickableRitualWidget(int x, int y, ResearchTableScreen screen) {
        super(x, y, 16, 16, Component.literal("ritual"));
        this.screen = screen;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if(isActive()) {
            super.onClick(mouseX, mouseY, button);
            if (ModCompatibility.jeiModPresent()) {
                List<ResourceLocation> recipes = recipesUnlockedByRitual();
                if(!recipes.isEmpty()) {
                    SoulForgePlugin.showRecipes(recipes);
                }
//                screen.getActiveResearch().orElseThrow().value().ritualReward().ifPresent(resourceLocation -> {
//                    SoulForgePlugin.showRecipe(getRitualResultStack(resourceLocation));
//                });
            }
        }
    }

    @Override
    public void playDownSound(SoundManager handler) {
        if(isActive()) {
            super.playDownSound(handler);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(isActive()) {
            ClientLevel level = Minecraft.getInstance().level;
            List<ResourceLocation> recipesUnlockedByRitual = recipesUnlockedByRitual();
            if(!recipesUnlockedByRitual.isEmpty()) {
                ResourceLocation resourceLocation = recipesUnlockedByRitual.get((int) (level.getGameTime() / 20 % recipesUnlockedByRitual.size()));
                level.getRecipeManager().byKey(resourceLocation).ifPresent(holder -> {
                    guiGraphics.renderFakeItem(holder.value().getResultItem(level.registryAccess()), getX(), getY());
                    if (isHovered()) {
                        guiGraphics.fill(RenderType.guiGhostRecipeOverlay(), getX(), getY(), getX() + 16, getY() + 16, 0x66FFFFFF);
                    }
                });
            }
        }
    }

    private List<ResourceLocation> recipesUnlockedByRitual() {
        if(isActive()) {
            ClientLevel level = Minecraft.getInstance().level;
            return level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.RITUAL.get()).stream()
                    .filter(recipeRecipeHolder -> {
                        if (recipeRecipeHolder.value().requiredResearch().isPresent()) {
                            return Objects.equals(screen.getActiveResearch().getKey(), recipeRecipeHolder.value().requiredResearch().get());
                        }
                        return false;
                    })
                    .map(RecipeHolder::id)
                    .toList();
        }
        return List.of();
    }

    @Override
    public boolean isActive() {
        return super.isActive() && screen.inspectScreenActive() && Research.playerHasResearchUnlocked(Minecraft.getInstance().player, screen.getActiveResearch());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
