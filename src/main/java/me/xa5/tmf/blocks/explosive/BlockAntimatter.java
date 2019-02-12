package me.xa5.tmf.blocks.explosive;

import me.xa5.tmf.blocks.ExplosiveBlock;
import me.xa5.tmf.entity.EntityAntimatter;
import me.xa5.tmf.explosive.ExplosiveEntity;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class BlockAntimatter extends ExplosiveBlock {
    public BlockAntimatter() {
        super(Material.TNT, "antimatter");
    }

    @Override
    protected ExplosiveEntity createEntity(World world, double x, double y, double z, LivingEntity livingEntity) {
        return new EntityAntimatter(world, x, y, z, livingEntity);
    }
}