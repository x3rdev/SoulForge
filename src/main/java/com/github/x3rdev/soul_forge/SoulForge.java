package com.github.x3rdev.soul_forge;

import com.github.x3rdev.soul_forge.common.CommonSetup;
import com.github.x3rdev.soul_forge.common.entity.ai.MovingHitboxAttackRegistry;
import com.github.x3rdev.soul_forge.common.packet.PacketRegistry;
import com.github.x3rdev.soul_forge.common.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(value = SoulForge.MOD_ID)
public class SoulForge {

    public static final String MOD_ID = "soul_forge";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SoulForge(ModContainer modContainer) {
        IEventBus modEventBus = modContainer.getEventBus();

        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        BlockItemRegistry.BLOCK_ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
        EntityDataRegistry.ENTITY_DATA.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);
        StructureRegistry.STRUCTURES.register(modEventBus);


        modEventBus.addListener(CommonSetup::attributeSetup);
        NeoForge.EVENT_BUS.addListener(CommonSetup::onDeath);
        modEventBus.addListener(PacketRegistry::registerPayloadHandlers);
        modEventBus.addListener(MovingHitboxAttackRegistry::registerDatapackRegistries);
    }
}
