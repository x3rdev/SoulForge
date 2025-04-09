package com.github.x3rdev.soul_forge;

import com.github.x3rdev.soul_forge.client.ClientSetup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = SoulForge.MOD_ID, dist = Dist.CLIENT)
public class SoulForgeClient {

    public SoulForgeClient(IEventBus modEventBus, ModContainer modContainer) {
        IEventBus forgeBus = NeoForge.EVENT_BUS;

        modEventBus.addListener(ClientSetup::registerRenderers);
    }
}
