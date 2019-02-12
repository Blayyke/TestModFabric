package me.xa5.tmf;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlayerSelectionInfo {
    private Vec3d pos1;
    private Vec3d pos2;
    private final PlayerEntity player;

    public PlayerSelectionInfo(PlayerEntity player) {
        this.player = player;
    }

    public void setPos1(Vec3d pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Vec3d pos2) {
        this.pos2 = pos2;
    }

    public Vec3d getPos1() {
        return pos1;
    }

    public Vec3d getPos2() {
        return pos2;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public static boolean isInvalidPos(Vec3d posVector, World world) {
        return !(posVector.y >= 0) || !(posVector.y <= world.getChunkManager().getChunkGenerator().getMaxY());
    }

    public static void setArea(PlayerSelectionInfo selectionInfo, World world, Block block, boolean hollow) {
        Vec3d pos1 = selectionInfo.getPos1();
        Vec3d pos2 = selectionInfo.getPos2();
        if (pos1 == null) {
            message(selectionInfo.getPlayer(), "pos1.unset");
            return;
        } else if (pos2 == null) {
            message(selectionInfo.getPlayer(), "pos2.unset");
            return;
        }

        double minX = Math.min(pos1.x, pos2.x);
        double minY = Math.min(pos1.x, pos2.x);
        double minZ = Math.min(pos1.x, pos2.x);
        double maxX = Math.max(pos1.x, pos2.x);
        double maxY = Math.max(pos1.x, pos2.x);
        double maxZ = Math.max(pos1.x, pos2.x);

        for (double x = minX; x < maxX; x++) {
            for (double y = minY; y < maxY; y++) {
                for (double z = minZ; z < maxZ; z++) {
                    world.setBlockState(new BlockPos(x, y, z), block.getDefaultState());
                }
            }
        }
    }

    public static void message(PlayerEntity playerEntity, String text) {
        playerEntity.addChatMessage(new TranslatableTextComponent(text), false);
    }
}