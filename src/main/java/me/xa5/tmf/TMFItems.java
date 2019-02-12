package me.xa5.tmf;

import me.xa5.tmf.items.*;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class TMFItems {
    private static TMFItems instance;

    public ItemIngot ingotCopper;
    public ItemIngot ingotTin;
    public ItemIngot ingotBronze;
    public ItemIngot ingotSteel;
    public ItemIngot ingotAluminium;

    public ItemDust dustCopper;
    public ItemDust dustTin;
    public ItemDust dustBronze;
    public ItemDust dustSteel;
    public ItemDust dustAluminium;

    // Dusts for vanilla ingots
    public ItemDust dustGold;
    public ItemDust dustIron;

    public ItemToast toast;
    public ItemClayBucket clayBucket;
    public ItemFiredClayBucket firedClayBucket;

    public static TMFItems getInstance() {
        return instance;
    }

    public void register(XItem item) {
        register(item.getRegistryName(), item);
    }

    public void register(XFoodItem item) {
        register(item.getRegistryName(), item);
    }

    static {
        instance = new TMFItems();
    }

    public void register(String registryName, Item item) {
        if (!TestModFabric.getInstance().getConfig().getNode("Items").setComment("Set any of these to false to prevent them from being registered.").getNode(registryName).getBoolean(true)) {
            TestModFabric.getInstance().getLogger().info("Skipping registry of item " + registryName + ".");
            return;
        }

        Registry.register(Registry.ITEM, TestModFabric.newIdentifier(registryName), item);
        TestModFabric.getInstance().getLogger().info("Registered item : " + registryName);
    }
}