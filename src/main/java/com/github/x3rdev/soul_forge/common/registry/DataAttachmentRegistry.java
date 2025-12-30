package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class DataAttachmentRegistry {

    public static final DeferredRegister<AttachmentType<?>> DATA_ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SoulForge.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<List<ResourceKey<Research>>>> UNLOCKED_RESEARCH = DATA_ATTACHMENT_TYPES.register(
            "research", () -> AttachmentType.builder(() -> List.<ResourceKey<Research>>of())
                    .serialize(Codec.list(ResourceKey.codec(DatapackRegistry.RESEARCH_KEY)))
                    .copyOnDeath()
                    .build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> WORD_STONE_SEED = DATA_ATTACHMENT_TYPES.register(
            "word_stone_seed", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .copyOnDeath()
                    .build()
    );
}
