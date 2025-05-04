package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.worldgen.structures.CryptStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StructureRegistry {

    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<CryptStructure>> CRYPT_STRUCTURE = STRUCTURES.register("crypt", () -> () -> CryptStructure.CODEC);

}
