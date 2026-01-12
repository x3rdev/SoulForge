package com.github.x3rdev.soul_forge.common.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;

public class WingItem extends Item implements Equipable {
    public final float acceleration; // meters per tick squared
    private final BiFunction<Player, WingItem, Vec3> behavior;

    public WingItem(Properties properties, float acceleration, BiFunction<Player, WingItem, Vec3> behavior) {
        super(properties);
        this.acceleration = acceleration;
        this.behavior = behavior;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    public float adjustedFallHeight(float velocity) {
        float g = 0.08f;
        float height = velocity * velocity / (2 * g);
        return height * fallMultiplier();
    }

    private float fallMultiplier() {
        if (this.acceleration < 0) return 1;
        double a = 0.25;
        double b = 0.5 * (1 + Math.sqrt(1 + 4 * a));
        return (float) Math.max(a / (this.acceleration + b) - b, 0);
    }

    public Vec3 getBehavior(Player player, WingItem item) {
        return this.behavior.apply(player, item);
    }
}
