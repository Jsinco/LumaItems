package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.particles.ParticleDisplay
import dev.jsinco.lumaitems.particles.Particles
import dev.jsinco.lumaitems.util.AbilityUtil
import dev.jsinco.lumaitems.util.Tier
import dev.jsinco.lumaitems.util.Util
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class MagiciansCloakItem : CustomItem {

    val colors = listOf(
        Util.hex2AwtColor("#8ec4f7"),
        Util.hex2AwtColor("#ff9ccb"),
        Util.hex2AwtColor("#d7f58d"),
        Util.hex2AwtColor("#fffe8a"),
        Util.hex2AwtColor("#ffd365")
    )

    val key = NamespacedKey(INSTANCE, "magicianscloak")

    override fun createItem(): Pair<String, ItemStack> {
        return ItemFactory.builder()
            .name("<b><#FFDA80>M<#FFCF6B>a<#FFC457>g<#FFB842>i<#FFAD2D>c<#F6B55A>i<#EDBE87>a<#E4C6B4>n<#DBCEE1>'<#E3DBE7>s <#F4F4F4>C<#CECDE2>l<#A7A7D1>o<#8180BF>a<#5A59AD>k</b>")
            .customEnchants("<gradient:#8ec4f7:#ff9ccb>Piz</gradient><gradient:#ff9ccb:#8FF37F>za</gradient><gradient:#8FF37F:#ACB5FE>zz!</gradient>")
            .lore("No lore yet")
            .material(Material.ELYTRA)
            .persistentData("magicianscloak")
            .tier(Tier.CARNIVAL_2024)
            .vanillaEnchants(mutableMapOf(Enchantment.MENDING to 1))
            .buildPair()
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {

            Action.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent
                val damage = event.damage.toInt().toShort()
                appendCloakDamage(player, damage)
                showDamageStars(player)
            }

            Action.RIGHT_CLICK -> {
                val target = player.getTargetEntity(15) as? LivingEntity ?: return false

                if (getTotalDamage(player) < 100 || AbilityUtil.noDamagePermission(player, target)) {
                    showDamageStars(player)
                    return false
                }

                val particleDisplay = ParticleDisplay.of(Particle.DUST).withLocation(target.location).withColor(colors.random())
                Particles.meguminExplosion(INSTANCE, 5.0, particleDisplay)
                updateCloakDamage(player, 0)
                showDamageStars(player)

                target.world.playSound(target.location, Sound.ITEM_TOTEM_USE, 0.9f, 1.2f)
                target.damage(target.health / 2, player)
            }

            else -> return false
        }
        return true
    }

    private fun appendCloakDamage(player: Player, amt: Short) {
        val item = player.equipment.chestplate ?: return
        val meta = item.itemMeta ?: return

        val currentDamage = meta.persistentDataContainer.get(key, PersistentDataType.SHORT) ?: 0
        if (currentDamage >= 100) return
        meta.persistentDataContainer.set(key, PersistentDataType.SHORT, (currentDamage + amt).toShort())
        item.itemMeta = meta
    }

    private fun updateCloakDamage(player: Player, amt: Short) {
        val item = player.equipment.chestplate ?: return
        val meta = item.itemMeta ?: return

        meta.persistentDataContainer.set(key, PersistentDataType.SHORT, amt)
        item.itemMeta = meta
    }

    private fun getTotalDamage(player: Player): Short {
        val item = player.equipment.chestplate ?: return 0
        val meta = item.itemMeta ?: return 0
        return meta.persistentDataContainer.get(key, PersistentDataType.SHORT) ?: 0
    }

    private fun showDamageStars(player: Player) {
        val damage = getTotalDamage(player)
        val stars = "★".repeat(damage / 20) + "☆".repeat(5 - damage / 20)

        val color = colors.random()


        player.sendActionBar(Component.text(stars).color(TextColor.color(color.red, color.green, color.blue)))
    }
}