package dev.jsinco.lumaitems.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Enum for generic Minecraft tool types.
 * Determine their type without regard for the type of material.
 */
public enum GenericMCToolType {

    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    SWORD,
    AXE,
    PICKAXE,
    SHOVEL,
    HOE,
    BOW,
    CROSSBOW,
    TRIDENT,
    SHIELD,
    FISHING_ROD;

    private final String toolTypeAsGenericString;

    GenericMCToolType() {
        this.toolTypeAsGenericString = this.toString();
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
            if (toolType.getToolTypeAsGenericString().contains(string)) {
                return toolType;
            }
        }
        return null;
    }
}
