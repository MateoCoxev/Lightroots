package net.coxev.lightroots.block;

import net.coxev.lightroots.Lightroots;
import net.coxev.lightroots.block.custom.LightrootBlock;
import net.coxev.lightroots.block.custom.LightrootCoreBlock;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModBlocks {

    public static final Block LIGHTROOT = registerBlock("lightroot", new LightrootBlock());
    public static final Block LIGHTROOT_CORE = registerBlock("lightroot_core", new LightrootCoreBlock());

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Lightroots.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, Identifier.of(Lightroots.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks(){
        Lightroots.LOGGER.info("Registering ModBlocks for " + Lightroots.MOD_ID);
    }
}
