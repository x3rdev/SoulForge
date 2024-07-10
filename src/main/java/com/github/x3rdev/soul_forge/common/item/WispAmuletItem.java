package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.entity.WispEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class WispAmuletItem extends Item {
    private static final String WISP_UUID_KEY = "wisp_uuid";
    public WispAmuletItem() {
        super(new Properties());
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if(!level.isClientSide()) {
            if (!wispValid(stack, (ServerLevel) level)) {
                WispEntity wisp = new WispEntity(level, player);
                wisp.setPos(player.position().add(2, 0,  2));
                level.addFreshEntity(wisp);
                setWisp(stack, wisp);
            }
        }
    }

    private boolean wispValid(ItemStack stack, ServerLevel level) {
        if(getWisp(stack) != null) {
            return level.getEntity(getWisp(stack)) != null;
        }
        return false;
    }
    private void setWisp(ItemStack stack, WispEntity wisp) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID(WISP_UUID_KEY, wisp.getUUID());
    }
    @Nullable
    private UUID getWisp(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(WISP_UUID_KEY) ? tag.getUUID(WISP_UUID_KEY) : null;
    }
}
