package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.client.renderer.item.NecronomiconRenderer;
import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.compat.ModCompatibility;
import com.github.x3rdev.soul_forge.common.compat.PatchouliCompat;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.SoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Necronomicon extends Item implements GeoItem {

    private static final RawAnimation CLOSING = RawAnimation.begin().thenPlayAndHold("closing");
    private static final RawAnimation CLOSED = RawAnimation.begin().thenPlayAndHold("closed");
    private static final RawAnimation OPENING = RawAnimation.begin().thenPlayAndHold("opening");
    private static final RawAnimation OPEN = RawAnimation.begin().thenPlayAndHold("open");
    private static final RawAnimation PAGE_FLIP = RawAnimation.begin().thenPlayAndHold("pageflip");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Necronomicon() {
        super(new Properties().stacksTo(1)
                .component(DataComponentRegistry.NECRONOMICON_OPEN, false));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.set(DataComponents.ITEM_NAME, MutableComponent.create(PlainTextContents.EMPTY)
                .append(player.getDisplayName())
                .append("'s ")
                .append(stack.getHoverName()));
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
        if (ModCompatibility.patchouliModPresent())
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
        if (!ModCompatibility.patchouliModPresent())
        {
            player.sendSystemMessage(Component.translatable("error.soul_forge.patchouli_not_installed"));
        }
        else if (player instanceof ServerPlayer serverPlayer)
        {
            stack.set(DataComponentRegistry.NECRONOMICON_OPEN, true);
            triggerAnim(player, GeoItem.getOrAssignId(stack, ((ServerLevel) level)), "c", "opening");
            PatchouliCompat.getAPI().openBookGUI(serverPlayer, ItemRegistry.NECRONOMICON.getId());
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private NecronomiconRenderer renderer;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new NecronomiconRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "c", 1, state -> {
            return state.setAndContinue(CLOSED);
        })
                .triggerableAnim("opening", OPENING)
                .triggerableAnim("closing", CLOSING)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
