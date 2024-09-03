package net.coxev.lightroots.world.feature;

import net.coxev.lightroots.util.ElegantPairing;
import net.coxev.lightroots.util.LightrootsArrayPersistentState;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class LightrootHelper {
    public LightrootHelper() {
    }

    public static boolean canGenerateBase(StructureWorldAccess world, BlockPos pos) {
        return isBelowY(pos) && isOnSurface(world, pos) && isFarFromOthers(world, pos);
    }

    public static boolean isBelowY(BlockPos pos){
        return pos.getY() > -30;
    }

    public static boolean isOnSurface(StructureWorldAccess world, BlockPos pos){
        boolean allPassed = true;
        for (BlockPos blockPos : BlockPos.iterateOutwards(pos, 1, 0, 1)){
            if(!canGenerate(world, blockPos)){
                allPassed = false;
            }
        }
        return allPassed;
    }

    public static boolean isFarFromOthers(StructureWorldAccess world, BlockPos pos){
        LightrootsArrayPersistentState persistentState = world.toServerWorld().getPersistentStateManager().getOrCreate(LightrootsArrayPersistentState::fromNbt, LightrootsArrayPersistentState::new, "lightroots_array");
        List<Long> array = persistentState.lightroots_array;
        for(long lightrootPos : array){
            int[] lightrootCoordinates = ElegantPairing.unpair(lightrootPos);
            int x1 = pos.getX();
            int z1 = pos.getZ();
            int x2 = lightrootCoordinates[0];
            int z2 = lightrootCoordinates[1];
            double distance = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((z2 - z1), 2));
            if(Math.floor(distance) < 100){
                return false;
            }
        }
        return true;
    }

    public static boolean canGenerate(WorldAccess world, BlockPos pos) {
        return world.testBlockState(pos, LightrootHelper::isAir) && world.testBlockState(pos.down(), LightrootHelper::isGround);
    }

    public static boolean canReplaceOrLava(BlockState state) {
        return canReplace(state) || state.isOf(Blocks.LAVA);
    }

    public static boolean canReplace(BlockState state) {
        return state.isOf(Blocks.DRIPSTONE_BLOCK) || state.isIn(BlockTags.DRIPSTONE_REPLACEABLE_BLOCKS);
    }

    public static boolean isAir(BlockState state) {
        return state.isAir();
    }

    public static boolean isGround(BlockState state) {
        return !state.isAir() && !(state.isOf(Blocks.LAVA) || state.isOf(Blocks.WATER));
    }
}
