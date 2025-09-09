package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.client.renderer.item.ResearcherGlassesRenderer;
import com.github.x3rdev.soul_forge.common.compat.CuriosCompat;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;
import java.util.function.Consumer;

public class ResearcherGlasses extends ArmorItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ResearcherGlasses(Type pType) {
        super(ArmorMaterials.GOLD, pType, new Properties().stacksTo(1));
    }

    public static boolean playerHasResearcherGlassesEquipped(Player player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if(head.getItem() instanceof ResearcherGlasses) {
            return true;
        }
        if(CuriosCompat.CuriosIsPresent()) {
            return playerHasResearcherGlassesInCuriosSlots(player);
        }
        return false;
    }

    private static boolean playerHasResearcherGlassesInCuriosSlots(Player player) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
        if(curiosInventory.isPresent()) {
            IItemHandlerModifiable equippedCurios = curiosInventory.get().getEquippedCurios();
            for (int i = 0; i < equippedCurios.getSlots(); i++) {
                ItemStack stackInSlot = equippedCurios.getStackInSlot(i);
                if(stackInSlot.getItem() instanceof ResearcherGlasses) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private ResearcherGlassesRenderer renderer;

            @Override
            public @Nullable <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null)
                    this.renderer = new ResearcherGlassesRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
