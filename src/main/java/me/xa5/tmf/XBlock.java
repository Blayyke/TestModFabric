package me.xa5.tmf;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.registry.Registry;

public abstract class XBlock extends Block implements Registrable {
    private String registryName;

    public XBlock(Material type, String registryName) {
        super(createBlockSettings(type));
        this.registryName = registryName;
    }

    private static Settings createBlockSettings(Material type) {
        FabricBlockSettings settings = FabricBlockSettings.of(type);
        settings.breakByHand(canBeBrokenByHand());

        return settings.build();
    }

    public boolean shouldRegisterItemBlock() {
        return true;
    }

    public void registerItemBlock() {
        BlockItem blockItem = new BlockItem(this, new Item.Settings().itemGroup(TestModFabric.getInstance().getModGroup()));
        blockItem.registerBlockItemMap(Item.BLOCK_ITEM_MAP, blockItem);
        Registry.register(Registry.ITEM, TestModFabric.newIdentifier(getRegistryName()), blockItem);
    }

    protected static boolean canBeBrokenByHand() {
        return true;
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }
}