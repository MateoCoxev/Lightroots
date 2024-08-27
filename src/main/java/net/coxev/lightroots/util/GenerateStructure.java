package net.coxev.lightroots.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;

import java.util.Optional;

public class GenerateStructure {
    public static boolean generateStructure(StructureWorldAccess originalWorld, RegistryKey<World> targetWorldKey, RegistryKey<Structure> structure, BlockPos pos){
        if(originalWorld.isClient())return false;
        Optional<RegistryEntry.Reference<Structure>> structureReference = originalWorld.getRegistryManager().get(RegistryKeys.STRUCTURE).getEntry(structure);
        if(structureReference.isEmpty()) return false;
        ServerWorld serverWorld = originalWorld.getServer().getWorld(targetWorldKey);
        StructureStart structureStart = structureStart(originalWorld,  targetWorldKey, structure, pos);
        if(structureStart == null)return false;
        BlockBox blockBox = structureStart.getBoundingBox();
        ChunkPos chunkPos = new ChunkPos(ChunkSectionPos.getSectionCoord(blockBox.getMinX()), ChunkSectionPos.getSectionCoord(blockBox.getMinZ()));
        ChunkPos chunkPos2 = new ChunkPos(ChunkSectionPos.getSectionCoord(blockBox.getMaxX()), ChunkSectionPos.getSectionCoord(blockBox.getMaxZ()));
        ChunkPos[] chunkPosArray =  ChunkPos.stream(chunkPos, chunkPos2).toArray(ChunkPos[]::new);
        return  generate(serverWorld, structureStart, chunkPosArray);
    }

    public static StructureStart structureStart (StructureWorldAccess  originalWorld, RegistryKey<World> targetWorldKey, RegistryKey<Structure> structureKey, BlockPos pos){
        if(originalWorld.isClient())return null;
        RegistryEntry.Reference<Structure> structure = originalWorld.getRegistryManager().get(RegistryKeys.STRUCTURE).getEntry(structureKey).get();
        ServerWorld serverWorld = originalWorld.getServer().getWorld(targetWorldKey);
        ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
        Structure structure2 = structure.value();
        StructureStart structureStart = structure2.createStructureStart(
                originalWorld.getRegistryManager(),
                chunkGenerator,
                chunkGenerator.getBiomeSource(),
                serverWorld.getChunkManager().getNoiseConfig(),
                serverWorld.getStructureTemplateManager(),
                originalWorld.getServer().getWorld(World.OVERWORLD).getSeed(),
                new ChunkPos(pos),
                0,
                serverWorld,
                biome -> true
        );
        return structureStart;
    }

    private static boolean generate(ServerWorld serverWorld, StructureStart structureStart, ChunkPos[] chunkPosArray){
        ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
        if(!structureStart.hasChildren()){
            return false;
        }else {
            for (ChunkPos chunkPosx : chunkPosArray) {
                structureStart.place(
                        serverWorld,
                        serverWorld.getStructureAccessor(),
                        chunkGenerator,
                        serverWorld.getRandom(),
                        new BlockBox(chunkPosx.getStartX(), serverWorld.getBottomY(), chunkPosx.getStartZ(), chunkPosx.getEndX(), serverWorld.getTopY(), chunkPosx.getEndZ()),
                        chunkPosx
                );
            }
            unloadNearbyChunks(chunkPosArray, serverWorld);
            return true;
        }
    }
    public static void forceLoadNearbyChunks(ChunkPos[] chunkPosArray, ServerWorld world) {
        for (ChunkPos chunkPos : chunkPosArray) {
            world.setChunkForced(chunkPos.x, chunkPos.z, true);
        }
    }
    public static void unloadNearbyChunks(ChunkPos[] chunkPosArray, ServerWorld world) {
        for (ChunkPos chunkPos : chunkPosArray) {
            world.setChunkForced(chunkPos.x, chunkPos.z, false);
        }
    }
}
