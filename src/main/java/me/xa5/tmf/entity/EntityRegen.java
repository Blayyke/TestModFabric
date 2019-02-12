package me.xa5.tmf.entity;

import me.xa5.tmf.explosive.ExplosiveEntity;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class EntityRegen extends ExplosiveEntity {
    public static final EntityType<?> TYPE = FabricEntityTypeBuilder.create(EntityCategory.MISC).build();

    public EntityRegen(World world) {
        super(TYPE, world);
    }

    public EntityRegen(World world, double x, double y, double z, LivingEntity causingEntity) {
        super(TYPE, x, y, z, causingEntity, world);
    }

    @Override
    protected void explode(BlockPos center, World world) {
        WorldChunk worldChunk = world.getWorldChunk(center);
        ChunkPos pos = worldChunk.getPos();
        ServerWorld serverWorld = (ServerWorld) world;
        ServerChunkManager chunkManager = serverWorld.getChunkManager();
        ChunkGenerator<?> chunkGenerator = chunkManager.getChunkGenerator();
        chunkGenerator.buildSurface(worldChunk);
        chunkGenerator.populateBiomes(worldChunk);
        chunkGenerator.populateNoise(world, worldChunk);
    }

    @Override
    protected int getExplosiveRadius() {
        return 0;
    }
}