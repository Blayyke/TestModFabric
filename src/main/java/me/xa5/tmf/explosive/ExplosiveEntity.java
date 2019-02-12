package me.xa5.tmf.explosive;

import me.xa5.tmf.TestModFabric;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.packet.EntitySpawnClientPacket;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ExplosiveEntity extends Entity {
    private static final TrackedData<Integer> FUSE;
    private LivingEntity causingEntity;
    private int fuseTimer;

    public ExplosiveEntity(EntityType<?> type, World world) {
        super(type, world);
        this.fuseTimer = 80;
        this.field_6033 = true;
        this.fireImmune = true;
    }

    public ExplosiveEntity(EntityType<?> type, World world, int fuseTimer) {
        this(type, world);
        this.fuseTimer = fuseTimer;
    }

    public ExplosiveEntity(EntityType<?> type, double x, double y, double z, LivingEntity causingEntity, World world) {
        this(type, world);
        this.setPosition(x, y, z);
        float float_1 = (float) (Math.random() * 6.2831854820251465D);
        this.velocityX = (double) (-((float) Math.sin((double) float_1)) * 0.02F);
        this.velocityY = 0.20000000298023224D;
        this.velocityZ = (double) (-((float) Math.cos((double) float_1)) * 0.02F);
        this.setFuse(80);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = causingEntity;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(FUSE, 80);
    }

    @Override
    protected boolean method_5658() {
        return false;
    }

    @Override
    public boolean doesCollide() {
        return !this.invalid;
    }

    @Override
    public void update() {
        this.prevX = this.x;
        this.prevY = this.y;
        this.prevZ = this.z;
        if (!this.isUnaffectedByGravity()) {
            this.velocityY -= 0.03999999910593033D;
        }

        this.move(MovementType.SELF, this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= 0.9800000190734863D;
        this.velocityY *= 0.9800000190734863D;
        this.velocityZ *= 0.9800000190734863D;
        if (this.onGround) {
            this.velocityX *= 0.699999988079071D;
            this.velocityZ *= 0.699999988079071D;
            this.velocityY *= -0.5D;
        }

        --this.fuseTimer;
        if (this.fuseTimer <= 0) {
            this.invalidate();
            if (!this.world.isClient) {
                explode(new BlockPos(this.x, this.y, this.z), this.world);
            }
        } else {
            this.method_5713();
            this.world.addParticle(ParticleTypes.SMOKE, this.x, this.y + 0.5D, this.z, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void explode(BlockPos center, World world) {
        int radius = getExplosiveRadius();
        if (radius <= 0) {
            TestModFabric.getInstance().getLogger().warn(getClass().getName() + " called explode() but provides a radius <= 0!");
            return;
        }

        int centerX = center.getX();
        int centerY = center.getY();
        int centerZ = center.getZ();

        // X,y,z would be nicer but having y first lets us break the loop easier
        for (int y = centerY + radius; y >= centerY - radius; y--) {
            for (int x = centerX - radius; x <= centerX + radius; x++) {
                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                    double distance = ((centerX - x) * (centerX - x) + ((centerZ - z) * (centerZ - z)) + ((centerY - y) * (centerY - y)));

                    if (distance < radius * radius) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (shouldDestroyBedrock() || world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                        }
                    }
                }
            }

            if (y == 0) break; // Don't go below 0
        }
    }

    protected boolean shouldDestroyBedrock() {
        return false;
    }

    protected abstract int getExplosiveRadius();

    @Override
    protected void writeCustomDataToTag(CompoundTag nbt) {
        nbt.putShort("Fuse", (short) this.getFuseTimer());
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag nbt) {
        this.setFuse(nbt.getShort("Fuse"));
    }

    public LivingEntity getCausingEntity() {
        return this.causingEntity;
    }

    @Override
    public float getEyeHeight() {
        return 0.0F;
    }


    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnClientPacket(this);
    }

    public void setFuse(int fuseTimer) {
        this.dataTracker.set(FUSE, fuseTimer);
        this.fuseTimer = fuseTimer;
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (FUSE.equals(data)) {
            this.fuseTimer = this.getFuse();
        }
    }

    private int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public int getFuseTimer() {
        return this.fuseTimer;
    }

    static {
        FUSE = DataTracker.registerData(ExplosiveEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}