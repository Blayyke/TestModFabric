package me.xa5.tmf;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class XBlockWithEntity extends XBlock implements BlockEntityProvider {
    public XBlockWithEntity(Material type, String registryName) {
        super(type, registryName);
    }

    @Override
    public final boolean hasBlockEntity() {
        return true;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState state2, boolean flag) {
        if (state.getBlock() != state2.getBlock()) {
            super.onBlockRemoved(state, world, pos, state2, flag);
            world.removeBlockEntity(pos);
        }
    }

    @Override
    public boolean onBlockAction(BlockState state, World world, BlockPos pos, int int1, int int2) {
        super.onBlockAction(state, world, pos, int1, int2);
        BlockEntity entity = world.getBlockEntity(pos);
        return entity != null && entity.onBlockAction(int1, int2);
    }
}