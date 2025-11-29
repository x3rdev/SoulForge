package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.client.renderer.item.SoulScytheRenderer;
import com.github.x3rdev.soul_forge.common.entity.SoulScytheProjectile;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.SoundRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.SimpleTier;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

import java.util.function.Consumer;

public class SoulScythe extends Scythe {

    public static final Tier SOUL_SCYTHE_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1738, 0.7F, 5.5F, 15, () -> Ingredient.of(ItemRegistry.SOUL_STEEL_INGOT.get()));

    public SoulScythe() {
        super(SOUL_SCYTHE_TIER, new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 40);
        }
        if (!pLevel.isClientSide()) {
            SoulScytheProjectile projectile = new SoulScytheProjectile(pLevel, pLivingEntity);
            projectile.setPos(pLivingEntity.getEyePosition().add(0, -0.30F, 0).add(pLivingEntity.getLookAngle().normalize()));
            projectile.shootFromRotation(pLivingEntity, pLivingEntity.getXRot(), pLivingEntity.getYRot(), 0.0F, 1.0F, 0.05F);
            pLevel.addFreshEntity(projectile);
            pLevel.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(), SoundRegistry.SCYTHE_SHOOT, SoundSource.PLAYERS, 1, 1+(pLevel.random.nextFloat()*0.5F));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private SoulScytheRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new SoulScytheRenderer();
                }
                return renderer;
            }
        });
    }
}
