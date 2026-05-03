package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.item.Necronomicon;
import com.github.x3rdev.soul_forge.common.recipe.RitualInput;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.registry.*;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.github.x3rdev.soul_forge.common.scheduler.ServerScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.ContainerSingleItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public class RitualAltarBlockEntity extends BlockEntity implements GeoBlockEntity, ContainerSingleItem {

    public static final int RITUAL_DURATION = 300;
    public static final int RITUAL_COMPLETION_CHECK = RITUAL_DURATION - 20;

    public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RITUAL_ANIM = RawAnimation.begin().thenPlay("ritual");

    private final IItemHandler itemHandler = new InvWrapper(this);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private enum PedestalOffset {
        NORTH(Vec3i.ZERO.north(3)),
        NORTH_EAST(Vec3i.ZERO.north(2).east(2)),
        EAST(Vec3i.ZERO.east(3)),
        SOUTH_EAST(Vec3i.ZERO.south(2).east(2)),
        SOUTH(Vec3i.ZERO.south(3)),
        SOUTH_WEST(Vec3i.ZERO.south(2).west(2)),
        WEST(Vec3i.ZERO.west(3)),
        NORTH_WEST(Vec3i.ZERO.north(2).west(2));

        private final Vec3i offset;

        PedestalOffset(Vec3i offset) {
            this.offset = offset;
        }
    }

    private ItemStack item;
    private @Nullable Component pendingIncantation;
    private boolean ritualActive;
    private int ritualTicks;
    private @Nullable Player ritualInitiator;

    public RitualAltarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.RITUAL_ALTAR.get(), pos, state);
        this.item = ItemStack.EMPTY;
        this.pendingIncantation = null;
        this.ritualActive = false;
        this.ritualTicks = 0;
        this.ritualInitiator = null;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, RitualAltarBlockEntity blockEntity) {
        if(blockEntity.isRitualActive()) {
            blockEntity.incrementRitualTicks();
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RitualAltarBlockEntity blockEntity) {
        if(blockEntity.isRitualActive()) {
            blockEntity.incrementRitualTicks();
            blockEntity.spawnCauldronToAltarParticles();
            if(blockEntity.getRitualTicks() == RITUAL_COMPLETION_CHECK) {
                blockEntity.completeRitual();
                return;
            }
            if(blockEntity.getRitualTicks() >= RITUAL_DURATION) {
                blockEntity.stopRitual();
                return;
            }
            if(blockEntity.getRitualTicks() < RITUAL_DURATION && blockEntity.getRitualTicks() % 3 == 0) {
                RitualInput input = blockEntity.buildRitualInput();
                Optional<RecipeHolder<RitualRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.RITUAL.get(), input, level);
                if (!recipe.isPresent()) {
                    level.playSound(null, blockEntity.getBlockPos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS);
                    blockEntity.stopRitual();
                    for (PedestalOffset pedestalOffset : PedestalOffset.values()) {
                        level.getBlockEntity(blockEntity.getBlockPos().offset(pedestalOffset.offset), BlockEntityRegistry.PEDESTAL.get()).ifPresent(PedestalBlockEntity::stopRitual);
                    }
                }
            }

        }
    }

    private void completeRitual() {
        RitualInput input = buildRitualInput();
        Optional<RecipeHolder<RitualRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.RITUAL.get(), input, level);
        if(recipe.isPresent()) {
            Vec3 pos = getBlockPos().getCenter().add(0, 1, 0);
            ItemEntity itemEntity = new ItemEntity(level, pos.x, pos.y, pos.z, recipe.get().value().result());
            level.addFreshEntity(itemEntity);
            this.removeTheItem();
            for (PedestalOffset pedestalOffset : PedestalOffset.values()) {
                level.getBlockEntity(getBlockPos().offset(pedestalOffset.offset), BlockEntityRegistry.PEDESTAL.get()).ifPresent(pedestalBlockEntity -> {
                    pedestalBlockEntity.removeTheItem();
                });
            }
            List<SoulCauldronBlockEntity> surroundingStorages = getSurroundingStorages();
            recipe.get().value().inputSouls().forEach((soulType, integer) -> {
                int i = integer;
                while (i > 0) {
                    for (SoulCauldronBlockEntity blockEntity : surroundingStorages) {
                        if (blockEntity.getSoulType().equals(soulType) && blockEntity.getSoulCount() > 0) {
                            blockEntity.setSoulCount(blockEntity.getSoulCount()-1);
                            i--;
                        }
                    }
                }
            });
        } else {
            SoulForge.LOGGER.warn("ritual ended with no valid recipe");
        }
    }

    @SubscribeEvent
    public static void chatEvent(ServerChatEvent event) {
        event.getPlayer().getData(DataAttachmentRegistry.INCANTATION_RECEIVER).forEach(pos -> {
            RitualAltarBlockEntity blockEntity = (RitualAltarBlockEntity) event.getPlayer().level().getBlockEntity(pos);
            blockEntity.onPlayerChat(event.getPlayer(), event.getMessage());
        });
    }

    public void tryStartRitual(ItemStack necronomiconStack, ServerPlayer player) {
        if(isRitualActive()) {
            level.playSound(null, this.getBlockPos(), SoundEvents.VILLAGER_NO, SoundSource.BLOCKS);
            return;
        }
        if(isRitualSetupValid()) {
            RitualInput input = buildRitualInput();
            Optional<RecipeHolder<RitualRecipe>> recipe = this.level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.RITUAL.get(), input, this.level);
            if(recipe.isPresent()) {
                if(Research.playerHasRitualUnlocked(player, recipe.get())) {
                    addIncantationData(player, this.getBlockPos());
                    ServerScheduler.schedule(() -> deleteIncantationData(player, this.getBlockPos()), 100);
                    if(recipe.get().value().requiredResearch().isPresent()) {
                        ResourceKey<Research> researchResourceKey = recipe.get().value().requiredResearch().get();
                        Registry<Research> registry = level.registryAccess().registry(DatapackRegistry.RESEARCH_KEY).orElseThrow();
                        Optional<Component> incantation = registry.get(researchResourceKey).incantation();
                        if(incantation.isPresent()) {
                            this.pendingIncantation = incantation.get();
                            Necronomicon.whisper(player, Component.literal("Utter your incantation... puny human"));
                        } else {
                            startRitual(player);
                        }
                    }
                } else {
                    level.playSound(null, this.getBlockPos(), SoundEvents.ARMOR_STAND_HIT, SoundSource.BLOCKS);
                    Necronomicon.whisper(player, Component.literal("You're knowledge is... insufficient"));
                }
            } else {
                level.playSound(null, this.getBlockPos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS);
                Necronomicon.whisper(player, Component.literal("These offerings are... inadequate"));
            }
        } else {
            spawnMissingPedestalParticles();
            Necronomicon.whisper(player, Component.literal("Your ritual setup is... unsatisfactory"));
        }
    }

    private static void addIncantationData(ServerPlayer player, BlockPos pos) {
        HashSet<BlockPos> curr = new HashSet<>(player.getData(DataAttachmentRegistry.INCANTATION_RECEIVER));
        curr.add(pos);
        player.setData(DataAttachmentRegistry.INCANTATION_RECEIVER.get(), curr);
    }

    private static void deleteIncantationData(ServerPlayer player, BlockPos pos) {
        if(player != null) {
            HashSet<BlockPos> curr = new HashSet<>(player.getData(DataAttachmentRegistry.INCANTATION_RECEIVER));
            curr.remove(pos);
            player.setData(DataAttachmentRegistry.INCANTATION_RECEIVER.get(), curr);
        }
    }

    private void onPlayerChat(ServerPlayer player, Component message) {
        if(Objects.equals(pendingIncantation.getString(), message.getString())) {
            pendingIncantation = null;
            startRitual(player);
        }
    }

    private boolean isRitualSetupValid() {
        for (PedestalOffset pedestalOffset : PedestalOffset.values()) {
            BlockState state = this.level.getBlockState(this.getBlockPos().offset(pedestalOffset.offset));
            if(!state.is(BlockRegistry.PEDESTAL.get())) {
                return false;
            }
        }
        return true;
    }

    private RitualInput buildRitualInput() {
        return new RitualInput(
                this.getTheItem(),
                List.of(
                        getItemOnOtherPedestal(PedestalOffset.NORTH.offset),
                        getItemOnOtherPedestal(PedestalOffset.EAST.offset),
                        getItemOnOtherPedestal(PedestalOffset.SOUTH.offset),
                        getItemOnOtherPedestal(PedestalOffset.WEST.offset)
                ),
                List.of(
                        getItemOnOtherPedestal(PedestalOffset.NORTH_EAST.offset),
                        getItemOnOtherPedestal(PedestalOffset.SOUTH_EAST.offset),
                        getItemOnOtherPedestal(PedestalOffset.SOUTH_WEST.offset),
                        getItemOnOtherPedestal(PedestalOffset.NORTH_WEST.offset)
                ),
                getAvailableSouls()
        );
    }

    private ItemStack getItemOnOtherPedestal(Vec3i offset) {
        Optional<PedestalBlockEntity> blockEntity = this.level.getBlockEntity(this.getBlockPos().offset(offset), BlockEntityRegistry.PEDESTAL.get());
        if(blockEntity.isPresent()) {
            return blockEntity.get().getTheItem();
        }
        return ItemStack.EMPTY;
    }

    private Map<SoulType, Integer> getAvailableSouls() {
        Map<SoulType, Integer> availableSouls = new EnumMap<>(SoulType.class);
        getSurroundingStorages().forEach(blockEntity -> availableSouls.merge(blockEntity.getSoulType(), blockEntity.getSoulCount(), Integer::sum));
        return availableSouls;
    }

    private List<SoulCauldronBlockEntity> getSurroundingStorages() {
        List<SoulCauldronBlockEntity> list = new ArrayList<>();
        ChunkPos pos = new ChunkPos(getBlockPos());
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                this.level.getChunk(pos.x+i, pos.z+j).getBlockEntities().values().forEach(blockEntity -> {
                    if(blockEntity instanceof SoulCauldronBlockEntity soulStorage) {
                        list.add(soulStorage);
                    }
                });
            }
        }
        return list;
    }

    private void spawnCauldronToAltarParticles() {
        ServerLevel serverLevel = (ServerLevel) level;
        Vec3 altarTop = getBlockPos().getCenter().add(0, 0.5, 0);
        for(SoulCauldronBlockEntity cauldron : getSurroundingStorages()) {
            if(cauldron.getSoulCount() <= 0) continue;
            Vec3 cauldronTop = cauldron.getBlockPos().getCenter().add(0, 0.5, 0);
            Vec3 delta = altarTop.subtract(cauldronTop);
            double t = (ritualTicks * 0.05) % 1.0;
            Vec3 particlePos = cauldronTop.add(delta.scale(t));
            serverLevel.sendParticles(ParticleRegistry.RITUAL_TRAIL.get(),
                    particlePos.x, particlePos.y, particlePos.z,
                    1, 0.05, 0.05, 0.05, 0.01);
        }
    }

    private void spawnMissingPedestalParticles() {
        for (PedestalOffset pedestalOffset : PedestalOffset.values()) {
            BlockPos pos = this.getBlockPos().offset(pedestalOffset.offset);
            BlockState state = this.level.getBlockState(pos);
            if(!state.is(BlockRegistry.PEDESTAL.get())) {
                ((ServerLevel) this.level).sendParticles(ParticleTypes.SMOKE,
                        pos.getCenter().x, pos.getCenter().y, pos.getCenter().z,
                        10, 0, 0, 0, 0.05);
            }
        }
    }

    public void startRitual(Player player) {
        this.ritualActive = true;
        this.triggerAnim("c", "ritual");
        level.playSound(null, getBlockPos(), SoundRegistry.RITUAL.get(), SoundSource.BLOCKS);
        for (PedestalOffset pedestalOffset : PedestalOffset.values()) {
            Optional<PedestalBlockEntity> blockEntity = level.getBlockEntity(getBlockPos().offset(pedestalOffset.offset), BlockEntityRegistry.PEDESTAL.get());
            blockEntity.orElseThrow().startRitual(getBlockPos());
        }
        this.ritualInitiator = player;
        this.setChanged();
    }

    public void stopRitual() {
        this.stopTriggeredAnim("c", "ritual");
        this.ritualActive = false;
        this.ritualTicks = 0;
        this.ritualInitiator = null;
        this.setChanged();
    }

    public boolean isRitualActive() {
        return this.ritualActive;
    }

    public int getRitualTicks() {
        return this.ritualTicks;
    }

    public void incrementRitualTicks() {
        ritualTicks++;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.get("item") != null) {
            this.item = ItemStack.parse(registries, tag.get("item")).orElseThrow();
        } else {
            this.item = ItemStack.EMPTY;
        }
        if(tag.get("ritualActive") != null) {
            this.ritualActive = tag.getBoolean("ritualActive");
        }
        if(tag.get("ritualTicks") != null) {
            this.ritualTicks = tag.getInt("ritualTicks");
        }
        if(tag.get("ritualInitiator") != null) {
            this.ritualInitiator = this.level.getPlayerByUUID(tag.getUUID("ritualInitiator"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(!item.isEmpty()) {
            tag.put("item", item.save(registries));
        }
        tag.putBoolean("ritualActive", ritualActive);
        tag.putInt("ritualTicks", ritualTicks);
        if(ritualInitiator != null) {
            tag.putUUID("ritualInitiator", ritualInitiator.getUUID());
        }
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public ItemStack getTheItem() {
        return this.item;
    }

    @Override
    public void setTheItem(ItemStack item) {
        this.item = item;
        this.setChanged();
    }

    @Override
    public ItemStack removeTheItem() {
        ItemStack copy = getTheItem().copyAndClear();
        setChanged();
        return copy;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.getItem(slot).isEmpty() && stack.getCount() == 1;
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return !isRitualActive();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    //client code start

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "c", 0, this::controller)
                .triggerableAnim("ritual", RITUAL_ANIM));
    }

    protected <E extends RitualAltarBlockEntity> PlayState controller(final AnimationState<E> event) {
        return event.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
