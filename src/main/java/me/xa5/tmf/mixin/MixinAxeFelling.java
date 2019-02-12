package me.xa5.tmf.mixin;

import me.xa5.tmf.TestModFabric;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(MiningToolItem.class)
public class MixinAxeFelling {
    @Inject(method = "onBlockBroken", at = @At("RETURN"))
    private void onBlockBroken(ItemStack item, World world, BlockState state, BlockPos pos, LivingEntity entity, CallbackInfoReturnable<Boolean> returnable) {
        if (TestModFabric.ENABLE_AXE_MIXIN && entity instanceof PlayerEntity && (Object) this instanceof AxeItem && isWoodType(state)) {
            PlayerEntity player = ((PlayerEntity) entity);

            if (!player.isSneaking() && !player.isCreative() && !world.isClient) {
                List<BlockPos> woodAbove = getWoodAbove(world, pos, state);
                if (!woodAbove.isEmpty()) {
                    int blocksBroken = 0;
                    int toolHealthRemaining = item.getDurability() - item.getDamage();

                    for (BlockPos abovePos : woodAbove) {
                        toolHealthRemaining--;
                        if (toolHealthRemaining < 0) {
                            System.out.println(toolHealthRemaining + ", breaking.");
                            break;
                        }
                        blocksBroken++;
                        world.breakBlock(abovePos, false);
                    }

                    item.applyDamage(blocksBroken, player);
                    Block.dropStack(world, pos, new ItemStack(state.getBlock(), blocksBroken));
                }
            }
        }
    }

    private boolean isWoodType(BlockState state) {
        return BlockTags.LOGS.contains(state.getBlock());
    }

    private List<BlockPos> getWoodAbove(World world, BlockPos pos, BlockState state) {
        List<BlockPos> list = new LinkedList<>();
        BlockPos blockAbove = new BlockPos.Mutable(pos);
        while (world.getBlockState(blockAbove = blockAbove.add(0, 1, 0)).getBlock() == state.getBlock()) {
            list.add(blockAbove);
        }
        return list;
    }
}