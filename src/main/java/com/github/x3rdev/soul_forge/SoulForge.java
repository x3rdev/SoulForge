package com.github.x3rdev.soul_forge;

import com.github.x3rdev.soul_forge.common.CommonSetup;
import com.github.x3rdev.soul_forge.common.compat.ModCompatibility;
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
        SoulForge.LOGGER.info("Initializing soul_forge");
        IEventBus modEventBus = modContainer.getEventBus();
        IEventBus neoEventBus = NeoForge.EVENT_BUS;

        ArmorMaterialRegistry.ARMOR_MATERIALS.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        BlockItemRegistry.BLOCK_ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        DataAttachmentRegistry.DATA_ATTACHMENT_TYPES.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
        EntityDataRegistry.ENTITY_DATA.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        FoliagePlacerTypeRegistry.FOLIAGE_PLACER_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);
        MenuTypeRegistry.MENUS.register(modEventBus);
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        RecipeSerializerRegistry.RECIPE_SERIALIZERS.register(modEventBus);
        RecipeTypeRegistry.RECIPE_TYPES.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);
        StructureRegistry.STRUCTURES.register(modEventBus);
        TreeDecoratorRegistry.TREE_DECORATORS.register(modEventBus);
        TrunkPlacerTypeRegistry.TRUNK_PLACER_TYPES.register(modEventBus);

        modEventBus.addListener(CommonSetup::createEntityAttributes);
        neoEventBus.addListener(CommonSetup::onDeath);
        modEventBus.addListener(PacketRegistry::registerPayloadHandlers);
        modEventBus.addListener(DatapackRegistry::registerDatapackRegistries);
        modEventBus.addListener(CommonSetup::registerSpawnPlacements);
        neoEventBus.addListener(CommonSetup::playerClone);
        neoEventBus.addListener(CommonSetup::playerLoggedIn);
        neoEventBus.addListener(CommonSetup::playerTickEvent);

        ModCompatibility.init();
    }
}
