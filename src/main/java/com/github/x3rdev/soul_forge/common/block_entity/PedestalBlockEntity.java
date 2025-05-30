package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.recipe.RitualInput;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ContainerSingleItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class PedestalBlockEntity extends BlockEntity implements GeoBlockEntity, ContainerSingleItem {

    private final IItemHandler itemHandler = new InvWrapper(this);
    private final Vec3i[] otherPedestalOffsets = new Vec3i[]{
            Vec3i.ZERO.north(3),
            Vec3i.ZERO.north(2).east(2),
            Vec3i.ZERO.east(3),
            Vec3i.ZERO.south(2).east(2),
            Vec3i.ZERO.south(3),
            Vec3i.ZERO.south(2).west(2),
            Vec3i.ZERO.west(3),
            Vec3i.ZERO.north(2).south(2)
    };
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private ItemStack item;

    public PedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.PEDESTAL.get(), pos, blockState);
        this.item = ItemStack.EMPTY;
    }

    public void tryStartRitual() {
        if(isRitualSetupValid()) {
            RitualInput input = buildRitualInput();
            Optional<RecipeHolder<RitualRecipe>> recipe = this.level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.RITUAL.get(), input, this.level);
            if(recipe.isPresent()) {

            } else {
                // TODO: do smthn when recipe doesnt work
            }
        } else {
            spawnMissingPedestalParticles();
        }
    }

    private boolean isRitualSetupValid() {
        for (Vec3i offset : otherPedestalOffsets) {
            BlockState state = this.level.getBlockState(this.getBlockPos().offset(offset));
            if(!state.is(BlockRegistry.PEDESTAL.get())) {
                return false;
            }
        }
        return true;
    }

    private RitualInput buildRitualInput() {
        return new RitualInput(
                this.getTheItem(),
                getItemOnOtherPedestal(otherPedestalOffsets[0]),
                getItemOnOtherPedestal(otherPedestalOffsets[1]),
                getItemOnOtherPedestal(otherPedestalOffsets[2]),
                getItemOnOtherPedestal(otherPedestalOffsets[3]),
                getItemOnOtherPedestal(otherPedestalOffsets[4]),
                getItemOnOtherPedestal(otherPedestalOffsets[5]),
                getItemOnOtherPedestal(otherPedestalOffsets[6]),
                getItemOnOtherPedestal(otherPedestalOffsets[7])
        );
    }

    private ItemStack getItemOnOtherPedestal(Vec3i offset) {
        return level.getBlockEntity(this.getBlockPos().offset(offset), BlockEntityRegistry.PEDESTAL.get()).orElseThrow().getTheItem();
    }

    private void spawnMissingPedestalParticles() {
        for (Vec3i offset : otherPedestalOffsets) {
            BlockPos pos = this.getBlockPos().offset(offset);
            BlockState state = this.level.getBlockState(pos);
            if(!state.is(BlockRegistry.PEDESTAL.get())) {
                this.level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0xFF0000),
                        pos.getCenter().x, pos.getCenter().y, pos.getCenter().z,
                        0, 0, 0);
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.get("item") != null) {
            this.item = ItemStack.parse(registries, tag.get("item")).orElseThrow();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(!item.isEmpty()) {
            tag.put("item", item.save(registries));
        }
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public ItemStack getTheItem() {
        return this.item;
    }

    @Override
    public void setTheItem(ItemStack item) {
        this.item = item;
        this.setChanged();
    }

    @Override
    public ItemStack removeTheItem() {
        ItemStack returnStack = ContainerSingleItem.super.removeTheItem();
        this.item = ItemStack.EMPTY;
        this.setChanged();
        return returnStack;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.getItem(slot).isEmpty() && stack.getCount() == 1;
    }

    //TODO make this method return false during ritual
    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return ContainerSingleItem.super.canTakeItem(target, slot, stack);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    // client code start

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
