package com.github.x3rdev.soul_forge.common.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FilledIngredientListCodec implements Codec<List<Ingredient>> {

    private final int size;

    public FilledIngredientListCodec(int size) {
        this.size = size;
    }

    private <R> DataResult<R> createTooLongError(final int size) {
        return DataResult.error(() -> "List is too long: " + size + ", expected range [" + 0 + "-" + size + "]");
    }

    @Override
    public <T> DataResult<Pair<List<Ingredient>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap((stream) -> {
            final DecoderState<T> decoder = new DecoderState(ops);
            stream.accept(decoder::accept);
            return decoder.build();
        });
    }

    @Override
    public <T> DataResult<T> encode(List<Ingredient> input, DynamicOps<T> ops, T prefix) {
        ListBuilder<T> builder = ops.listBuilder();
        if(input.size() > size) {
            return createTooLongError(input.size());
        }
        for (int i = 0; i < input.size(); i++) {
            builder.add(Ingredient.CODEC.encodeStart(ops, input.get(i)));
        }
        for (int i = 0; i < input.size() - size; i++) {
            builder.add(Ingredient.CODEC.encodeStart(ops, Ingredient.EMPTY));
        }
        return builder.build(prefix);
    }

    private class DecoderState<T> {
        private static final DataResult<Unit> INITIAL_RESULT = DataResult.success(Unit.INSTANCE, Lifecycle.stable());

        private final DynamicOps<T> ops;
        private final List<Ingredient> elements = new ArrayList<>();
        private final Stream.Builder<T> failed = Stream.builder();
        private DataResult<Unit> result = INITIAL_RESULT;
        private int totalCount;

        private DecoderState(final DynamicOps<T> ops) {
            this.ops = ops;
        }

        public void accept(final T value) {
            totalCount++;
            if (elements.size() >= size) {
                failed.add(value);
                return;
            }
            final DataResult<Pair<Ingredient, T>> elementResult = Ingredient.CODEC.decode(ops, value);
            elementResult.error().ifPresent(error -> failed.add(value));
            elementResult.resultOrPartial().ifPresent(pair -> elements.add(pair.getFirst()));
            result = result.apply2stable((result, element) -> result, elementResult);
        }

        public DataResult<Pair<List<Ingredient>, T>> build() {
            final T errors = ops.createList(failed.build());
            final Pair<List<Ingredient>, T> pair = Pair.of(List.copyOf(elements), errors);
            if (totalCount > size) {
                result = createTooLongError(totalCount);
            }
            return result.map(ignored -> pair).setPartial(pair);
        }
    }
}
