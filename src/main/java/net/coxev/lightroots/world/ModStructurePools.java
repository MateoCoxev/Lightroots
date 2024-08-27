package net.coxev.lightroots.world;

import com.google.common.collect.ImmutableList;
import net.coxev.lightroots.Lightroots;
import net.coxev.lightroots.world.gen.LightrootGenerator;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.Identifier;

public class ModStructurePools {
    public static void boostrap(Registerable<StructurePool> structurePoolsRegisterable) {
        LightrootGenerator.bootstrap(structurePoolsRegisterable);
    }
}
