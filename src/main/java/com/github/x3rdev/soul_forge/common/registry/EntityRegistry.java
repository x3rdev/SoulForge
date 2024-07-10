package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulScytheProjectileEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulTypes;
import com.github.x3rdev.soul_forge.common.entity.WispEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SoulForge.MOD_ID);

    public static final RegistryObject<EntityType<SoulEntity>> SOUL = registerMob("soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.SOUL),
            MobCategory.MISC,
            0.5F,
            1.0F);
    public static final RegistryObject<EntityType<SoulEntity>> UNDEAD_SOUL = registerMob("undead_soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.UNDEAD_SOUL),
            MobCategory.MISC,
            0.5F,
            1.0F);
    public static final RegistryObject<EntityType<SoulEntity>> NETHER_SOUL = registerMob("nether_soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.NETHER_SOUL),
            MobCategory.MISC,
            0.5F,
            1.0F);
    public static final RegistryObject<EntityType<SoulEntity>> ENDER_SOUL = registerMob("ender_soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.ENDER_SOUL),
            MobCategory.MISC,
            0.5F,
            1.0F);
    public static final RegistryObject<EntityType<SoulEntity>> DRAGON_SOUL = registerMob("dragon_soul",
            (pEntityType, pLevel) -> new SoulEntity(pEntityType, pLevel, SoulTypes.DRAGON_SOUL),
            MobCategory.MISC,
            0.75F,
            1.0F);
    public static final RegistryObject<EntityType<SoulScytheProjectileEntity>> SOUL_SCYTHE_PROJECTILE = registerMob("soul_scythe_projectile",
            SoulScytheProjectileEntity::new,
            MobCategory.MISC,
            1.5F,
            0.5F);
    public static final RegistryObject<EntityType<WispEntity>> WISP = ENTITIES.register("wisp",
            () -> EntityType.Builder.<WispEntity>of(WispEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .noSave()
                    .noSummon()
                    .build("wisp"));
    public static <T extends Entity> RegistryObject<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> entity, MobCategory mobCategory, float width, float height) {
        return ENTITIES.register(name,
                () -> EntityType.Builder.of(entity, mobCategory).sized(width, height).build(name));
    }
}
