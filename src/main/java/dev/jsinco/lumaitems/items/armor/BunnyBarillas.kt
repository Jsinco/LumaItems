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

class BunnyBarillas : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
                "&#E7934F&lB&#EBA454&lu&#EFB658&ln&#F3C75D&ln&#F7D861&ly &#FBEA66&lB&#FFFB6A&la&#E5F461&lr&#CAED58&li&#B0E64F&ll&#95DF45&ll&#7BD83C&la&#60D133&ls",
                mutableListOf("&#E7934FJump Boost III"),
                mutableListOf("Jump like a bunny!"),
                Material.NETHERITE_BOOTS,
                mutableListOf("bunnybarillas"),
                mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 6, Enchantment.PROTECTION_PROJECTILE to 7, Enchantment.PROTECTION_FALL to 5, Enchantment.DURABILITY to 8, Enchantment.MENDING to 1)
        )
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"
        return Pair("bunnybarillas", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                if (Util.isItemInSlot("bunnybarillas", EquipmentSlot.FEET, player)) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 340, 2, false, false, false))
                }
            }
            Ability.ARMOR_SWAP -> {
                if (!Util.isItemInSlot("bunnybarillas", EquipmentSlot.FEET, player)) {
                    player.removePotionEffect(PotionEffectType.JUMP)
                } else {
                    player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 340, 2, false, false, false))
                }
            }
            else -> return false
        }
        return true
    }
}