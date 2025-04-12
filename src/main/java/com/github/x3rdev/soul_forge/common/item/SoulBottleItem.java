package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.entity.SoulTypes;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.List;

public class SoulBottleItem extends Item {

    private final int capacity;

    public SoulBottleItem(int capacity) {
        super(new Properties()
                .component(DataComponentRegistry.STORED_SOUL_TYPE.get(), null)
                .component(DataComponentRegistry.STORED_SOUL_COUNT.get(), 0)
        );
        this.capacity = capacity;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return getSoulCount(pStack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.round(13F * (float) getSoulCount(pStack) / capacity);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        if (getSoulType(pStack) == null) {
            return 0x000000;
        }
        return getSoulType(pStack).color();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (getSoulType(stack) != null) {
            int soulCount = getSoulCount(stack);
            String soulType = formatSoulTypeName(getSoulType(stack));
            tooltipComponents.add(Component.translatable("item.soul_forge.soul_bottle.tooltip", soulCount + " " + soulType).withColor(getSoulType(stack).color()));
        } else {
            tooltipComponents.add(Component.translatable("item.soul_forge.soul_bottle.tooltip", "0").withColor(0x28292e));
        }
    }

    public boolean tryFillBottle(ItemStack stack, SoulEntity soulEntity, Player player) {
        SoulType entitySoulType = soulEntity.getSoulType();
        if (canBottleFitSoul(stack, entitySoulType)) {
            setSoulType(stack, entitySoulType);
            setSoulCount(stack, getSoulCount(stack) + entitySoulType.size());
            return true;
        }
        return false;
    }

    private boolean canBottleFitSoul(ItemStack stack, SoulType entitySoulType) {
        SoulType bottleSoulType = getSoulType(stack);
        int bottleSoulCount = getSoulCount(stack);
        if (bottleSoulType == null) {
            return entitySoulType.size() <= capacity;
        }
        if (bottleSoulType.equals(entitySoulType)) {
            return bottleSoulCount + entitySoulType.size() <= capacity;
        }
        return false;
    }

    private void setSoulType(ItemStack stack, @Nullable SoulType soulType) {
        stack.set(DataComponentRegistry.STORED_SOUL_TYPE, soulType.toString());
    }

    private @Nullable SoulType getSoulType(ItemStack stack) {
        for (SoulTypes soulType : SoulTypes.values()) {
            if (soulType.toString().equals(stack.get(DataComponentRegistry.STORED_SOUL_TYPE))) {
                return soulType;
            }
        }
        return null;
    }

    private void setSoulCount(ItemStack stack, int count) {
        stack.set(DataComponentRegistry.STORED_SOUL_COUNT, count);
    }

    private int getSoulCount(ItemStack stack) {
        return stack.get(DataComponentRegistry.STORED_SOUL_COUNT);
    }

    private String formatSoulTypeName(SoulType type) {
        String input = type.toString().replace('_', ' ').replace("soul", "");
        String[] words = input.split("\\s");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(Character.toTitleCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return result.toString().trim();
    }
}
