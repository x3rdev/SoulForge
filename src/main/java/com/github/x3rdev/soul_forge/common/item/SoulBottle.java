package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.block_entity.SoulStorageBlockEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class SoulBottle extends Item {

    private final int capacity;

    public SoulBottle(int capacity) {
        super(new Properties()
                .component(DataComponentRegistry.STORED_SOUL_TYPE.get(), SoulType.EMPTY)
                .component(DataComponentRegistry.STORED_SOUL_COUNT.get(), 0)
                .stacksTo(1)
        );
        this.capacity = capacity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        Optional<SoulStorageBlockEntity> optionalSoulStorage = optionalInViewSoulStorage(level, player);
        if(optionalSoulStorage.isPresent()) {
            SoulStorageBlockEntity soulStorage = optionalSoulStorage.get();
            boolean interactionSuccess;
            if(getSoulCount(stack) > 0) {
                interactionSuccess = tryEmptyBottle(stack, soulStorage, player);
            } else {
                interactionSuccess = tryFillBottle(stack, soulStorage, player);
            }
            if(interactionSuccess) {
                return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
            }
        }
        return super.use(level, player, usedHand);
    }

    private Optional<SoulStorageBlockEntity> optionalInViewSoulStorage(Level level, Player player) {
        ChunkPos pos = new ChunkPos(player.blockPosition());
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (BlockEntity blockEntity : level.getChunk(pos.x+i, pos.z+j).getBlockEntities().values()) {
                    if (blockEntity instanceof SoulStorageBlockEntity soulStorage) {
                        Vec3 lookVec = player.getLookAngle().normalize();
                        AABB box = AABB.ofSize(soulStorage.getBlockPos().getCenter().add(0, soulStorage.getCrystalHeight(0), 0), 1.5, 1.5, 1.5);
                        if(box.clip(player.getEyePosition(), player.getEyePosition().add(lookVec.scale(10))).isPresent()) {
                            return Optional.of(soulStorage);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return getSoulCount(pStack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.round(13F * getSoulCount(pStack) / capacity);
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
            tooltipComponents.add(Component.translatable("item.soul_forge.soul_bottle.tooltip", soulCount + soulType).withColor(getSoulType(stack).color()));
        } else {
            tooltipComponents.add(Component.translatable("item.soul_forge.soul_bottle.tooltip.empty").withStyle(ChatFormatting.GRAY));
        }
    }

    public boolean tryEmptyBottle(ItemStack stack, SoulStorageBlockEntity soulStorage, Player player) {
        SoulType bottleSoulType = getSoulType(stack);
        SoulType storageSoulType = soulStorage.getSoulType();
        if(!bottleSoulType.isEmpty() && (storageSoulType.isEmpty() || bottleSoulType.equals(storageSoulType)) && bottleSoulTypeFitsInStorage(stack, soulStorage)) {
            soulStorage.setSoulType(bottleSoulType);
            while (bottleSoulTypeFitsInStorage(stack, soulStorage) && getSoulCount(stack) > 0) {
                soulStorage.setSoulCount(soulStorage.getSoulCount()+bottleSoulType.size());
                setSoulCount(stack, getSoulCount(stack)-bottleSoulType.size());
            }
            player.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH);
            return true;
        }
        return false;
    }

    private boolean bottleSoulTypeFitsInStorage(ItemStack stack, SoulStorageBlockEntity blockEntity) {
        return getSoulType(stack).size() <= blockEntity.getMaxCapacity()-blockEntity.getSoulCount();
    }

    public boolean tryFillBottle(ItemStack stack, SoulEntity soulEntity, Player player) {
        SoulType entitySoulType = soulEntity.getSoulType();
        if (canBottleFitSoul(stack, entitySoulType)) {
            setSoulType(stack, entitySoulType);
            setSoulCount(stack, getSoulCount(stack) + entitySoulType.size());
            player.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH);
            return true;
        }
        return false;
    }

    public boolean tryFillBottle(ItemStack stack, SoulStorageBlockEntity soulStorage, Player player) {
        SoulType storageSoulType = soulStorage.getSoulType();
        if(canBottleFitSoul(stack, storageSoulType)) {
            setSoulType(stack, storageSoulType);
            while (canBottleFitSoul(stack, storageSoulType) && soulStorage.getSoulCount() > 0) {
                setSoulCount(stack, getSoulCount(stack) + storageSoulType.size());
                soulStorage.setSoulCount(soulStorage.getSoulCount() - storageSoulType.size());
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

    private void setSoulType(ItemStack stack, SoulType soulType) {
        stack.set(DataComponentRegistry.STORED_SOUL_TYPE, soulType);
    }

    private SoulType getSoulType(ItemStack stack) {
        return stack.get(DataComponentRegistry.STORED_SOUL_TYPE);
    }

    private void setSoulCount(ItemStack stack, int soulCount) {
        if(soulCount == 0) {
            setSoulType(stack,  SoulType.EMPTY);
        }
        stack.set(DataComponentRegistry.STORED_SOUL_COUNT, soulCount);
    }

    private int getSoulCount(ItemStack stack) {
        return stack.get(DataComponentRegistry.STORED_SOUL_COUNT);
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
