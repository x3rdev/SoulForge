package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.block_entity.SoulCauldronBlockEntity;
import com.github.x3rdev.soul_forge.common.entity.Soul;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;
import java.util.Optional;

public class SoulContainer extends Item {

    private final int capacity;

    public SoulContainer(int capacity) {
        super(new Properties()
                .component(DataComponentRegistry.STORED_SOUL_TYPE.get(), SoulType.EMPTY)
                .component(DataComponentRegistry.STORED_SOUL_COUNT.get(), 0)
                .stacksTo(1)
        );
        this.capacity = capacity;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        Optional<SoulCauldronBlockEntity> soulCauldronOptional = context.getLevel().getBlockEntity(context.getClickedPos(), BlockEntityRegistry.SOUL_CAULDRON.get());
        if(soulCauldronOptional.isPresent()) {
            SoulCauldronBlockEntity soulCauldron = soulCauldronOptional.get();
            boolean interactionSuccess;
            if(getSoulCount(stack) > 0) {
                interactionSuccess = tryEmptyBottle(stack, soulCauldron, player);
            } else {
                interactionSuccess = tryFillBottle(stack, soulCauldron, player);
            }
            if(interactionSuccess) {
                return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
            }
        }
        return super.useOn(context);
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return getSoulCount(pStack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.round(13F * getSoulCount(pStack)*getSoulType(pStack).size() / capacity);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return getSoulType(pStack).color();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (!getSoulType(stack).isEmpty()) {
            int soulCount = getSoulCount(stack);
            String soulType = formatSoulTypeName(getSoulType(stack));
            tooltipComponents.add(Component.translatable(
                    soulCount == 1 ? "item.soul_forge.soul_container.tooltip" : "item.soul_forge.soul_container.tooltip_plural",
                    soulCount + soulType, capacity).withColor(getSoulType(stack).color()));
        } else {
            tooltipComponents.add(Component.translatable("item.soul_forge.soul_container.tooltip.empty").withStyle(ChatFormatting.GRAY));
        }
    }

    public boolean tryEmptyBottle(ItemStack stack, SoulCauldronBlockEntity soulStorage, Player player) {
        SoulType bottleSoulType = getSoulType(stack);
        SoulType storageSoulType = soulStorage.getSoulType();
        if(!bottleSoulType.isEmpty() && (storageSoulType.isEmpty() || bottleSoulType.equals(storageSoulType)) && bottleSoulTypeFitsInCauldron(stack, soulStorage)) {
            soulStorage.setSoulType(bottleSoulType);
            while (bottleSoulTypeFitsInCauldron(stack, soulStorage) && getSoulCount(stack) > 0) {
                soulStorage.setSoulCount(soulStorage.getSoulCount()+1);
                setSoulCount(stack, getSoulCount(stack)-1);
            }
            player.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH);
            return true;
        }
        return false;
    }

    private boolean bottleSoulTypeFitsInCauldron(ItemStack stack, SoulCauldronBlockEntity blockEntity) {
        return getSoulType(stack).size() <= SoulCauldronBlockEntity.MAX_CAPACITY-(blockEntity.getSoulCount()*getSoulType(stack).size());
    }

    public boolean tryFillBottle(ItemStack stack, Soul soulEntity, Player player) {
        SoulType entitySoulType = soulEntity.getSoulType();
        if (canBottleFitSoul(stack, entitySoulType)) {
            setSoulType(stack, entitySoulType);
            setSoulCount(stack, getSoulCount(stack) + 1);
            player.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH);
            return true;
        }
        return false;
    }

    public boolean tryFillBottle(ItemStack stack, SoulCauldronBlockEntity soulStorage, Player player) {
        SoulType storageSoulType = soulStorage.getSoulType();
        if(canBottleFitSoul(stack, storageSoulType)) {
            setSoulType(stack, storageSoulType);
            while (canBottleFitSoul(stack, storageSoulType) && soulStorage.getSoulCount() > 0) {
                setSoulCount(stack, getSoulCount(stack) + 1);
                soulStorage.setSoulCount(soulStorage.getSoulCount() - 1);
            }
            player.playSound(SoundEvents.SHULKER_TELEPORT);
            return true;
        }
        return false;
    }

    private boolean canBottleFitSoul(ItemStack stack, SoulType entitySoulType) {
        SoulType bottleSoulType = getSoulType(stack);
        int bottleSoulCount = getSoulCount(stack);
        if (bottleSoulType.isEmpty()) {
            return entitySoulType.size() <= capacity;
        }
        if (bottleSoulType.equals(entitySoulType)) {
            return bottleSoulCount + entitySoulType.size() <= capacity;
        }
        return false;
    }

    public void setSoulType(ItemStack stack, SoulType soulType) {
        stack.set(DataComponentRegistry.STORED_SOUL_TYPE, soulType);
    }

    private SoulType getSoulType(ItemStack stack) {
        return stack.get(DataComponentRegistry.STORED_SOUL_TYPE);
    }

    public void setSoulCount(ItemStack stack, int soulCount) {
        if(soulCount == 0) {
            setSoulType(stack, SoulType.EMPTY);
        }
        stack.set(DataComponentRegistry.STORED_SOUL_COUNT, soulCount);
    }

    public int getSoulCount(ItemStack stack) {
        return stack.get(DataComponentRegistry.STORED_SOUL_COUNT);
    }

    public int getCapacity() {
        return capacity;
    }

    private String formatSoulTypeName(SoulType type) {
        String input = type.toString().replace('_', ' ').replace("soul", "");
        String[] words = input.split("\\s");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if(!word.isBlank()) {
                result.append(word).append(" ");
            }
        }
        if(!result.isEmpty()) {
            result.insert(0, " ");
        }
        return result.toString().stripTrailing();
    }
}
