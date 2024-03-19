package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BouncyBunnyBarillas : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
                "bouncybunnybarillas",
                mutableListOf(),
                mutableListOf("Bounce like a bunny!"),
                Material.NETHERITE_BOOTS,
                mutableListOf("bouncybunnybarillas"),
                mutableMapOf()
        )
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"
        return Pair("bouncybunnybarillas", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                if (Util.isItemInSlot("bouncybunnybarillas", EquipmentSlot.FEET, player)) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 320, 2, false, false, false))
                }
            }
            Ability.ARMOR_SWAP -> {
                if (!Util.isItemInSlot("bouncybunnybarillas", EquipmentSlot.FEET, player)) {
                    player.removePotionEffect(PotionEffectType.JUMP)
                } else {
                    player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 320, 2, false, false, false))
                }
            }
            else -> return false
        }
        return true
    }
}