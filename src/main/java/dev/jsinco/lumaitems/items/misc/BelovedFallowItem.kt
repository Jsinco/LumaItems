package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Animals
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID

class BelovedFallowItem : CustomItem {

    companion object {
        private val cooldown: MutableSet<UUID> = mutableSetOf()
        private val plugin: LumaItems = LumaItems.getPlugin()
    }


    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#ffa5e3&lB&#ffafe6&le&#ffb8e9&ll&#ffc2ed&lo&#ffcbf0&lv&#ffd5f3&le&#ffdef6&ld &#f9d8f8&lF&#f4d1f9&la&#eecbfb&ll&#e8c5fc&ll&#e3befe&lo&#ddb8ff&lw",
            mutableListOf("&#ddb8ffBreeder"),
            mutableListOf("Right-click while holding", "to breed animals in a 5x5", "radius around you", "", "&cCooldown: 2m"),
            Material.NETHERITE_HOE,
            mutableListOf("belovedfallow"),
            mutableMapOf(Enchantment.MENDING to 1, Enchantment.DURABILITY to 10, Enchantment.DIG_SPEED to 6, Enchantment.LOOT_BONUS_BLOCKS to 5)
        )
        item.tier = "&#fb5a5a&lV&#fb6069&la&#fc6677&ll&#fc6c86&le&#fc7294&ln&#fd78a3&lt&#fd7eb2&li&#fb83be&ln&#f788c9&le&#f38dd4&ls &#f092df&l2&#ec97e9&l0&#e89cf4&l2&#e4a1ff&l4"
        return Pair("belovedfallow", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RIGHT_CLICK -> {
                val playerLocation = player.location
                val entities = playerLocation.world.getNearbyEntities(playerLocation, 5.0, 5.0, 5.0)

                var affected = 0
                for (entity in entities) {
                    if (entity is Animals) {
                        if (!entity.canBreed()) {
                            continue
                        }

                        entity.world.spawnParticle(Particle.HEART, entity.location, 4, 0.2, 0.5, 0.2, 0.0)
                        entity.loveModeTicks = 600 // Normal breeding time
                        affected++
                    }
                }

                if (affected == 0) {
                    return false
                }

                cooldown.add(player.uniqueId)
                plugin.server.scheduler.runTaskLater(plugin, Runnable {
                    cooldown.remove(player.uniqueId)
                }, 2400L)
            }
            else -> return false
        }
        return true
    }
}