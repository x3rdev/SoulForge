package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SoulEntity>> SOUL = registerMob("soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.SOUL),
            MobCategory.MISC,
            0.5F,
            1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<SoulEntity>> UNDEAD_SOUL = registerMob("undead_soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.UNDEAD_SOUL),
            MobCategory.MISC,
            0.5F,
            1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<SoulEntity>> NETHER_SOUL = registerMob("nether_soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.NETHER_SOUL),
            MobCategory.MISC,
            0.5F,
            1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<SoulEntity>> ENDER_SOUL = registerMob("ender_soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.ENDER_SOUL),
            MobCategory.MISC,
            0.5F,
            1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<SoulEntity>> DRAGON_SOUL = registerMob("dragon_soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.DRAGON_SOUL),
            MobCategory.MISC,
            0.75F,
            1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<SoulScytheProjectileEntity>> SOUL_SCYTHE_PROJECTILE = registerMob("soul_scythe_projectile",
            SoulScytheProjectileEntity::new,
            MobCategory.MISC,
            1.5F,
            0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<WispEntity>> WISP = ENTITIES.register("wisp",
            () -> EntityType.Builder.<WispEntity>of(WispEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .noSave()
                    .noSummon()
                    .build("wisp"));
    public static final DeferredHolder<EntityType<?>, EntityType<GhostEntity>> GHOST = registerMob("ghost",
            GhostEntity::new,
            MobCategory.MONSTER,
            0.6F,
            1.8F);
    public static final DeferredHolder<EntityType<?>, EntityType<NergalEntity>> NERGAL = registerMob("nergal",
            NergalEntity::new,
            MobCategory.MONSTER,
            2.5F,
            4F);


    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> entity, MobCategory mobCategory, float width, float height) {
        return ENTITIES.register(name,
                () -> EntityType.Builder.of(entity, mobCategory).sized(width, height).build(name));
    }
}
