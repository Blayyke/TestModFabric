package me.xa5.tmf.items;

import me.xa5.tmf.TMFItems;
import me.xa5.tmf.TestModFabric;
import me.xa5.tmf.XItem;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemFiredClayBucket extends XItem {
    private final Fluid fluid;

    public ItemFiredClayBucket(Fluid fluid) {
        super("fired_clay_bucket", new Item.Settings().itemGroup(TestModFabric.getInstance().getModGroup()).stackSize(16).rarity(Rarity.COMMON));
        this.fluid = fluid;
    }

    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        ItemStack itemInHand = playerEntity_1.getStackInHand(hand_1);
        HitResult hitResult = getHitResult(world_1, playerEntity_1, this.fluid == Fluids.EMPTY ? RayTraceContext.FluidHandling.SOURCE_ONLY : RayTraceContext.FluidHandling.NONE);
        if (hitResult.getType() == HitResult.Type.NONE) {
            return new TypedActionResult<>(ActionResult.PASS, itemInHand);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos hitPos = blockHitResult.getBlockPos();
            if (world_1.canPlayerModifyAt(playerEntity_1, hitPos) && playerEntity_1.canPlaceBlock(hitPos, blockHitResult.getSide(), itemInHand)) {
                BlockState hitBlockState;
                if (this.fluid == Fluids.EMPTY) {
                    // Bucket is empty

                    hitBlockState = world_1.getBlockState(hitPos);
                    if (hitBlockState.getBlock() == Blocks.WATER) {
                        WaterFluid waterFluid = (WaterFluid) ((FluidDrainable) hitBlockState.getBlock()).tryDrainFluid(world_1, hitPos, hitBlockState);

                        if (waterFluid != Fluids.EMPTY) {
                            playerEntity_1.incrementStat(Stats.USED.getOrCreateStat(this));
                            playerEntity_1.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                            ItemStack itemStack_2 = this.getFilledStack(itemInHand, playerEntity_1, TMFItems.getInstance().firedClayBucket);
                            if (!world_1.isClient) {
                                Criterions.FILLED_BUCKET.method_8932((ServerPlayerEntity) playerEntity_1, new ItemStack(TMFItems.getInstance().firedClayBucket));
                            }

                            return new TypedActionResult<>(ActionResult.SUCCESS, itemStack_2);
                        }
                    }

                    return new TypedActionResult<>(ActionResult.FAILURE, itemInHand);
                } else {
                    hitBlockState = world_1.getBlockState(hitPos);
                    BlockPos fluidPos = hitBlockState.getBlock() instanceof FluidFillable ? hitPos : blockHitResult.getBlockPos().offset(blockHitResult.getSide());
                    if (this.placeFluid(playerEntity_1, world_1, fluidPos, blockHitResult)) {
                        this.onEmptied(world_1, itemInHand, fluidPos);
                        if (playerEntity_1 instanceof ServerPlayerEntity) {
                            Criterions.PLACED_BLOCK.handle((ServerPlayerEntity) playerEntity_1, fluidPos, itemInHand);
                        }

                        playerEntity_1.incrementStat(Stats.USED.getOrCreateStat(this));
                        return new TypedActionResult<>(ActionResult.SUCCESS, this.getEmptiedStack(itemInHand, playerEntity_1));
                    } else {
                        return new TypedActionResult<>(ActionResult.FAILURE, itemInHand);
                    }
                }
            } else {
                return new TypedActionResult<>(ActionResult.FAILURE, itemInHand);
            }
        } else {
            return new TypedActionResult<>(ActionResult.PASS, itemInHand);
        }
    }

    protected ItemStack getEmptiedStack(ItemStack itemStack_1, PlayerEntity playerEntity_1) {
        return !playerEntity_1.abilities.creativeMode ? new ItemStack(TMFItems.getInstance().firedClayBucket) : itemStack_1;
    }

    public void onEmptied(World world_1, ItemStack itemStack_1, BlockPos blockPos_1) {
    }

    private ItemStack getFilledStack(ItemStack itemStack_1, PlayerEntity playerEntity_1, Item item_1) {
        if (playerEntity_1.abilities.creativeMode) {
            return itemStack_1;
        } else {
            itemStack_1.subtractAmount(1);
            if (itemStack_1.isEmpty()) {
                return new ItemStack(item_1);
            } else {
                if (!playerEntity_1.inventory.insertStack(new ItemStack(item_1))) {
                    playerEntity_1.dropItem(new ItemStack(item_1), false);
                }

                return itemStack_1;
            }
        }
    }

    public boolean placeFluid(@Nullable PlayerEntity playerEntity_1, World world_1, BlockPos posToPlaceAt, @Nullable BlockHitResult blockHitResult_1) {
        if (this.fluid == null) {
            return false;
        } else {
            BlockState stateAtPos = world_1.getBlockState(posToPlaceAt);
            Material posMaterial = stateAtPos.getMaterial();
            boolean flag = !posMaterial.method_15799();
            boolean isBlockAtPosReplaceable = posMaterial.isReplaceable();
            if (flag || isBlockAtPosReplaceable || stateAtPos.getBlock() instanceof FluidFillable && ((FluidFillable) stateAtPos.getBlock()).canFillWithFluid(world_1, posToPlaceAt, stateAtPos, this.fluid)) {
                if (world_1.dimension.doesWaterVaporize() && this.fluid.matches(FluidTags.WATER)) {
                    int x = posToPlaceAt.getX();
                    int y = posToPlaceAt.getY();
                    int z = posToPlaceAt.getZ();
                    world_1.playSound(playerEntity_1, posToPlaceAt, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCK, 0.5F, 2.6F + (world_1.random.nextFloat() - world_1.random.nextFloat()) * 0.8F);

                    for (int int_4 = 0; int_4 < 8; ++int_4) {
                        world_1.addParticle(ParticleTypes.LARGE_SMOKE, (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else if (stateAtPos.getBlock() instanceof FluidFillable) {
                    if (((FluidFillable) stateAtPos.getBlock()).tryFillWithFluid(world_1, posToPlaceAt, stateAtPos, ((BaseFluid) this.fluid).getState(false))) {
                        this.playEmptyingSound(playerEntity_1, world_1, posToPlaceAt);
                    }
                } else {
                    if (!world_1.isClient && (flag || isBlockAtPosReplaceable) && !posMaterial.isLiquid()) {
                        world_1.breakBlock(posToPlaceAt, true);
                    }

                    this.playEmptyingSound(playerEntity_1, world_1, posToPlaceAt);
                    world_1.setBlockState(posToPlaceAt, this.fluid.getDefaultState().getBlockState(), 11);
                }

                return true;
            } else {
                return blockHitResult_1 == null ? false : this.placeFluid(playerEntity_1, world_1, blockHitResult_1.getBlockPos().offset(blockHitResult_1.getSide()), (BlockHitResult) null);
            }
        }
    }

    protected void playEmptyingSound(@Nullable PlayerEntity playerEntity_1, IWorld iWorld_1, BlockPos blockPos_1) {
        iWorld_1.playSound(playerEntity_1, blockPos_1, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCK, 1.0F, 1.0F);
    }
}
