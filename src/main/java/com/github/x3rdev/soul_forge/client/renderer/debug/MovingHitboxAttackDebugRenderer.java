package com.github.x3rdev.soul_forge.client.renderer.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.AABB;

public class MovingHitboxAttackDebugRenderer {

    public static void renderAttackHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, AABB box) {
        LevelRenderer.renderLineBox(poseStack, vertexConsumer, box, 1,1,1,1);
    }

}
