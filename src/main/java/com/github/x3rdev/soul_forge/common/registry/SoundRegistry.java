package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, SoulForge.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> NECRONOMICON_LAUGH = registerSound("necronomicon_laugh");
    public static final DeferredHolder<SoundEvent, SoundEvent> RITUAL = registerSound("ritual");


    public static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, name)));
    }
}
