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

class LuminalRabbitEyeglassesItem : CustomItem {


    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#717FEF&lL&#7B83F0&lu&#8487F2&lm&#8E8CF3&li&#9890F4&ln&#A194F5&la&#AB98F7&ll &#B49CF8&lR&#BEA0F9&la&#C8A5FA&lb&#D1A9FC&lb&#DBADFD&li&#DAA9FB&lt &#D9A5F8&lE&#D9A0F6&ly&#D89CF4&le&#D798F1&lg&#D694EF&ll&#D590EC&la&#D48CEA&ls&#D487E8&ls&#D383E5&le&#D27FE3&ls",
            mutableListOf("&#DBADFDNight Vision"),
            mutableListOf("Grants the wearer night vision", "while being worn."),
            Material.NETHERITE_HELMET,
            mutableListOf("luminalrabbiteyeglasses"),
            mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 7, Enchantment.DURABILITY to 8, Enchantment.PROTECTION_EXPLOSIONS to 6, Enchantment.MENDING to 1, Enchantment.OXYGEN to 4, Enchantment.WATER_WORKER to 1)
        )
        item.addQuote("&#D27FE3\"See anything, see everything.\"")
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"

        return Pair("luminalrabbiteyeglasses", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 340, 0, false, false, false))
            }

            Ability.ARMOR_CHANGE -> {
                if (!Util.isItemInSlot("luminalrabbiteyeglasses", EquipmentSlot.HEAD, player)) {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION)
                } else {
                    player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 340, 0, false, false, false))
                }
            }

            else -> return false
        }
        return true
    }
}