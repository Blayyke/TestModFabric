package me.xa5.tmf.blocks;

import me.xa5.tmf.XBlock;
import net.minecraft.block.Material;

public class BlockBlock extends XBlock {
    public BlockBlock(String materialName) {
        super(Material.METAL, materialName + "_block");
    }
}