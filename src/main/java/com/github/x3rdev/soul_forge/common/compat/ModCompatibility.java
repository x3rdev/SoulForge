package com.github.x3rdev.soul_forge.common.compat;

import net.neoforged.fml.ModList;

public class ModCompatibility {

    private static boolean curioModDetected;
    private static boolean irisModDetected;
    private static boolean jeiModDetected;
    private static boolean patchouliModDetected;

    public static boolean curiosModPresent() {
        return curioModDetected;
    }

    public static boolean irisModPresent() {
        return irisModDetected;
    }

    public static boolean jeiModPresent() {
        return jeiModDetected;
    }

    public static boolean patchouliModPresent() {
        return patchouliModDetected;
    }

    public static void init() {
        curioModDetected = ModList.get().isLoaded("curios");
        irisModDetected = ModList.get().isLoaded("iris");
        jeiModDetected = ModList.get().isLoaded("curios");
        patchouliModDetected = ModList.get().isLoaded("patchouli");
    }
}
