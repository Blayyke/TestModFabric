package me.xa5.tmf.blocks;

import me.xa5.tmf.XBlock;
import me.xa5.tmf.entity.EntityAntimatter;
import me.xa5.tmf.explosive.ExplosiveEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public abstract class ExplosiveBlock extends XBlock {
    public ExplosiveBlock(Material type, String registryName) {
        super(type, registryName);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState state2) {
        if (state2.getBlock() != state.getBlock()) {
            if (world.isReceivingRedstonePower(pos)) {
                primeExplosive(world, pos);
                world.clearBlockState(pos);
            }
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbourPos) {
        if (world.isReceivingRedstonePower(pos)) {
            primeExplosive(world, pos);
            world.clearBlockState(pos);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && !player.isCreative()) {
            primeExplosive(world, pos);
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient) {
            ExplosiveEntity entity = createEntity(world, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosion.getCausingEntity());
            entity.setFuse((short) (world.random.nextInt(entity.getFuseTimer() / 4) + entity.getFuseTimer() / 8));
            world.spawnEntity(entity);
        }
    }

    private void primeExplosive(World world, BlockPos pos) {
        primeExplosive(world, pos, null);
    }

    private void primeExplosive(World world, BlockPos pos, LivingEntity livingEntity) {
        if (!world.isClient) {
            ExplosiveEntity explosiveEntity = createEntity(world, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), livingEntity);
            world.spawnEntity(explosiveEntity);
            world.playSound(null, explosiveEntity.x, explosiveEntity.y, explosiveEntity.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCK, 1.0F, 1.0F);
        }
    }

    protected abstract ExplosiveEntity createEntity(World world, double x, double y, double z, LivingEntity livingEntity);

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        ItemStack stackInHand = player.getStackInHand(hand);
        Item itemInHand = stackInHand.getItem();
        if (itemInHand != Items.FLINT_AND_STEEL && itemInHand != Items.FIRE_CHARGE) {
            return super.activate(state, world, pos, player, hand, hitResult);
        } else {
            primeExplosive(world, pos, player);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            if (itemInHand == Items.FLINT_AND_STEEL) {
                stackInHand.applyDamage(1, player);
            } else {
                stackInHand.subtractAmount(1);
            }
            return true;
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity instanceof ProjectileEntity) {
            ProjectileEntity projectile = (ProjectileEntity) entity;
            Entity projectileOwner = projectile.getOwner();
            if (projectile.isOnFire()) {
                primeExplosive(world, pos, projectileOwner instanceof LivingEntity ? (LivingEntity) projectileOwner : null);
                world.clearBlockState(pos);
            }
        }
    }

    @Override
    public final boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }
}