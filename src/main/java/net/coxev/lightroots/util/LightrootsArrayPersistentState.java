package net.coxev.lightroots.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LightrootsArrayPersistentState extends PersistentState {

    public List<Long> lightroots_array = new ArrayList<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putLongArray("lightroots_array", lightroots_array);
        return nbt;
    }

    public static LightrootsArrayPersistentState fromNbt(NbtCompound tag) {
        LightrootsArrayPersistentState state = new LightrootsArrayPersistentState();
        state.lightroots_array = Arrays.stream(tag.getLongArray("lightroots_array")).boxed().toList();
        return state;
    }
}
