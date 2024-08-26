package net.coxev.lightroots.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class LightrootCoreBlockEntity extends BlockEntity {

    Boolean active = false;

    public LightrootCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIGHTROOT_CORE_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, LightrootCoreBlockEntity lightrootCoreBlockEntity) {
        if(lightrootCoreBlockEntity.active){
            world.setBlockState(blockPos, blockState.with(Properties.LIT, true));
            if (world.getTime() % 80L == 0L) {
                applyPlayerEffects(world, blockPos);
                playSound(world, blockPos, SoundEvents.BLOCK_BEACON_AMBIENT);
            }
        }
    }

    public static void applyPlayerEffects(World world, BlockPos pos) {
        if (!world.isClient) {
            Box box = (new Box(pos)).expand(50).stretch(0.0, world.getHeight(), 0.0);

            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
            Iterator<PlayerEntity> playerList = list.iterator();
            PlayerEntity playerEntity;

            while(playerList.hasNext()) {
                playerEntity = playerList.next();
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300, 0, true, true));
            }
        }
    }

    public static void playSound(World world, BlockPos pos, SoundEvent sound) {
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
