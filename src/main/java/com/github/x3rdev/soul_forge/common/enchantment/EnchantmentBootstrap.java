package com.github.x3rdev.soul_forge.common.enchantment;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.EnchantmentEffectsRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;

public class EnchantmentBootstrap {

    public static final ResourceKey<Enchantment> REAPING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "reaping"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchantLookup = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> itemLookup = context.lookup(Registries.ITEM);

        register(context, REAPING, Enchantment.enchantment(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                itemLookup.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                4,
                                1,
                                Enchantment.dynamicCost(5, 7),
                                Enchantment.dynamicCost(10, 7),
                                2,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, new ReapingEnchantmentEffect())
        );
    }

    public static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }


}
