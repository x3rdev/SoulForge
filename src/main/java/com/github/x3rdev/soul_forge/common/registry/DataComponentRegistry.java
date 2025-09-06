package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.item.Necronomicon;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class DataComponentRegistry {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SoulType>> STORED_SOUL_TYPE = DATA_COMPONENTS.registerComponentType("stored_soul_type",
            builder -> builder.persistent(SoulType.CODEC).networkSynchronized(SoulType.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STORED_SOUL_COUNT = DATA_COMPONENTS.registerComponentType("stored_soul_count",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> WISP_UUID = DATA_COMPONENTS.registerComponentType("wisp_uuid",
            builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> NECRONOMICON_OPEN = DATA_COMPONENTS.registerComponentType("necronomicon_open",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> SHIELD_BLOCKING = DATA_COMPONENTS.registerComponentType("shield_blocking",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
}

