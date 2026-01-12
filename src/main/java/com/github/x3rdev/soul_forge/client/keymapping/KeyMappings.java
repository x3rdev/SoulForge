package com.github.x3rdev.soul_forge.client.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {
    public static final Lazy<KeyMapping> KEY_FLY = Lazy.of(() ->
            new KeyMapping("key.soul_forge.fly", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, KeyMapping.CATEGORY_MOVEMENT)
    );

    @SubscribeEvent
    public static void registerKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(KeyMappings.KEY_FLY.get());
    }
}
