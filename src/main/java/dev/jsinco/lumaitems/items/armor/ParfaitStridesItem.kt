package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.DefaultAttributes
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID

class ParfaitStridesItem : CustomItem {

    companion object {
        private val plugin: LumaItems = LumaItems.getInstance()
        private val cooldown: MutableSet<UUID> = mutableSetOf()
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#9a8a88&lP&#b19a95&la&#c7a9a3&lr&#deb9b0&lf&#f4c9be&la&#fcd1c6&li&#fcd6cb&lt &#fddcd0&lS&#fde1d5&lt&#fde5da&lr&#fbe8dd&li&#faebe1&ld&#f9eee4&le&#f8f1e7&ls",
            mutableListOf("&#FCCEC2Spillover", "&#FCCEC2Beacon"),
            mutableListOf("Passively grants the wearer", "extra health while wearing", "", "While wearing, crouch and", "right-click to grant all nearbys", "a buff for a short duration", "", "&cCooldown: 1m"),
            Material.NETHERITE_LEGGINGS,
            mutableListOf("parfaitstrides"),
            mutableMapOf(Enchantment.BLAST_PROTECTION to 8, Enchantment.PROTECTION to 7, Enchantment.UNBREAKING to 8, Enchantment.MENDING to 1)
        )
        item.tier = "&#fb5a5a&lV&#fb6069&la&#fc6677&ll&#fc6c86&le&#fc7294&ln&#fd78a3&lt&#fd7eb2&li&#fb83be&ln&#f788c9&le&#f38dd4&ls &#f092df&l2&#ec97e9&l0&#e89cf4&l2&#e4a1ff&l4"
        item.attributeModifiers = DefaultAttributes.NETHERITE_LEGGINGS.appendThenGetAttributes(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier(UUID.randomUUID(), "genericMaxHealth", 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS)
        )

        return Pair("parfaitstrides", item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.RIGHT_CLICK -> {
                if (cooldown.contains(player.uniqueId) || !player.isSneaking) return false

                val nearbyPlayers: MutableList<Player> = player.getNearbyEntities(10.0, 10.0, 10.0).mapNotNull { it as? Player }.toMutableList()
                nearbyPlayers.add(player)

                for (nearbyPlayer in nearbyPlayers) {
                    nearbyPlayer.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 450, 1))
                    nearbyPlayer.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 450, 0))
                    nearbyPlayer.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 450, 1))
                }

                cooldown.add(player.uniqueId)
                plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                    cooldown.remove(player.uniqueId)
                }, 1200L)
            }

            else -> return false
        }
        return true
    }
}