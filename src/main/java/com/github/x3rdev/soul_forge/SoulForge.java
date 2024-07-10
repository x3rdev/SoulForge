package com.github.x3rdev.soul_forge;

import com.github.x3rdev.soul_forge.common.CommonSetup;
import com.github.x3rdev.soul_forge.common.registry.BlockItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SoulForge.MOD_ID)
public class SoulForge {

    public static final String MOD_ID = "soul_forge";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SoulForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;


        BlockItemRegistry.BLOCK_ITEMS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(CommonSetup::attributeSetup);
        forgeBus.addListener(CommonSetup::onDeath);
    }
}
