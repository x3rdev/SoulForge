package com.github.x3rdev.soul_forge.common.compat;

import com.github.x3rdev.soul_forge.SoulForge;
import net.neoforged.fml.ModList;

public class PatchouliCompat {

    private static boolean patchouliModDetected;

    public static boolean PatchouliIsPresent()
    {
        return patchouliModDetected;
    }

    public static void init()
    {
        patchouliModDetected = ModList.get().isLoaded("patchouli");
    }
}
