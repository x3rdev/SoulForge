package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.menu.SoulAnvilMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypeRegistry {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, SoulForge.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ResearchTableMenu>> RESEARCH_TABLE = MENUS.register("research_table",
            () -> IMenuTypeExtension.create(ResearchTableMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<SoulAnvilMenu>> SOUL_ANVIL = MENUS.register("soul_anvil",
            () -> IMenuTypeExtension.create(SoulAnvilMenu::new));
}
