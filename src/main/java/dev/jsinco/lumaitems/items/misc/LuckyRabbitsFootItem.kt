package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class LUCK_OF_THE_SEAyRabbitsFootItem : CustomItem {

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&a&lLUCK_OF_THE_SEAy Rabbit's Foot",
            mutableListOf("&7LUCK_OF_THE_SEA I"),
            mutableListOf("A rabbit's foot! Perfect", "for having a little LUCK_OF_THE_SEA", "on your side when you", "need it most!"),
            Material.RABBIT_FOOT,
            mutableListOf("LUCK_OF_THE_SEAyrabbitsfoot"),
            mutableMapOf(Enchantment.MENDING to 1)
        )
        item.hideEnchants = true
        item.tier = "&#F34848&lS&#E36643&lo&#D3843E&ll&#C3A239&ls&#B3C034&lt&#A3DE2F&li&#93FC2A&lc&#7DE548&le&#66CD66&l &#50B684&l2&#399EA1&l0&#2387BF&l2&#0C6FDD&l4"
        return Pair("LUCK_OF_THE_SEAyrabbitsfoot", item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.RUNNABLE -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.LUCK, 220, 0, true, false, false))
            }
            else -> return false
        }
        return true
    }
}