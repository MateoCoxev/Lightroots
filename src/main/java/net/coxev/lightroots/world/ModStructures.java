package net.coxev.lightroots.world;

import com.mojang.serialization.Lifecycle;
import net.coxev.lightroots.Lightroots;
import net.coxev.lightroots.world.gen.LightrootGenerator;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.structure.PlainsVillageData;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.minecraft.world.gen.structure.Structures;

import java.util.List;
import java.util.Map;

public class ModStructures {

    public static final RegistryKey<Structure> LIGHTROOT = registerKey("lightroot");
    public static void boostrap(Registerable<Structure> context) {
        RegistryEntryLookup<Biome> registryEntryLookup = context.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<StructurePool> registryEntryLookup2 = context.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);

        register(context, LIGHTROOT, new JigsawStructure(ModStructures.createConfig(registryEntryLookup.getOrThrow(BiomeTags.IS_OVERWORLD), Map.of(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, StructureTerrainAdaptation.BEARD_THIN), registryEntryLookup2.getOrThrow(LightrootGenerator.BOTTOM), 2, UniformHeightProvider.create(YOffset.fixed(-60), YOffset.fixed(-30)), false, Heightmap.Type.WORLD_SURFACE_WG));
    }

    public static RegistryKey<Structure> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(Lightroots.MOD_ID, name));
    }

    private static <T> void register(Registerable<Structure> context, RegistryKey<Structure> key, T value) {
        context.register(key, (Structure) value);
    }

    private static Structure.Config createConfig(RegistryEntryList<Biome> biomes, Map<SpawnGroup, StructureSpawns> spawns, GenerationStep.Feature featureStep, StructureTerrainAdaptation terrainAdaptation) {
        return new Structure.Config(biomes, spawns, featureStep, terrainAdaptation);
    }
}
