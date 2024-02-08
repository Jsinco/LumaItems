package dev.jsinco.lumaitems.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public enum DefaultAttributes {

    NETHERITE_HELMET(Map.of(
            Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "genericArmor", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD),
            Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "genericArmorToughness", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD),
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "genericKnockbackResistance", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))),
    NETHERITE_CHESTPLATE(Map.of(
            Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "genericArmor", 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST),
            Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "genericArmorToughness", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST),
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "genericKnockbackResistance", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))),
    NETHERITE_LEGGINGS(Map.of(
            Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "genericArmor", 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS),
            Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "genericArmorToughness", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS),
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "genericKnockbackResistance", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))),
    NETHERITE_BOOTS(Map.of(
            Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "genericArmor", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET),
            Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "genericArmorToughness", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET),
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "genericKnockbackResistance", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET)));

    private final Map<Attribute, AttributeModifier> defaultAttributes;

    DefaultAttributes(Map<Attribute, AttributeModifier> defaultAttributes) {
        this.defaultAttributes = defaultAttributes;
    }

    public Map<Attribute, AttributeModifier> getDefaultAttributes() {
        return defaultAttributes;
    }
}
