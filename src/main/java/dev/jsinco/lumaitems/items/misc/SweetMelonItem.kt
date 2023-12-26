package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SweetMelonItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = CreateItem(
            "&#ff347d&lS&#ff546b&lw&#fe7359&le&#fe9347&le&#fdb235&lt &#ecc930&lM&#c9d639&le&#a6e341&ll&#83f04a&lo&#60fd52&ln",
            mutableListOf("&7Haste II"),
            mutableListOf("§fThis sweet melon just","§freally raises your blood sugar","","§fHolding this melon in your","§foffhand will give you a Haste II boost"),
            Material.GLISTERING_MELON_SLICE,
            mutableListOf("sweetmelon"),
            mutableMapOf(Enchantment.DURABILITY to 1)
        )
        item.hideEnchants = true
        return Pair("sweetmelon", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 220, 1, false, false, false))
            }
            else -> return false
        }
        return true
    }
}