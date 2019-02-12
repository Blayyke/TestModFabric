package me.xa5.tmf;

import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public abstract class XFoodItem extends FoodItem implements Registrable {
    public XFoodItem(String registryName, int int_1, float float_1, boolean boolean_1) {
        super(int_1, float_1, boolean_1, new Item.Settings().itemGroup(TestModFabric.getInstance().getModGroup()).rarity(Rarity.COMMON));
        this.registryName = registryName;
    }

    private String registryName;

    @Override
    public String getRegistryName() {
        return registryName;
    }
}