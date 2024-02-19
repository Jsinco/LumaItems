package dev.jsinco.lumaitems.util;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Enum for generic tooltypes
public enum ToolType {
    ARMOR(List.of("HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS")),
    WEAPON(List.of("SWORD", "AXE", "BOW", "CROSSBOW", "TRIDENT", "SHIELD")),
    TOOL(List.of("PICKAXE", "AXE", "SHOVEL", "HOE", "ROD"));

    private final List<String> gearType;

    ToolType(List<String> list) {
        this.gearType = list;
    }

    public List<String> getGearTypes() {
        return gearType;
    }

    public static List<String> getArmorStrings() {
        return ARMOR.getGearTypes();
    }

    public static List<String> getWeaponStrings() {
        return WEAPON.getGearTypes();
    }

    public static List<String> getToolStrings() {
        return TOOL.getGearTypes();
    }

    public static ToolType getToolType(Material material) {
        for (String string :  ToolType.getArmorStrings()) {
            if (material.toString().contains(string)) return ToolType.ARMOR;
        }
        for (String string :  ToolType.getToolStrings()) {
            if (material.toString().contains(string)) return ToolType.TOOL;
        }
        for (String string :  ToolType.getWeaponStrings()) {
            if (material.toString().contains(string)) return ToolType.WEAPON;
        }
        return null;
    }


    public EquipmentSlot getEquipmentSlot() {
        switch (this) {
            case ARMOR -> {
                return EquipmentSlot.CHEST;
            }
            case WEAPON -> {
                return EquipmentSlot.HAND;
            }
            case TOOL -> {
                return EquipmentSlot.HAND;
            }
        }
        return null;
    }
}
