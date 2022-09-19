package com.awakenedredstone.simpletech;

import com.awakenedredstone.simpletech.blocks.CableBlock;
import com.awakenedredstone.simpletech.blocks.QuarryBlock;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ModBlocks implements BlockRegistryContainer {
    public static final Block QUARRY = new QuarryBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block CABLE = new CableBlock(FabricBlockSettings.of(Material.METAL).strength(1.0f));

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return new BlockItem(block, new Item.Settings().group(SimpleTech.ITEM_GROUP));
    }
}
