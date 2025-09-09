package com.github.x3rdev.soul_forge.common.compat;

import com.github.x3rdev.soul_forge.common.item.WispAmulet;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CuriosCompat {

    private static boolean curioModDetected;

    public static boolean CuriosIsPresent() {
        return curioModDetected;
    }

    public static void init() {
        curioModDetected = ModList.get().isLoaded("curios");
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                CuriosCapability.ITEM,
                (stack, context) -> new ICurio() {

                    @Override
                    public ItemStack getStack() {
                        return stack;
                    }

                    @Override
                    public void curioTick(SlotContext slotContext) {
                        WispAmulet.tick(getStack(), slotContext.entity().level(), slotContext.entity());
                    }
                },
                ItemRegistry.WISP_AMULET.get()
        );
        event.registerItem(
                CuriosCapability.ITEM,
                (stack, context) -> new ICurio() {
                    @Override
                    public ItemStack getStack() {
                        return stack;
                    }
                },
                ItemRegistry.RESEARCHER_GLASSES.get()
        );
    }
}
