package dev.jsinco.lumaitems.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
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
            Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "genericKnockbackResistance", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))),
    NETHERITE_SWORD(Map.of(
            Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attackDamage", 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND),
            Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attackSpeed", 1.6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))),
    NETHERITE_PICKAXE(Map.of(
            Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "attackDamage", 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND),
            Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "attackSpeed", 1.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)));

    private final Map<Attribute, AttributeModifier> defaultAttributes;
    private Map<Attribute, AttributeModifier> attributeModifierMap;

    DefaultAttributes(Map<Attribute, AttributeModifier> defaultAttributes) {
        this.defaultAttributes = defaultAttributes;
        this.attributeModifierMap = new HashMap<>(defaultAttributes);
    }

    public void setAttributes(Map<Attribute, AttributeModifier> attributes) {
        this.attributeModifierMap = attributes;
    }

    public void addAttribute(Attribute attribute, AttributeModifier attributeModifier) {
        attributeModifierMap.put(attribute, attributeModifier);
    }

    public Map<Attribute, AttributeModifier> getDefaultAttributes() {
        return defaultAttributes;
    }

    public Map<Attribute, AttributeModifier> getAttributes() {
        return attributeModifierMap;
    }

    public Map<Attribute, AttributeModifier> appendThenGetAttributes(Attribute attribute, AttributeModifier attributeModifier) {
        attributeModifierMap.put(attribute, attributeModifier);
        return attributeModifierMap;
    }
}
