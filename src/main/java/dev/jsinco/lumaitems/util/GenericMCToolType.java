package dev.jsinco.lumaitems.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public enum GenericMCToolType {

    HELMET("HELMET"),
    CHESTPLATE("CHESTPLATE"),
    LEGGINGS("LEGGINGS"),
    BOOTS("BOOTS"),
    SWORD("SWORD"),
    AXE("AXE"),
    PICKAXE("PICKAXE"),
    SHOVEL("SHOVEL"),
    HOE("HOE"),
    BOW("BOW"),
    CROSSBOW("CROSSBOW"),
    TRIDENT("TRIDENT"),
    SHIELD("SHIELD"),
    FISHING_ROD("FISHING_ROD");

    private final String toolTypeAsGenericString;

    GenericMCToolType(String string) {
        this.toolTypeAsGenericString = string;
    }

    public String getToolTypeAsGenericString() {
        return toolTypeAsGenericString;
    }

    @Nullable
    public static GenericMCToolType getToolType(ItemStack item) {
        return getToolType(item.getType().toString());
    }
    @Nullable
    public static GenericMCToolType getToolType(Material material) {
        return getToolType(material.toString());
    }
    @Nullable
    public static GenericMCToolType getToolType(String string) {
        string = string.toUpperCase();

        for (GenericMCToolType toolType : GenericMCToolType.values()) {
            if (toolType.getToolTypeAsGenericString().contains(string)) return toolType;
        }
        return null;
    }
}
