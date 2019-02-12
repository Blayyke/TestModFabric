package me.xa5.tmf.blocks.explosive;

import me.xa5.tmf.blocks.ExplosiveBlock;
import me.xa5.tmf.entity.EntityRegen;
import me.xa5.tmf.explosive.ExplosiveEntity;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class BlockRegenExplosive extends ExplosiveBlock {
    public BlockRegenExplosive() {
        super(Material.TNT, "regen_explosive");
    }

    @Override
    protected ExplosiveEntity createEntity(World world, double x, double y, double z, LivingEntity livingEntity) {
        return new EntityRegen(world, x, y, z, livingEntity);
    }
}