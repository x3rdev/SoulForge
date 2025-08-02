package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class EntityDataRegistry {

    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, SoulForge.MOD_ID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<AABB>> DEBUG_BOX = ENTITY_DATA.register("debug_box",
            () -> EntityDataSerializer.forValueType(StreamCodec.of(
                    (buffer, aabb) -> {
                        buffer.writeVec3(aabb.getMinPosition());
                        buffer.writeVec3(aabb.getMaxPosition());
                    },
                    buffer -> new AABB(buffer.readVec3(), buffer.readVec3())
            )));

}
