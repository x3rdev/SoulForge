package com.github.x3rdev.soul_forge.common.packet;

import com.github.x3rdev.soul_forge.SoulForge;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SendParticlePayload(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SendParticlePayload> TYPE = new SendParticlePayload.Type<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "send_particle"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SendParticlePayload> STREAM_CODEC = StreamCodec.of(
            (buffer, value) -> {
                ParticleTypes.STREAM_CODEC.encode(buffer, value.options());
                buffer.writeDouble(value.x());
                buffer.writeDouble(value.y());
                buffer.writeDouble(value.z());
                buffer.writeDouble(value.xSpeed());
                buffer.writeDouble(value.ySpeed());
                buffer.writeDouble(value.zSpeed());
            },
            buffer -> {
                ParticleOptions options = ParticleTypes.STREAM_CODEC.decode(buffer);
                double x = buffer.readDouble();
                double y = buffer.readDouble();
                double z = buffer.readDouble();
                double xSpeed = buffer.readDouble();
                double ySpeed = buffer.readDouble();
                double zSpeed = buffer.readDouble();
                return new SendParticlePayload(options, x, y, z, xSpeed, ySpeed, zSpeed);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
