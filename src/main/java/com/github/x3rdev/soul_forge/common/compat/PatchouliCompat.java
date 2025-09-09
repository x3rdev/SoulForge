package com.github.x3rdev.soul_forge.common.compat;

import com.github.x3rdev.soul_forge.SoulForge;
import net.neoforged.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliCompat {

    private static boolean patchouliModDetected;

    public static boolean PatchouliIsPresent() {
        return patchouliModDetected;
    }

    public static PatchouliAPI.IPatchouliAPI getAPI() {
        if(!PatchouliIsPresent()) throw new IllegalArgumentException("Patchouli not present");
        return PatchouliAPI.get();
    }

    public static void init() {
        patchouliModDetected = ModList.get().isLoaded("patchouli");
    }
}
