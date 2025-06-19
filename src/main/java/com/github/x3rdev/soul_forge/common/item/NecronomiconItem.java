package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.compat.PatchouliCompat;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.SoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.Optional;

public class NecronomiconItem extends Item {

    public NecronomiconItem() {
        super(new Properties().stacksTo(1));
    }

    public static void whisper(Player player, Component component) {
        player.level().playSound(null, player.blockPosition(), SoundRegistry.NECRONOMICON_LAUGH.get(), SoundSource.BLOCKS);
        player.displayClientMessage(component.copy().withStyle(ChatFormatting.DARK_GRAY), true);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Optional<PedestalBlockEntity> blockEntity = level.getBlockEntity(blockpos, BlockEntityRegistry.PEDESTAL.get());
        if(blockEntity.isPresent()) {
            if(!level.isClientSide()) {
                blockEntity.get().tryStartRitual(context.getItemInHand(), ((ServerPlayer) context.getPlayer()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(getEdition().copy().withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public static Component getEdition()
    {
        if (PatchouliCompat.PatchouliIsPresent())
        {
            try
            {
                return PatchouliAPI.get().getSubtitle(ItemRegistry.NECRONOMICON.getId());
            }
            catch (IllegalArgumentException e)
            {
                return Component.empty();
            }
        }
        else
        {
            return Component.translatable("error.soul_forge.patchouli_not_installed");
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!PatchouliCompat.PatchouliIsPresent())
        {
            player.sendSystemMessage(Component.translatable("error.soul_forge.patchouli_not_installed"));
        }
        else if (player instanceof ServerPlayer serverPlayer)
        {
            PatchouliAPI.get().openBookGUI(serverPlayer, ItemRegistry.NECRONOMICON.getId());
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
