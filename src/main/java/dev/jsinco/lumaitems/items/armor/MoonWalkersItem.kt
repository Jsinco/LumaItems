package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MoonWalkersItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = CreateItem(
            "&#1646c1&lM&#295bc1&lo&#3c70c1&lo&#4e84c1&ln&#6199c1&lw&#74aec1&la&#6fa2be&ll&#6a97bc&lk&#658bb9&le&#6080b7&lr&#5b74b4&ls",
            mutableListOf("&#3f73c1J&#4478c1u&#497ec1m&#4d83c1p &#5288c1b&#578ec1o&#5c93c1o&#6199c1s&#669ec1t &#6aa3c1I&#6fa9c1I&#74aec1I"),
            mutableListOf("§fWalk on the moon!"),
            Material.NETHERITE_BOOTS,
            mutableListOf("moonwalkers"),
            mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 7, Enchantment.PROTECTION_PROJECTILE to 4, Enchantment.PROTECTION_FALL to 6, Enchantment.DURABILITY to 8, Enchantment.MENDING to 1)
        )
        return Pair("moonwalkers", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 220, 2, false, false, false))
            }
            else -> return false
        }
        return true
    }
}