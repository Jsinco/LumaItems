package dev.jsinco.lumaitems.items

import com.iridium.iridiumcolorapi.IridiumColorAPI
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.util.MiniMessageUtil
import dev.jsinco.lumaitems.enums.Tier
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

// Breakdown:
// - This class is for creating custom items easily.
// - Our constructor takes in the parameters that generally most custom items always have and builds an item based off of them.
// - Some other options are available outside the constructor.

class ItemFactory(
    private val name: String,
    private val customEnchants: MutableList<String>,
    private val lore: MutableList<String>,
    private val material: Material,
    private val persistentData: MutableList<String>,
    private val vanillaEnchants: MutableMap<Enchantment, Int>,

    var tier: String = "&#AC87FB&lAstral", //"&#ffc8c8&lC&#ffcfc8&le&#ffd5c7&ll&#ffdcc7&le&#ffe3c7&ls&#ffe9c6&lt&#fff0c6&li&#fff6c5&la&#fffdc5&ll"

    var unbreakable: Boolean = false,
    var hideEnchants: Boolean = false,
    var addSpace: Boolean = true,
    var autoHat: Boolean = false,
    var attributeModifiers: MutableMap<Attribute, AttributeModifier> = mutableMapOf(),
    val stringPersistentDatas: MutableMap<NamespacedKey, String> = mutableMapOf(),
    var quotes: MutableList<String> = mutableListOf()
) {

    private val tierFormat = listOf(
        "",
        "&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ",
        "&#EEE1D5Tier • $tier",
        "&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       "
    )

    private val miniMessageTierFormat = listOf(
        "",
        "<#EEE1D5><st>       </st>⋆⁺₊⋆ ★ ⋆⁺₊⋆<st>       </st>",
        "<#EEE1D5>Tier • $tier",
        "<#EEE1D5><st>       </st>⋆⁺₊⋆ ★ ⋆⁺₊⋆<st>       </st>"
    )

    var miniMessage = false

    fun miniMessage() {
        miniMessage = true
    }

    companion object {
        private val plugin: LumaItems = LumaItems.getInstance()
        fun builder() = Builder()
    }


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
        val meta = item.itemMeta as? Damageable ?: return item

        meta.persistentDataContainer.set(NamespacedKey(plugin, "lumaitem"), PersistentDataType.SHORT, 1)
        for (name in persistentData) {
            meta.persistentDataContainer.set(NamespacedKey(plugin, name), PersistentDataType.SHORT, 1)
        }

        for (stringPersistentData in stringPersistentDatas) {
            meta.persistentDataContainer.set(stringPersistentData.key, PersistentDataType.STRING, stringPersistentData.value)
        }


        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE)

        val combinedLore: MutableList<String> = mutableListOf()
        combinedLore.addAll(customEnchants)


        if ((addSpace && lore.isNotEmpty()) || quotes.isNotEmpty()) combinedLore.add("")

        if (quotes.isNotEmpty()) {
            combinedLore.addAll(quotes)
            combinedLore.add("")
        }


        if (!miniMessage) {
            combinedLore.addAll(lore.map { "&f$it" })
            combinedLore.addAll(tierFormat)

            meta.setDisplayName(Util.colorcode(name))
            meta.lore = Util.colorcodeList(combinedLore)
        } else {
            combinedLore.addAll(lore.map { "<white>$it" })
            combinedLore.addAll(miniMessageTierFormat)

            meta.displayName(MiniMessageUtil.mm(name))
            meta.lore(MiniMessageUtil.mml(combinedLore))
        }

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

        if (autoHat) {
            meta.persistentDataContainer.set(NamespacedKey(plugin, "autohat"), PersistentDataType.SHORT, 1)
        }


        item.itemMeta = meta
        return item
    }

    class Builder {
        private var name: String = ""
        private var customEnchants: MutableList<String> = mutableListOf()
        private var lore: MutableList<String> = mutableListOf()
        private var material: Material = Material.AIR
        private var persistentData: MutableList<String> = mutableListOf()
        private var vanillaEnchants: MutableMap<Enchantment, Int> = mutableMapOf()
        private var tier: String = Tier.ASTRAL.toString()
        private var unbreakable: Boolean = false
        private var hideEnchants: Boolean = false
        private var addSpace: Boolean = true
        private var autoHat: Boolean = false
        private var attributeModifiers: MutableMap<Attribute, AttributeModifier> = mutableMapOf()
        private var stringPersistentDatas: MutableMap<NamespacedKey, String> = mutableMapOf()
        private var quotes: MutableList<String> = mutableListOf()

        fun name(name: String) = apply { this.name = name }
        fun customEnchants(customEnchants: MutableList<String>) = apply { this.customEnchants = customEnchants }
        fun customEnchants(vararg customEnchants: String) = apply { this.customEnchants = customEnchants.toMutableList() }
        fun lore(lore: MutableList<String>) = apply { this.lore = lore }
        fun lore(vararg lore: String) = apply { this.lore = lore.toMutableList() }
        fun material(material: Material) = apply { this.material = material }
        fun material(s: String) = apply { this.material = Material.matchMaterial(s) ?: Material.AIR }
        fun persistentData(persistentData: MutableList<String>) = apply { this.persistentData = persistentData }
        fun persistentData(vararg persistentData: String) = apply { this.persistentData = persistentData.toMutableList() }
        fun vanillaEnchants(vanillaEnchants: MutableMap<Enchantment, Int>) = apply { this.vanillaEnchants = vanillaEnchants }
        fun tier(tier: String) = apply { this.tier = tier }
        fun tier (tier: Tier) = apply { this.tier = tier.tierString }
        fun unbreakable(unbreakable: Boolean) = apply { this.unbreakable = unbreakable }
        fun hideEnchants(hideEnchants: Boolean) = apply { this.hideEnchants = hideEnchants }
        fun addSpace(addSpace: Boolean) = apply { this.addSpace = addSpace }
        fun autoHat(autoHat: Boolean) = apply { this.autoHat = autoHat }
        fun attributeModifiers(attributeModifiers: MutableMap<Attribute, AttributeModifier>) = apply { this.attributeModifiers = attributeModifiers }
        fun stringPersistentDatas(stringPersistentDatas: MutableMap<NamespacedKey, String>) = apply { this.stringPersistentDatas = stringPersistentDatas }
        fun quotes(quotes: MutableList<String>) = apply { this.quotes = quotes }
        fun quotes(vararg quotes: String) = apply { this.quotes = quotes.toMutableList() }

        fun build() = ItemFactory(
            name, customEnchants, lore, material, persistentData, vanillaEnchants,
            tier, unbreakable, hideEnchants, addSpace, autoHat, attributeModifiers, stringPersistentDatas, quotes
        ).apply { miniMessage() }

        fun buildNoMiniMessage() = ItemFactory(
            name, customEnchants, lore, material, persistentData, vanillaEnchants,
            tier, unbreakable, hideEnchants, addSpace, autoHat, attributeModifiers, stringPersistentDatas, quotes
        )

        fun buildPair(): Pair<String, ItemStack> {
            if (persistentData.size != 1) {
                LumaItems.log("Error: persistentData must have exactly one value!")
                return Pair("", ItemStack(Material.AIR))
            }
            return Pair(persistentData[0], build().createItem())
        }

    }
}