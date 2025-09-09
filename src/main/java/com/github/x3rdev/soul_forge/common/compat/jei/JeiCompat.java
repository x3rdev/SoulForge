package com.github.x3rdev.soul_forge.common.compat.jei;

import net.neoforged.fml.ModList;

public class JeiCompat {

    private static boolean jeiModDetected;

    public static boolean JeiIsPresent() {
        return jeiModDetected;
    }

    public static void init() {
        jeiModDetected = ModList.get().isLoaded("curios");
    }
}
