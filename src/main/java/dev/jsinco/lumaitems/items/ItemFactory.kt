package dev.jsinco.lumaitems.items

import com.iridium.iridiumcolorapi.IridiumColorAPI
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class ItemFactory(
    private val name: String,
    private val customEnchants: MutableList<String>,
    private val lore: MutableList<String>,
    private val material: Material,
    private val persistentData: MutableList<String>,
    private val vanillaEnchants: MutableMap<Enchantment, Int>
) {

    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
    }

    var tier: String = "&#E97979&lAstral" //"&#ffc8c8&lC&#ffcfc8&le&#ffd5c7&ll&#ffdcc7&le&#ffe3c7&ls&#ffe9c6&lt&#fff0c6&li&#fff6c5&la&#fffdc5&ll"

    var unbreakable: Boolean = false
    var hideEnchants: Boolean = false
    var addSpace: Boolean = true
    var attributeModifiers: MutableMap<Attribute, AttributeModifier> = mutableMapOf()
    val stringPersistentDatas: MutableMap<NamespacedKey, String> = mutableMapOf()
    var quotes: MutableList<String> = mutableListOf()

    fun addQuote(s: String) {
        quotes.add(s)
    }

    fun addGradientQuote(s: String, color1: String, color2: String) {
        val strippedColor1 = color1.replace("#", "").replace("&", "").trim()
        val strippedColor2 = color2.replace("#", "").replace("&", "").trim()
        quotes.add(IridiumColorAPI.process("<GRADIENT:$strippedColor1>\"$s\"</GRADIENT:$strippedColor2>"))
    }

    fun createItem(): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item

        meta.persistentDataContainer.set(NamespacedKey(plugin, "lumaitem"), PersistentDataType.SHORT, 1)
        for (name in persistentData) {
            meta.persistentDataContainer.set(NamespacedKey(plugin, name), PersistentDataType.SHORT, 1)
        }

        for (stringPersistentData in stringPersistentDatas) {
            meta.persistentDataContainer.set(stringPersistentData.key, PersistentDataType.STRING, stringPersistentData.value)
        }

        meta.setDisplayName(Util.colorcode(name))
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_UNBREAKABLE)

        val combinedLore: MutableList<String> = mutableListOf()
        combinedLore.addAll(customEnchants)

        if (lore.isEmpty()) addSpace = false
        if (addSpace || quotes.isNotEmpty()) combinedLore.add("")

        if (quotes.isNotEmpty()) {
            combinedLore.addAll(quotes)
            combinedLore.add("")
        }


        combinedLore.addAll(lore.map { "&f$it" })
        combinedLore.add("")
        combinedLore.add("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ")
        combinedLore.add("&#EEE1D5Tier • $tier")
        combinedLore.add("&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ")

        meta.lore = Util.colorcodeList(combinedLore)

        for (enchant in vanillaEnchants) {
            meta.addEnchant(enchant.key, enchant.value, true)
        }
        if (attributeModifiers.isNotEmpty()) {
            for (attributeModifier in attributeModifiers) {
                meta.addAttributeModifier(attributeModifier.key, attributeModifier.value)
            }
        }
        meta.isUnbreakable = unbreakable
        if (hideEnchants) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        item.itemMeta = meta

        return item
    }
}