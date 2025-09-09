package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.entity.WispEntity;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class WispAmulet extends Item {
    private static final String WISP_UUID_KEY = "wisp_uuid";

    public WispAmulet() {
        super(new Properties().component(DataComponentRegistry.WISP_UUID, null));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        tick(stack, level, entity);
    }

    public static void tick(ItemStack stack, Level level, Entity entity) {
        if (!level.isClientSide() && entity instanceof Player player) {
            if (!wispValid(stack, (ServerLevel) level)) {
                WispEntity wisp = new WispEntity(level, player);
                wisp.setPos(entity.position().add(2, 0, 2));
                level.addFreshEntity(wisp);
                setWispUUID(stack, wisp);
            }
        }
    }

    private static boolean wispValid(ItemStack stack, ServerLevel level) {
        if (getWispUUID(stack) != null) {
            return level.getEntity(getWispUUID(stack)) != null;
        }
        return false;
    }

    private static void setWispUUID(ItemStack stack, WispEntity wisp) {
        stack.set(DataComponentRegistry.WISP_UUID, wisp.getUUID());
    }

    @Nullable
    private static UUID getWispUUID(ItemStack stack) {
        return stack.get(DataComponentRegistry.WISP_UUID);
    }
}
