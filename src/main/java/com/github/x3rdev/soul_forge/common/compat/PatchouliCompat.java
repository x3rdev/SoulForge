package com.github.x3rdev.soul_forge.common.compat;

import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliCompat {

    public static PatchouliAPI.IPatchouliAPI getAPI() {
        if(!ModCompatibility.patchouliModPresent()) throw new IllegalArgumentException("Patchouli not present");
        return PatchouliAPI.get();
    }
}
