package dev.jsinco.lumaitems.items.astral

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class AstralSetFactory (val name: String, val customEnchantNames: List<String>?) {

    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
    }

    var commonLore: List<String> = listOf()
    var identifier = "${name.lowercase()}-set"
    val createdAstralItems: MutableList<ItemStack> = mutableListOf()
    var commonEnchants: MutableMap<Enchantment, Int> = mutableMapOf()

    constructor(name: String, customEnchantNames: List<String>?, commonLore: List<String>) : this(name, customEnchantNames) {
        this.commonLore = commonLore
    }

    fun astralSetItem(material: Material, vanillaEnchants: MutableMap<Enchantment, Int>, includeCommonEnchants: Boolean): ItemStack {
        return astralSetItem(material, vanillaEnchants, commonLore, includeCommonEnchants)
    }

    fun astralSetItem(material: Material, vanillaEnchants: MutableMap<Enchantment, Int>, lore: List<String>): ItemStack {
        return astralSetItem(material, vanillaEnchants, lore, true)
    }

    fun astralSetItem(material: Material, vanillaEnchants: MutableMap<Enchantment, Int>, lore: List<String>, includeCommonEnchants: Boolean): ItemStack {
        return astralSetItem(material, vanillaEnchants, lore, includeCommonEnchants, null)
    }

    fun astralSetItem(
        material: Material,
        vanillaEnchants: MutableMap<Enchantment, Int>,
        lore: List<String>,
        includeCommonEnchants: Boolean,
        attributeModifiers: MutableMap<Attribute, AttributeModifier>?): ItemStack {


        // override common enchants
        val finalVanillaEnchants = if (includeCommonEnchants) {
            commonEnchants.toMutableMap().also { it.putAll(vanillaEnchants) }
        } else {
            vanillaEnchants
        }

        val item = ItemFactory(
            "&#E97979&l$name &f${Util.getGearType(material)}",
            customEnchantNames?.toMutableList() ?: mutableListOf(),
            lore.toMutableList(),
            material,
            mutableListOf(identifier),
            finalVanillaEnchants
        )

        if (attributeModifiers != null) {
            item.attributeModifiers = attributeModifiers
        }


        item.stringPersistentDatas[NamespacedKey(plugin, "relic-rarity")] = Rarity.ASTRAL.name
        return item.createItem().also { createdAstralItems.add(it) }
    }

    fun astralSetItemGenericEnchantOnly(material: Material): ItemStack {
        return astralSetItemGenericEnchantOnly(material, commonLore, null)
    }

    fun astralSetItemGenericEnchantOnly(material: Material, lore: List<String>): ItemStack {
        return astralSetItemGenericEnchantOnly(material, lore, null)
    }

    fun astralSetItemGenericEnchantOnly(
        material: Material,
        lore: List<String>,
        attributeModifiers: MutableMap<Attribute, AttributeModifier>?): ItemStack {

        val enchants: MutableMap<Enchantment, Int> = mutableMapOf()

        for (enchant in commonEnchants) {
            if (enchant.key.canEnchantItem(ItemStack(material))) {
                enchants[enchant.key] = enchant.value
            }
        }

        return astralSetItem(material, enchants, lore, false, attributeModifiers)
    }

}