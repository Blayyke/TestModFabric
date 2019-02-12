package me.xa5.tmf.mixin;

import net.minecraft.block.WitherSkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherSkullBlock.class)
public class MixinWither {
    @Inject(method = "onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SkullBlockEntity;)V", at = @At("HEAD"))
    private static void onPlaced(World world_1, BlockPos blockPos_1, SkullBlockEntity skullBlockEntity_1, CallbackInfo info) {
        if (!world_1.getGameRules().getBoolean("doWitherSpawning")) {
            return;
        }
    }
}