package me.xa5.tmf.blocks;

import me.xa5.tmf.XBlock;
import net.minecraft.block.Material;

public class BlockOre extends XBlock {
    public BlockOre(String materialName) {
        super(Material.METAL, materialName + "_ore");
//        FabricItemTags.PICKAXES -- keep in mind this class
    }
}