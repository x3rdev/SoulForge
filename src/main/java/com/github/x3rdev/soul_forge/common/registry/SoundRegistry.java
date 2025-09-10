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
    public static final DeferredHolder<SoundEvent, SoundEvent> NERGAL_IDLE = registerSound("nergal_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> RITUAL = registerSound("ritual");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCYTHE_SHOOT = registerSound("scythe_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> WISP_DING = registerSound("wisp_ding");
    public static final DeferredHolder<SoundEvent, SoundEvent> WISP_HURT = registerSound("wisp_hurt");


    public static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, name)));
    }
}
