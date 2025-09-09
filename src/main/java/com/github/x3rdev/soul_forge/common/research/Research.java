package com.github.x3rdev.soul_forge.common.research;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.packet.SendResearchDataPayload;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.registry.DataAttachmentRegistry;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public record Research(ResourceKey<Research> parent, String title, String description, ItemStack iconItemStack, Ingredient unlockIngredient, boolean inactive) {

    private static Set<Holder.Reference<Research>> cachedUnlockableResearch;

    public static final ResourceKey<Research> EMPTY_RESOURCE_KEY = ResourceKey.create(DatapackRegistry.RESEARCH_KEY, ResourceLocation.withDefaultNamespace("empty"));
    public static final ResourceKey<Research> HEAD_RESOURCE_KEY = ResourceKey.create(DatapackRegistry.RESEARCH_KEY, ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "necronomicon"));

    public static final Codec<Research> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(DatapackRegistry.RESEARCH_KEY).fieldOf("parent").orElse(EMPTY_RESOURCE_KEY).forGetter(Research::parent),
                    Codec.STRING.fieldOf("title").forGetter(Research::title),
                    Codec.STRING.fieldOf("description").forGetter(Research::description),
                    ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("icon_item_stack").orElse(ItemStack.EMPTY).forGetter(Research::iconItemStack),
                    Ingredient.CODEC.fieldOf("unlock_ingredient").orElse(Ingredient.EMPTY).forGetter(Research::unlockIngredient),
                    Codec.BOOL.fieldOf("inactive").orElse(false).forGetter(Research::inactive)
            ).apply(instance, Research::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Research> STREAM_CODEC = StreamCodec.of(
            (buffer, value) -> {
                ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY).encode(buffer, value.parent);
                ByteBufCodecs.STRING_UTF8.encode(buffer, value.title);
                ByteBufCodecs.STRING_UTF8.encode(buffer, value.description);
                ItemStack.STREAM_CODEC.encode(buffer, value.iconItemStack);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.unlockIngredient);
                ByteBufCodecs.BOOL.encode(buffer, value.inactive);
            },
            buffer -> {
                ResourceKey<Research> decode = ResourceKey.streamCodec(DatapackRegistry.RESEARCH_KEY).decode(buffer);
                String title = ByteBufCodecs.STRING_UTF8.decode(buffer);
                String description = ByteBufCodecs.STRING_UTF8.decode(buffer);
                ItemStack iconItemStack = ItemStack.STREAM_CODEC.decode(buffer);
                Ingredient unlockIngredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
                boolean inactive = ByteBufCodecs.BOOL.decode(buffer);
                return new Research(decode, title, description, iconItemStack, unlockIngredient, inactive);
            }
    );


    public static Holder.Reference<Research> getEmptyResearch(RegistryAccess access) {
        return access.holder(EMPTY_RESOURCE_KEY).orElseThrow();
    }

    public static Holder.Reference<Research> getHeadResearch(RegistryAccess access) {
        return access.holder(HEAD_RESOURCE_KEY).orElseThrow();
    }

    public static void grantResearchToPlayer(ServerPlayer player, Holder.Reference<Research> research) {
        List<ResourceKey<Research>> currentKeys = player.getData(DataAttachmentRegistry.UNLOCKED_RESEARCH.get());
        ResourceKey<Research> newKey = research.key();
        if(!currentKeys.contains(newKey)) {
            ImmutableList<ResourceKey<Research>> newKeys = ImmutableList.<ResourceKey<Research>>builder().addAll(currentKeys).add(newKey).build();
            player.setData(DataAttachmentRegistry.UNLOCKED_RESEARCH.get(), newKeys);
            PacketDistributor.sendToPlayer(player, new SendResearchDataPayload(newKeys));

            player.sendSystemMessage(Component.translatable("research.soul_forge.unlock" + newKey));
        }
    }

    public static void clearResearchFromPlayer(ServerPlayer player) {
        ImmutableList<ResourceKey<Research>> newKeys = ImmutableList.of();
        player.setData(DataAttachmentRegistry.UNLOCKED_RESEARCH.get(), newKeys);
        PacketDistributor.sendToPlayer(player, new SendResearchDataPayload(newKeys));
    }

    public static boolean playerHasResearchUnlocked(Player player, Holder.Reference<Research> research) {
        if(research.value().inactive()) {
            return true;
        }
        return player.getData(DataAttachmentRegistry.UNLOCKED_RESEARCH.get()).contains(research.key());
    }

    public static boolean playerHasRitualUnlocked(Player player, RecipeHolder<RitualRecipe> recipe) {
        Optional<ResourceKey<Research>> optionalResourceKey = recipe.value().requiredResearch();
        if(optionalResourceKey.isPresent()) {
            Holder.Reference<Research> researchReference = player.registryAccess().lookup(DatapackRegistry.RESEARCH_KEY).orElseThrow().get(optionalResourceKey.get()).orElseThrow();
            return playerHasResearchUnlocked(player, researchReference);
        }
        return true; //No required research = ritual unlocked by default
    }

    public static boolean isItemUsedToUnlockNextResearch(ItemStack stack, Player player) {
        return Research.getCachedUnlockableResearch(player).stream().anyMatch(
                researchReference -> researchReference.value().unlockIngredient().test(stack));
    }

    public static Set<Holder.Reference<Research>> getCachedUnlockableResearch(Player player) {
        if(cachedUnlockableResearch == null) {
            cachedUnlockableResearch = getUnlockableResearch(player);
        }
        return cachedUnlockableResearch;
    }

    public static void clearCache() {
        cachedUnlockableResearch = null;
    }

    public static Set<Holder.Reference<Research>> getUnlockableResearch(Player player) {
        Set<Holder.Reference<Research>> result = new HashSet<>();
        player.registryAccess().lookup(DatapackRegistry.RESEARCH_KEY).orElseThrow()
                .listElements()
                .filter(researchReference -> !researchReference.value().inactive())
                .forEach(researchReference -> {
                    if(!playerHasResearchUnlocked(player, researchReference) && playerHasResearchUnlocked(player, researchReference.value().getParent(player.registryAccess()))) {
                        result.add(researchReference);
                    }
                });
        return result;
    }

    public Holder.Reference<Research> getParent(RegistryAccess access) {
        if(parent == null) {
            return getEmptyResearch(access);
        }
        return access.holder(parent).orElseThrow();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj; // Evaluate reference
    }


}
