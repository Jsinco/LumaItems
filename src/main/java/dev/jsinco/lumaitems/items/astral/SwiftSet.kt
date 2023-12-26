package dev.jsinco.lumaitems.items.astral

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class SwiftSet : CustomItem, AstralSet {

    companion object {
        val materials: List<Material> = listOf(
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.NETHERITE_SWORD,
        )
        val enchants: Map<Enchantment, Int> = mapOf(
            Enchantment.PROTECTION_ENVIRONMENTAL to 4,
            Enchantment.DAMAGE_ALL to 4,
            Enchantment.DURABILITY to 4,
            Enchantment.SWEEPING_EDGE to 4,
        )
    }

    override fun setItems(): List<ItemStack> {
        val items: MutableList<ItemStack> = mutableListOf()

        for (material in materials) {
            val item = ItemStack(material)
            val meta = item.itemMeta!!

            for (enchant in enchants) {
                if (enchant.key.canEnchantItem(item)) {
                    meta.addEnchant(enchant.key, enchant.value, true)
                }
            }
            meta.setDisplayName(Util.colorcode("&#f498f6&lSwift &f${Util.getGearType(item)}"))
            meta.persistentDataContainer.set(NamespacedKey(LumaItems.getPlugin(), "set"), PersistentDataType.STRING, "swiftset")
            item.itemMeta = meta
            items.add(item)
        }
        return items
    }

    override fun createItem(): Pair<String, ItemStack> {
        return Pair("swiftset", ItemStack(Material.AIR))
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        return false // No abilities for this set
    }
}