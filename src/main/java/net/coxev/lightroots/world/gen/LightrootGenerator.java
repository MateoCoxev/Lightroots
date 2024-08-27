package net.coxev.lightroots.world.gen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;

import java.util.List;

public class LightrootGenerator {
    public static final RegistryKey<StructurePool> BOTTOM = StructurePools.of("lightroot/bottom");
    public static final RegistryKey<StructurePool> MIDDLE = StructurePools.of("lightroot/middle");
    public static final RegistryKey<StructurePool> TOP_1 = StructurePools.of("lightroot/top_1");
    public static final RegistryKey<StructurePool> TOP_2 = StructurePools.of("lightroot/top_2");
    public static final RegistryKey<StructurePool> TOP_3 = StructurePools.of("lightroot/top_3");

    public static void bootstrap(Registerable<StructurePool> poolRegisterable) {
        RegistryEntryLookup<StructurePool> registryEntryLookup = poolRegisterable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry.Reference<StructurePool> registryEntry = registryEntryLookup.getOrThrow(StructurePools.EMPTY);
        poolRegisterable.register(BOTTOM, new StructurePool(registryEntry, List.of(Pair.of(StructurePoolElement.ofLegacySingle("lightroot/bottom_1"), 1), Pair.of(StructurePoolElement.ofLegacySingle("trail_ruins/bottom_2"), 1), Pair.of(StructurePoolElement.ofLegacySingle("trail_ruins/bottom_3"), 1)), StructurePool.Projection.RIGID));
        poolRegisterable.register(MIDDLE, new StructurePool(registryEntry, List.of(Pair.of(StructurePoolElement.ofLegacySingle("lightroot/middle_1"), 1), Pair.of(StructurePoolElement.ofLegacySingle("trail_ruins/middle_2"), 1), Pair.of(StructurePoolElement.ofLegacySingle("trail_ruins/middle_3"), 1)), StructurePool.Projection.RIGID));
        poolRegisterable.register(TOP_1, new StructurePool(registryEntry, List.of(Pair.of(StructurePoolElement.ofLegacySingle("lightroot/top_1"), 1)), StructurePool.Projection.RIGID));
        poolRegisterable.register(TOP_2, new StructurePool(registryEntry, List.of(Pair.of(StructurePoolElement.ofLegacySingle("lightroot/top_2"), 1)), StructurePool.Projection.RIGID));
        poolRegisterable.register(TOP_3, new StructurePool(registryEntry, List.of(Pair.of(StructurePoolElement.ofLegacySingle("lightroot/top_3"), 1)), StructurePool.Projection.RIGID));
    }
}
