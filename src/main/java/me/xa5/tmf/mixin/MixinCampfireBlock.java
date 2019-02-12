package me.xa5.tmf.mixin;

import me.xa5.tmf.TestModFabric;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.CampfireBlock.*;

@Mixin(CampfireBlock.class)
public abstract class MixinCampfireBlock extends BlockWithEntity {
    protected MixinCampfireBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Shadow
    protected abstract boolean doesBlockCauseSignalFire(BlockState state);

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    public void getPlacementState(ItemPlacementContext itemPlacementContext_1, CallbackInfoReturnable<BlockState> infoReturnable) {
        if (TestModFabric.ENABLE_CAMPFIRE_MIXIN) {
            IWorld iWorld_1 = itemPlacementContext_1.getWorld();
            BlockPos blockPos_1 = itemPlacementContext_1.getBlockPos();
            boolean boolean_1 = iWorld_1.getFluidState(blockPos_1).getFluid() == Fluids.WATER;
            infoReturnable.setReturnValue(this.getDefaultState().with(WATERLOGGED, boolean_1).with(SIGNAL_FIRE, this.doesBlockCauseSignalFire(iWorld_1.getBlockState(blockPos_1.down()))).with(LIT, false).with(FACING, itemPlacementContext_1.getPlayerHorizontalFacing()));
        }
    }
}