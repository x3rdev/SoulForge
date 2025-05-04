package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ObservationItem extends Item {

    public static final ResourceLocation EMPTY_OBSERVATION_ID = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "empty");

    public ObservationItem(Properties properties) {
        super(properties
                .component(DataComponentRegistry.OBSERVATION_ID.get(), EMPTY_OBSERVATION_ID));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal(stack.get(DataComponentRegistry.OBSERVATION_ID.get()).toString()));
    }

    public record Observation(String type, ResourceLocation id) {
        public static final Codec<Observation> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("type").forGetter(Observation::type),
                        ResourceLocation.CODEC.fieldOf("id").forGetter(Observation::id)
                ).apply(instance, Observation::new)
        );

//        public static final StreamCodec<ByteBuf, Observation> STREAM_CODEC = StreamCodec.of(
//                (buffer, observation) -> {
//                    ByteBufCodecs.STRING_UTF8.encode(buffer, observation.type);
//                    ResourceLocation.STREAM_CODEC.encode(buffer, observation.id);
//                },
//                buffer -> new Observation(
//                        ByteBufCodecs.STRING_UTF8.decode(buffer),
//                        ResourceLocation.STREAM_CODEC.decode(buffer)
//                )
//        );
    }
}
