package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.entity.SoulTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class SoulBottleItem extends Item {

    private static final String SOUL_TYPE_KEY = "soul_type";
    private static final String SOUL_COUNT_KEY = "soul_count";
    private final int capacity;
    public SoulBottleItem(int capacity) {
        super(new Properties());
        this.capacity = capacity;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return getSoulCount(pStack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.round(13F*(float)getSoulCount(pStack)/capacity);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        if(getSoulType(pStack) == null) {
            return 0x000000;
        }
        return getSoulType(pStack).color();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(getSoulType(pStack) != null) {
            int soulCount = getSoulCount(pStack);
            String soulType = formatSoulTypeName(getSoulType(pStack).toString());
            pTooltipComponents.add(Component.translatable("item.soul_forge.soul_bottle.tooltip", soulCount + " " + soulType).withStyle(Style.EMPTY.withColor(getSoulType(pStack).color())));
        } else {
            pTooltipComponents.add(Component.translatable("item.soul_forge.soul_bottle.tooltip", "0").withStyle(ChatFormatting.GRAY));
        }
    }

    public boolean tryFillBottle(ItemStack stack, SoulEntity soulEntity, Player player) {
        SoulType entitySoulType = soulEntity.getSoulType();
        if(canBottleFitSoul(stack, entitySoulType)) {
            setSoulType(stack, entitySoulType);
            setSoulCount(stack, getSoulCount(stack)+entitySoulType.size());
            return true;
        }
        return false;
    }

    private boolean canBottleFitSoul(ItemStack stack, SoulType entitySoulType) {
        SoulType bottleSoulType = getSoulType(stack);
        int bottleSoulCount = getSoulCount(stack);
        if(Objects.isNull(bottleSoulType)) {
            return entitySoulType.size() <= capacity;
        }
        if(bottleSoulType.equals(entitySoulType)) {
            return bottleSoulCount + entitySoulType.size() <= capacity;
        }
        return false;
    }

    private void setSoulType(ItemStack stack, @Nullable SoulType soulType) {
        CompoundTag tag = stack.getOrCreateTag();
        String name = soulType != null ? soulType.toString() : "";
        tag.putString(SOUL_TYPE_KEY, name);
    }
    private @Nullable SoulType getSoulType(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        for (SoulTypes soulType : SoulTypes.values()) {
            if(soulType.toString().equals(tag.getString(SOUL_TYPE_KEY))) {
                return soulType;
            }
        }
        return null;
    }
    private void setSoulCount(ItemStack stack, int count) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(SOUL_COUNT_KEY, count);
    }
    private int getSoulCount(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt(SOUL_COUNT_KEY);
    }

    private String formatSoulTypeName(String s) {
        String input = s.replace('_', ' ').replace("soul", "");
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
