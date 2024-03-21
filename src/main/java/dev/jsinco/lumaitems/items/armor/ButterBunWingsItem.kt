package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ButterBunWingsItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#EAA974&lB&#EFB882&lu&#F4C78F&lt&#F9D79D&lt&#FDE6AA&le&#FEE7B9&lr&#FDE0C7&lB&#FBD9D6&lu&#FAD2E4&ln &#F7CEEF&lW&#F1CEF3&li&#ECCDF7&ln&#E6CDFB&lg&#E0CDFF&ls",
            mutableListOf("&7Glow I"),
            mutableListOf("These wings are made of","butterbuns, and they","make you glow!"),
            Material.ELYTRA,
            mutableListOf("butterbunwings"),
            mutableMapOf(Enchantment.DURABILITY to 7, Enchantment.PROTECTION_ENVIRONMENTAL to 6, Enchantment.PROTECTION_FALL to 5, Enchantment.MENDING to 1)
        )
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"
        return Pair("butterbunwings", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                if (Util.isItemInSlot("butterbunwings", EquipmentSlot.CHEST, player)) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 340, 0, false, false, false))
                }
            }
            Ability.ARMOR_CHANGE -> {
                if (!Util.isItemInSlot("butterbunwings", EquipmentSlot.CHEST, player)) {
                    player.removePotionEffect(PotionEffectType.GLOWING)
                } else {
                    player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 340, 0, false, false, false))
                }
            }
            else -> return false
        }
        return true
    }

}
