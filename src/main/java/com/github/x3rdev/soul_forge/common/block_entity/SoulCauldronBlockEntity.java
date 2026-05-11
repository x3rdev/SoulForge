package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SoulCauldronBlockEntity extends BlockEntity implements GeoBlockEntity {

    public static final int MAX_CAPACITY = 20;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private SoulType soulType;
    private int soulCount;

    public SoulCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.SOUL_CAULDRON.get(), pos, blockState);
        this.soulType = SoulType.EMPTY;
        this.soulCount = 0;
    }

    public SoulType getSoulType() {
        return soulType;
    }

    public void setSoulType(SoulType soulType) {
        this.soulType = soulType;
        this.setChanged();
        this.syncToClients();
    }

    public int getSoulCount() {
        return soulCount;
    }

    public void setSoulCount(int soulCount) {
        if(soulCount == 0) {
            setSoulType(SoulType.EMPTY);
        }
        this.soulCount = soulCount;
        this.setChanged();
        this.syncToClients();
    }

    public int getContainedSoulColor() {
        return SoulType.SOUL.color();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.get("soul_type") != null) {
            this.soulType = SoulType.CODEC.parse(NbtOps.INSTANCE, tag.get("soul_type")).getOrThrow();
        }
        if(tag.get("soul_count") != null) {
            this.soulCount = Codec.INT.parse(NbtOps.INSTANCE, tag.get("soul_count")).getOrThrow();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("soul_type", SoulType.CODEC.encodeStart(NbtOps.INSTANCE, soulType).getOrThrow());
        tag.put("soul_count", Codec.INT.encodeStart(NbtOps.INSTANCE, soulCount).getOrThrow());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    private void syncToClients() {
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
