package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.SoulForge;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SoulForgeEntityTagsProvider extends EntityTypeTagsProvider {

    public static final TagKey<EntityType<?>> DROPS_UNDEAD_SOUL = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(SoulForge.MOD_ID, "drops_undead_soul"));
    public static final TagKey<EntityType<?>> DROPS_NETHER_SOUL = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(SoulForge.MOD_ID, "drops_nether_soul"));
    public static final TagKey<EntityType<?>> DROPS_ENDER_SOUL = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(SoulForge.MOD_ID, "drops_ender_soul"));;
    public static final TagKey<EntityType<?>> DROPS_DRAGON_SOUL = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(SoulForge.MOD_ID, "drops_dragon_soul"));;



    public SoulForgeEntityTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, SoulForge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DROPS_UNDEAD_SOUL).add(EntityType.HUSK, EntityType.PHANTOM, EntityType.SKELETON, EntityType.SKELETON_HORSE, EntityType.STRAY, EntityType.ZOGLIN, EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN);
        this.tag(DROPS_NETHER_SOUL).add(EntityType.BLAZE, EntityType.GHAST, EntityType.HOGLIN, EntityType.MAGMA_CUBE, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.WITHER, EntityType.WITHER_SKELETON);
        this.tag(DROPS_ENDER_SOUL).add(EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER);
        this.tag(DROPS_DRAGON_SOUL).add(EntityType.ENDER_DRAGON);
    }
}
