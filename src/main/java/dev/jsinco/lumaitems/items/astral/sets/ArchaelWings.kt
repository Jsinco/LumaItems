package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.relics.Rarity
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class ArchaelWings : AstralSet {
    override fun setItems(): List<ItemStack> {
        val item = CreateItem(
            "&#fb4d4d&lArchael &fWings",
            mutableListOf(),
            mutableListOf(),
            Material.ELYTRA,
            mutableListOf("archael-set"),
            mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 6, Enchantment.PROTECTION_PROJECTILE to 5, Enchantment.DURABILITY to 5, Enchantment.PROTECTION_FALL to 4, Enchantment.MENDING to 1)
        )
        item.tier = "&#fb4d4d&lAstral"
        item.stringPersistentDatas[NamespacedKey(LumaItems.getPlugin(), "relic-rarity")] = Rarity.ASTRAL.name
        return listOf(item.createItem())
    }
}