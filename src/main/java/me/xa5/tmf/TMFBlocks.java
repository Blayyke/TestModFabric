package me.xa5.tmf;

import me.xa5.tmf.blocks.BlockBlock;
import me.xa5.tmf.blocks.explosive.BlockAntimatter;
import me.xa5.tmf.blocks.BlockOre;
import me.xa5.tmf.blocks.explosive.BlockRegenExplosive;
import net.minecraft.util.registry.Registry;

public class TMFBlocks {
    private static TMFBlocks instance;
    public BlockOre oreCopper;
    public BlockOre oreTin;
    public BlockOre oreAluminium;

    public BlockBlock blockCopper;
    public BlockBlock blockTin;
    public BlockBlock blockBronze;
    public BlockBlock blockSteel;
    public BlockBlock blockAluminium;

    public BlockAntimatter blockAntimatter;
    public BlockRegenExplosive blockRegenExplosive;

    public static TMFBlocks getInstance() {
        return instance;
    }

    public void register(XBlock block) {
        if (!TestModFabric.getInstance().getConfig().getNode("Blocks").setComment("Set any of these to false to prevent them from being registered.").getNode(block.getRegistryName()).getBoolean(true)) {
            TestModFabric.getInstance().getLogger().info("Skipping registry of item " + block.getRegistryName() + ".");
            return;
        }

        Registry.register(Registry.BLOCK, TestModFabric.newIdentifier(block.getRegistryName()), block);
        if (block.shouldRegisterItemBlock()) {
            block.registerItemBlock();
        }

        TestModFabric.getInstance().getLogger().info("Registered block : " + block.getRegistryName());
    }

    static {
        instance = new TMFBlocks();
    }
}