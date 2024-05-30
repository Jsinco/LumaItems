package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.DefaultAttributes
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.UUID

class PrideCrownItem : CustomItem {

    companion object {
        val colors: List<DustOptions> = listOf(
            DustOptions(Util.hex2BukkitColor("#F36B6B"), 0.6f),
            DustOptions(Util.hex2BukkitColor("#F3B36B"), 0.6f),
            DustOptions(Util.hex2BukkitColor("#F3EA6B"), 0.6f),
            DustOptions(Util.hex2BukkitColor("#89E280"), 0.6f),
            DustOptions(Util.hex2BukkitColor("#7485E3"), 0.6f),
            DustOptions(Util.hex2BukkitColor("#A374E3"), 0.6f),
            DustOptions(Util.hex2BukkitColor("#D179DE"), 0.6f),
        )
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#BA6BF3&lP&#BE6FF2&lr&#C273F1&li&#C778F0&ld&#CB7CEF&le &#CF80ED&lC&#D384EC&lr&#D889EB&lo&#DC8DEA&lw&#E091E9&ln",
            mutableListOf(),
            mutableListOf(),
            Material.LARGE_AMETHYST_BUD,
            mutableListOf("pridecrown"),
            mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 8)
        )
        item.autoHat = true
        item.tier = "&#731385&lP&#4332B9&lr&#1351ED&li&#0C6A87&ld&#058221&le &#7FB715&l2&#F9EB08&l0&#EF7A05&l2&#E40902&l4"
        item.attributeModifiers = DefaultAttributes.NETHERITE_HELMET.appendThenGetAttributes(
            Attribute.GENERIC_MAX_HEALTH, AttributeModifier(UUID.randomUUID(),"genericMaxHealth", 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD)
        )
        return Pair("pridecrown", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                for (element in colors) {
                    player.world.spawnParticle(
                        Particle.REDSTONE, player.eyeLocation.add(0.0, 0.49, 0.0), 2, 0.2, 0.0, 0.2, element
                    )
                }
            }
            else -> return false
        }
        return true
    }
}