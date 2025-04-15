package com.github.x3rdev.soul_forge;

import com.github.x3rdev.soul_forge.client.ClientSetup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = SoulForge.MOD_ID, dist = Dist.CLIENT)
public class SoulForgeClient {

    public SoulForgeClient(ModContainer modContainer) {
        IEventBus modEventBus = modContainer.getEventBus();

        modEventBus.addListener(ClientSetup::registerRenderers);
        modEventBus.addListener(ClientSetup::registerParticleProvider);
        modEventBus.addListener(ClientSetup::registerShaders);
    }
}
