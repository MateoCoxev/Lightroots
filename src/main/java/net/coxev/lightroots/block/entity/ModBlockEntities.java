package net.coxev.lightroots.block.entity;

import net.coxev.lightroots.Lightroots;
import net.coxev.lightroots.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<LightrootBlockEntity> LIGHTROOT_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Lightroots.MOD_ID, "lightroot_block_entity"),
                    FabricBlockEntityTypeBuilder.create(LightrootBlockEntity::new,
                            ModBlocks.LIGHTROOT).build());
    public static BlockEntityType<LightrootCoreBlockEntity> LIGHTROOT_CORE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Lightroots.MOD_ID, "lightroot_core_block_entity"),
                    FabricBlockEntityTypeBuilder.create(LightrootCoreBlockEntity::new,
                            ModBlocks.LIGHTROOT_CORE).build());

    public static void registerModBlockEntities(){
        Lightroots.LOGGER.info("Registering ModBlockEntities for " + Lightroots.MOD_ID);
    }
}
