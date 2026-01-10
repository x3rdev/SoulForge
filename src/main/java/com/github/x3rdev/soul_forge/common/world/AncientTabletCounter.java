package com.github.x3rdev.soul_forge.common.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class AncientTabletCounter extends SavedData {

    private int count;

    private AncientTabletCounter() {
        this.count = 0;
    }

    public static AncientTabletCounter create() {
        return new AncientTabletCounter();
    }

    public static AncientTabletCounter load(CompoundTag compoundTag, HolderLookup.Provider registries) {
        AncientTabletCounter savedData = new AncientTabletCounter();
        savedData.count = compoundTag.getInt("count");
        return savedData;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("count", count);
        return tag;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
        this.setDirty();
    }
}
