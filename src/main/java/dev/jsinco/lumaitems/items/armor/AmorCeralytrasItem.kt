package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.AbilityUtil
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

class AmorCeralytrasItem : CustomItem {

    companion object {
        private val boostCounter: MutableMap<UUID, Int> = mutableMapOf()
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#fd9a7f&lA&#fda583&lm&#fdb088&lo&#fdba8c&lr &#fdc590&lC&#fdd095&le&#fddb99&lr&#fddb9c&la&#fdd19f&ll&#fdc7a2&ly&#fdbea5&lt&#fdb4a7&lr&#fdaaaa&la&#fda0ad&ls",
            mutableListOf("&#FD9A7FEros"),
            mutableListOf("Allows the wearer to triple", "jump when sneaking midair", "", "Crouch and hold for 4 secs", "to activate a high boost"),
            Material.ELYTRA,
            mutableListOf("armorceralytras"),
            mutableMapOf(Enchantment.MENDING to 1, Enchantment.PROTECTION to 8, Enchantment.UNBREAKING to 10, Enchantment.FEATHER_FALLING to 5, Enchantment.PROJECTILE_PROTECTION to 4)
        )
        item.tier = "&#fb5a5a&lV&#fb6069&la&#fc6677&ll&#fc6c86&le&#fc7294&ln&#fd78a3&lt&#fd7eb2&li&#fb83be&ln&#f788c9&le&#f38dd4&ls &#f092df&l2&#ec97e9&l0&#e89cf4&l2&#e4a1ff&l4"
        return Pair("armorceralytras", item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.PLAYER_CROUCH -> {
                if (!player.isSneaking && AbilityUtil.isOnGround(player)) {
                    object : BukkitRunnable() {
                        override fun run() {
                            if (!player.isSneaking || !AbilityUtil.isOnGround(player)) {
                                if (boostCounter.contains(player.uniqueId) && boostCounter[player.uniqueId]!! >= 80) {
                                    player.velocity = player.velocity.multiply(8.2).setY(3.0)
                                }
                                boostCounter.remove(player.uniqueId)
                                cancel()
                                return
                            }

                            boostCounter[player.uniqueId] = boostCounter.getOrDefault(player.uniqueId, 0) + 5


                            if (boostCounter[player.uniqueId]!! % 80 == 0) {
                                player.world.spawnParticle(Particle.DUST, player.location, 40, 0.5, 0.0, 0.5, 0.5, DustOptions(Color.RED, 1f))
                                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
                            }
                        }
                    }.runTaskTimer(LumaItems.getInstance(), 0L, 5L)
                }

                if (player.isSneaking || player.isFlying || AbilityUtil.isOnGround(player) ||
                    (boostCounter.contains(player.uniqueId) && boostCounter[player.uniqueId]!! >= 2)) return false

                player.velocity = player.location.direction.multiply(0.6).setY(0.7)
                boostCounter[player.uniqueId] = boostCounter.getOrDefault(player.uniqueId, 0) + 1
            }
            Action.MOVE -> {
                if (!boostCounter.contains(player.uniqueId) || !AbilityUtil.isOnGround(player)) return false
                boostCounter.remove(player.uniqueId)
            }
            else -> return false
        }
        return true
    }

}