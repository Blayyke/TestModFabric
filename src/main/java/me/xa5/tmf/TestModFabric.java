package me.xa5.tmf;

import me.xa5.modconfig.FabricModConfig;
import me.xa5.tmf.blocks.BlockBlock;
import me.xa5.tmf.blocks.BlockOre;
import me.xa5.tmf.blocks.explosive.BlockAntimatter;
import me.xa5.tmf.blocks.explosive.BlockRegenExplosive;
import me.xa5.tmf.entity.EntityAntimatter;
import me.xa5.tmf.entity.EntityRegen;
import me.xa5.tmf.items.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestModFabric implements ModInitializer {
    public static final String MOD_ID = "tmf";
    private static TestModFabric instance;
    private Logger logger = LogManager.getFormatterLogger(getClass().getSimpleName());
    private FabricModConfig config = new FabricModConfig();

    private ItemGroup group = FabricItemGroupBuilder
            .create(newIdentifier("tab"))
            .icon(() -> new ItemStack(TMFBlocks.getInstance().oreCopper))
            .build();
    private Map<String, PlayerSelectionInfo> selectionInfoMap = new HashMap<>();

    public static boolean ENABLE_CAMPFIRE_MIXIN;
    public static boolean ENABLE_AXE_MIXIN;
    public static boolean ENABLE_WITHER_MIXIN;

    public static Identifier newIdentifier(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static TestModFabric getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this;

        try {
            config.loadConfig(new File(config.getConfigFolder(), MOD_ID + ".conf"), this::readConfig);
        } catch (IOException e) {
            getLogger().fatal("Caught exception during reading/loading of config file!");
            throw new RuntimeException(e);
        }

        registerBlocks();
        registerItems();
        registerEntities();
        registerGameRules();
        config.saveConfig();

        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> new Commands().register(serverCommandSourceCommandDispatcher));
    }

    private void registerGameRules() {
        GameRules.getKeys().put("doWitherSpawning", new GameRules.Key("true", GameRules.Type.BOOLEAN));
    }

    private void readConfig() {
        CommentedConfigurationNode tweaks = config.getNode("Tweaks").setComment("Misc tweaks added by " + MOD_ID + ". true = tweak enabled");
        TestModFabric.ENABLE_CAMPFIRE_MIXIN = tweaks.getNode("campfire").setComment("Enable the mixin that defaults placed campfire blocks to unlit.").getBoolean(true);
        TestModFabric.ENABLE_WITHER_MIXIN = tweaks.getNode("wither").setComment("Enable the mixin that disables wither spawning when the doWitherSpawning gamerule is false.").getBoolean(true);
        TestModFabric.ENABLE_AXE_MIXIN = tweaks.getNode("axe").setComment("Enable the mixin that fells whole trees when logs are mined when not shifting.").getBoolean(true);
    }

    private void registerEntities() {
        Registry.register(Registry.ENTITY_TYPE, newIdentifier("antimatter_entity"), EntityAntimatter.TYPE);
        Registry.register(Registry.ENTITY_TYPE, newIdentifier("regen_entity"), EntityRegen.TYPE);
    }

    private void registerItems() {
        TMFItems items = TMFItems.getInstance();

        items.register(items.ingotCopper = new ItemIngot("copper"));
        items.register(items.ingotTin = new ItemIngot("tin"));
        items.register(items.ingotBronze = new ItemIngot("bronze"));
        items.register(items.ingotSteel = new ItemIngot("steel"));
        items.register(items.ingotAluminium = new ItemIngot("aluminium"));

        items.register(items.dustCopper = new ItemDust("copper"));
        items.register(items.dustTin = new ItemDust("tin"));
        items.register(items.dustBronze = new ItemDust("bronze"));
        items.register(items.dustSteel = new ItemDust("steel"));
        items.register(items.dustAluminium = new ItemDust("aluminium"));

        // Dusts for vanilla ingots
        items.register(items.dustGold = new ItemDust("gold"));
        items.register(items.dustIron = new ItemDust("iron"));

        items.register(items.toast = new ItemToast());
        items.register(items.clayBucket = new ItemClayBucket());
        items.register(items.firedClayBucket = new ItemFiredClayBucket(Fluids.EMPTY));
    }

    private void registerBlocks() {
        TMFBlocks blocks = TMFBlocks.getInstance();
        blocks.register(blocks.oreCopper = new BlockOre("copper"));
        blocks.register(blocks.oreTin = new BlockOre("tin"));
        blocks.register(blocks.oreAluminium = new BlockOre("aluminium"));

        blocks.register(blocks.blockCopper = new BlockBlock("copper"));
        blocks.register(blocks.blockTin = new BlockBlock("tin"));
        blocks.register(blocks.blockBronze = new BlockBlock("bronze"));
        blocks.register(blocks.blockSteel = new BlockBlock("steel"));
        blocks.register(blocks.blockAluminium = new BlockBlock("aluminium"));

        blocks.register(blocks.blockAntimatter = new BlockAntimatter());
        blocks.register(blocks.blockRegenExplosive = new BlockRegenExplosive());
    }

    public ItemGroup getModGroup() {
        return group;
    }

    public Logger getLogger() {
        return logger;
    }

    public PlayerSelectionInfo getSelectionInfo(PlayerEntity player) {
        return selectionInfoMap.putIfAbsent(player.getUuidAsString(), new PlayerSelectionInfo(player));
    }

    public FabricModConfig getConfig() {
        return config;
    }
}