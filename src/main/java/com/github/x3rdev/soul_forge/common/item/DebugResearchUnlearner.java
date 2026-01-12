package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.registry.DataAttachmentRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class DebugResearchUnlearner extends Item {
    public DebugResearchUnlearner() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide()) {
            Research.clearResearchFromPlayer(((ServerPlayer) player));
            player.setData(DataAttachmentRegistry.RESEARCH_DISCOVERED_CHARS.get(), new HashMap<>());
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
