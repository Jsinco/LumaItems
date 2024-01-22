package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class NeptunesCrownItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#006cd0&lN&#0c76cf&le&#1780ce&lp&#238acd&lt&#2e94cc&lu&#3a9ecb&ln&#45a8ca&le&#53acc3&l'&#63aab4&ls &#73a8a5&lC&#82a796&lr&#92a587&lo&#a2a378&lw&#b2a169&ln",
            mutableListOf("&#006cd0Olympian"),
            mutableListOf("§fWearing this helmet will grant you","§fConduit power and Dolphin's grace"),
            Material.NETHERITE_HELMET,
            mutableListOf("neptunescrown"),
            mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 6, Enchantment.PROTECTION_FIRE to 10, Enchantment.DURABILITY to 5, Enchantment.OXYGEN to 4, Enchantment.WATER_WORKER to 1, Enchantment.MENDING to 1)
        )
        return Pair("neptunescrown", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.DOLPHINS_GRACE, 220, 0, false, false, false))
            }
            else -> return false
        }
        return true
    }
}