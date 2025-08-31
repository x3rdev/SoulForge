package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.menu.SoulAnvilMenu;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilInput;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilRecipe;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilSerializer;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.stream.IntStream;

public class SoulAnvilBlockEntity extends BaseContainerBlockEntity implements GeoBlockEntity {

    public static final int SOUL_ANVIL_CONTAINER_SIZE = 13;
    public static final int TICKS_UNTIL_ITEM_CRAFTED = 12 * 20;
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation FORGING = RawAnimation.begin().thenPlay("forging");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private NonNullList<ItemStack> items = NonNullList.withSize(9+4+1, ItemStack.EMPTY);
    private Optional<SoulAnvilRecipe> activeRecipe = Optional.empty();
    private int progressTicks = 0;

    public SoulAnvilBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.SOUL_ANVIL.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SoulAnvilBlockEntity blockEntity) {
        if(blockEntity.getActiveRecipe().isPresent()) {
            SoulAnvilRecipe recipe = blockEntity.getActiveRecipe().get();
            if(isRecipeStillValid(level, blockEntity)) {
                blockEntity.progressTicks++;
                if(blockEntity.progressTicks > TICKS_UNTIL_ITEM_CRAFTED) {
                    for (int i = 0; i < recipe.gridInput().ingredients().size(); i++) {
                        if(!recipe.gridInput().ingredients().get(i).isEmpty()) {
                            blockEntity.getItem(i).shrink(1);
                        }
                    }
                    for (int i = 0; i < recipe.outerInputs().size(); i++) {
                        if(!recipe.outerInputs().get(i).isEmpty()) {
                            blockEntity.getItem(i+9).shrink(1);
                        }
                    }
                    Vec3 itemPos = blockEntity.getBlockPos().getCenter().add(0, 1, 0);
                    ItemEntity entity = new ItemEntity(level, itemPos.x, itemPos.y, itemPos.z, recipe.result().copy());
                    level.addFreshEntity(entity);
                    blockEntity.stopTriggeredAnim("c", "forging");
                    blockEntity.setActiveRecipe(null);
                }
                blockEntity.setChanged();
                blockEntity.getLevel().sendBlockUpdated(pos, state, state, 3);
            } else {
                blockEntity.setActiveRecipe(null);
                blockEntity.setChanged();
                blockEntity.getLevel().sendBlockUpdated(pos, state, state, 3);
                blockEntity.stopTriggeredAnim("c", "forging");
            }
        } else {
            blockEntity.progressTicks = 0;
        }
    }

    private static boolean isRecipeStillValid(Level level, SoulAnvilBlockEntity blockEntity) {
        SoulAnvilInput input = new SoulAnvilInput(
                CraftingInput.of(3, 3, IntStream.range(0, 9).mapToObj(blockEntity::getItem).toList()),
                IntStream.range(9, 13).mapToObj(blockEntity::getItem).toList());
        return blockEntity.getActiveRecipe().orElseThrow().matches(input, level);
    }

    public void startAnvil() {
        SoulAnvilInput input = new SoulAnvilInput(
                CraftingInput.of(3, 3, IntStream.range(0, 9).mapToObj(this::getItem).toList()),
                IntStream.range(9, 13).mapToObj(this::getItem).toList());
        Optional<RecipeHolder<SoulAnvilRecipe>> holderOptional = this.level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.SOUL_ANVIL.get(), input, this.level);
        holderOptional.ifPresent(soulAnvilRecipeRecipeHolder -> setActiveRecipe(soulAnvilRecipeRecipeHolder.value()));
        this.triggerAnim("c", "forging");
    }

    private Optional<SoulAnvilRecipe> getActiveRecipe() {
        return this.activeRecipe;
    }

    private void setActiveRecipe(@Nullable SoulAnvilRecipe recipe) {
        progressTicks = 0;
        this.activeRecipe = Optional.ofNullable(recipe);
    }

    public int getProgressTicks() {
        return progressTicks;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.soul_forge.soul_anvil");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SoulAnvilMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return SOUL_ANVIL_CONTAINER_SIZE;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.progressTicks = tag.getInt("progress_ticks");
        if(tag.contains("active_recipe")) {
            this.activeRecipe = SoulAnvilSerializer.CODEC.codec().parse(NbtOps.INSTANCE, tag.getCompound("active_recipe")).result();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("progress_ticks", this.progressTicks);
        this.activeRecipe.ifPresent(soulAnvilRecipe -> {
            tag.put("active_recipe", SoulAnvilSerializer.CODEC.codec().encodeStart(NbtOps.INSTANCE, soulAnvilRecipe).getOrThrow());
        });
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "c", 10, state -> PlayState.CONTINUE)
                .triggerableAnim("forging", FORGING));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
