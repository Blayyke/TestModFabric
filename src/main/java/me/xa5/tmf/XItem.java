package me.xa5.tmf;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class XItem extends Item implements Registrable {
    private String registryName;

    public XItem(String registryName) {
        this(registryName, new Settings().itemGroup(TestModFabric.getInstance().getModGroup()).rarity(Rarity.COMMON));
    }

    public XItem(String registryName, Item.Settings settings) {
        super(settings);
        this.registryName = registryName;
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }
}