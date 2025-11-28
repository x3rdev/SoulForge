package com.github.x3rdev.soul_forge.common.compat;

import net.irisshaders.iris.api.v0.IrisApi;

public class IrisCompat {

    public static boolean areShadersLoaded() {
        if(ModCompatibility.irisModPresent()) {
            return IrisApi.getInstance().isShaderPackInUse();
        }
        return false;
    }
}
