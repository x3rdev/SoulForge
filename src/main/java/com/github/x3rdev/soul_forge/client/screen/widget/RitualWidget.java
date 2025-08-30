package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.common.compat.jei.SoulForgePlugin;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class RitualWidget extends AbstractWidget {

    private final ResearchTableScreen screen;

    public RitualWidget(int x, int y, ResearchTableScreen screen) {
        super(x, y, 16, 16, Component.literal("ritual"));
        this.screen = screen;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
        if(ModList.get().isLoaded("jei")) {

        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(screen.inspectScreenActive()) {
            screen.getActiveResearch().orElseThrow().value().ritualReward().ifPresent(recipeResourceKey -> {
                guiGraphics.renderFakeItem(getRitualResultStack(recipeResourceKey), getX(), getY());
                if(isHovered()) {
                    guiGraphics.fill(RenderType.guiGhostRecipeOverlay(), getX(), getY(), getX() + 16, getY() + 16, 0x66FFFFFF);
                    if(!ModList.get().isLoaded("jei")) {
                        guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal("JEI not installed"), mouseX, mouseY);
                    }
                }
            });
        }
    }

    private ItemStack getRitualResultStack(ResourceLocation resourceLocation) {
        RecipeHolder<?> holder = Minecraft.getInstance().level.getRecipeManager().byKey(resourceLocation)
                .orElseThrow(() -> new IllegalStateException("Recipe key " + resourceLocation + " found"));
        if(holder.value() instanceof RitualRecipe ritualRecipe) {
            return ritualRecipe.result();
        }
        throw new IllegalStateException("Recipe key provided by research was not RitualRecipe");
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
