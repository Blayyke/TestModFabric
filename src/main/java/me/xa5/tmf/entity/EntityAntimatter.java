package me.xa5.tmf.entity;

import me.xa5.tmf.explosive.ExplosiveEntity;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class EntityAntimatter extends ExplosiveEntity {
    public static final EntityType<?> TYPE = FabricEntityTypeBuilder.create(EntityCategory.MISC).build();

    public EntityAntimatter(World world) {
        super(TYPE, world);
    }

    public EntityAntimatter(World world, double x, double y, double z, LivingEntity causingEntity) {
        super(TYPE, x, y, z, causingEntity, world);
    }

    @Override
    protected int getExplosiveRadius() {
        return 45;
    }

    @Override
    protected boolean shouldDestroyBedrock() {
        return true;
    }
}