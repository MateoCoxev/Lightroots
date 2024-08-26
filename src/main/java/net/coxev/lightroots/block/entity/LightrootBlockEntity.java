package net.coxev.lightroots.block.entity;

import net.coxev.lightroots.Lightroots;
import net.coxev.lightroots.block.ModBlocks;
import net.coxev.lightroots.block.custom.LightrootBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class LightrootBlockEntity extends BlockEntity {

    Boolean active = false;

    public LightrootBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIGHTROOT_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, LightrootBlockEntity lightrootBlockEntity) {
        if(!blockState.get(Properties.LIT)){
            if (world.getTime() % 80L == 0L) {
                Box box = (new Box(blockPos)).expand(1).stretch(0.0, world.getHeight(), 0.0);

                Stream<BlockState> stream = world.getStatesInBox(box);
                Iterator<BlockState> blockStateList = stream.iterator();

                while(blockStateList.hasNext()) {
                    BlockState blockState1 = blockStateList.next();
                    if(blockState1.isOf(ModBlocks.LIGHTROOT) || blockState1.isOf(ModBlocks.LIGHTROOT_CORE)) {
                        if(blockState1.get(Properties.LIT)){
                            world.setBlockState(blockPos, blockState.with(Properties.LIT, true));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.active = nbt.getBoolean("Active");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("Active", this.active);
    }

    public boolean isActive(){
        return this.active;
    }

    public boolean switchActive(){
        return this.active = !this.active;
    }
}
