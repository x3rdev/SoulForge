package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.compat.PatchouliCompat;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.registry.*;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Necronomicon extends Item {

    public Necronomicon() {
        super(new Properties().stacksTo(1)
                .component(DataComponentRegistry.NECRONOMICON_DATA, new NecronomiconData(List.of()))
        );
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.set(DataComponents.ITEM_NAME, MutableComponent.create(PlainTextContents.EMPTY).append(player.getDisplayName()).append("'s").append(stack.getDisplayName()));
//        stack.set(DataComponents.LORE, Component.literal(String.format("%d research unlocked", 0)));
    }


    public static boolean isResearchUnlocked(ItemStack stack, Holder.Reference<Research> research) {
        if(!stack.is(ItemRegistry.NECRONOMICON.get())) {
            throw new IllegalArgumentException(String.format("ItemStack %s is not a Necronomicon", stack));
        }
        if(research.value().inactive()) {
            return true;
        }
        return stack.get(DataComponentRegistry.NECRONOMICON_DATA).unlockedResearch().contains(research.key());
    }

    public static boolean isRitualUnlocked(ServerPlayer player, ItemStack stack, RecipeHolder<RitualRecipe> recipe) {
        if(!stack.is(ItemRegistry.NECRONOMICON.get())) {
            throw new IllegalArgumentException(String.format("ItemStack %s is not a Necronomicon", stack));
        }
        return stack.get(DataComponentRegistry.NECRONOMICON_DATA).unlockedResearch().stream()
                .map(researchResourceKey -> player.level().registryAccess().holder(researchResourceKey).orElseThrow().value().ritualReward())
                .anyMatch(recipeResourceKey -> recipeResourceKey.orElseThrow().equals(recipe.id()));
    }

    public static void unlockResearch(ItemStack stack, Holder.Reference<Research> research) {
        if(!stack.is(ItemRegistry.NECRONOMICON.get())) {
            throw new IllegalArgumentException(String.format("ItemStack %s is not a Necronomicon", stack));
        }
        List<ResourceKey<Research>> currentKeys = stack.get(DataComponentRegistry.NECRONOMICON_DATA).unlockedResearch();
        ResourceKey<Research> newKey = research.key();
        if(!currentKeys.contains(newKey)) {
            List<ResourceKey<Research>> newKeys = new ArrayList<>(currentKeys);
            newKeys.add(newKey);
            stack.set(DataComponentRegistry.NECRONOMICON_DATA, new NecronomiconData(newKeys));
        }
    }

    public static void whisper(Player player, Component component) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegistry.NECRONOMICON_LAUGH.get(), SoundSource.BLOCKS);
        player.displayClientMessage(component, true);
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
                return PatchouliCompat.getAPI().getSubtitle(ItemRegistry.NECRONOMICON.getId());
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
            PatchouliCompat.getAPI().openBookGUI(serverPlayer, ItemRegistry.NECRONOMICON.getId());
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public record NecronomiconData(List<ResourceKey<Research>> unlockedResearch) {

        public static final Codec<NecronomiconData> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.list(ResourceKey.codec(DatapackRegistry.RESEARCH_KEY)).fieldOf("unlocked_research").forGetter(NecronomiconData::unlockedResearch)
                ).apply(instance, NecronomiconData::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, NecronomiconData> STREAM_CODEC = StreamCodec.composite(
                ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY).apply(ByteBufCodecs.list()),
                NecronomiconData::unlockedResearch,
                NecronomiconData::new
        );
    }
}
