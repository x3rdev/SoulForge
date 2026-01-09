package com.github.x3rdev.soul_forge.common.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class AncientTabletIncrementer extends SavedData {
    private static int sequencerPosition;

    public static AncientTabletIncrementer create() {
        return new AncientTabletIncrementer();
    }

    public static AncientTabletIncrementer load(CompoundTag compoundTag, HolderLookup.Provider registries) {
        AncientTabletIncrementer savedData = new AncientTabletIncrementer();
        sequencerPosition = compoundTag.getInt("sequencerPosition");
        return savedData;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("sequencerPosition", sequencerPosition);
        return tag;
    }

    public int getSequencerPosition() {
        return sequencerPosition;
    }

    public void increment() {
        sequencerPosition = ++sequencerPosition % 16777216;
        this.setDirty();
    }
}
