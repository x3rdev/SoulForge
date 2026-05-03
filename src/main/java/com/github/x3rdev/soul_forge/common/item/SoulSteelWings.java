package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.item.SoulSteelArmorRenderer;
import com.github.x3rdev.soul_forge.client.renderer.item.SoulSteelWingsRenderer;
import com.github.x3rdev.soul_forge.common.registry.ArmorMaterialRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class SoulSteelWings extends ArmorItem implements GeoItem {

    public static final RawAnimation FOLDING = RawAnimation.begin().thenPlayAndHold("folding");
    public static final RawAnimation FOLD_IDLE = RawAnimation.begin().thenPlay("fold_idle");
    public static final RawAnimation UNFOLDING = RawAnimation.begin().thenPlay("unfolding");
    public static final RawAnimation GLIDE = RawAnimation.begin().thenPlay("glide");
    public static final RawAnimation FLY = RawAnimation.begin().thenPlay("fly");
    public static final RawAnimation FLOAT = RawAnimation.begin().thenPlay("float");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SoulSteelWings(Properties properties) {
        super(ArmorMaterialRegistry.SOUL_STEEL_WINGS, Type.CHESTPLATE, properties);

    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private SoulSteelWingsRenderer renderer;

            @Override
            public @Nullable <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new SoulSteelWingsRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "c", 0, state -> {
            return state.setAndContinue(FOLD_IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
