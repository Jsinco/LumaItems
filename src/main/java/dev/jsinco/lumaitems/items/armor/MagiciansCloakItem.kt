package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.enums.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.particles.ParticleDisplay
import dev.jsinco.lumaitems.particles.Particles
import dev.jsinco.lumaitems.util.AbilityUtil
import dev.jsinco.lumaitems.enums.Tier
import dev.jsinco.lumaitems.enums.ToolType
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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class MagiciansCloakItem : CustomItem {

    private val colors = listOf(
        Util.hex2AwtColor("#8ec4f7"),
        Util.hex2AwtColor("#ff9ccb"),
        Util.hex2AwtColor("#d7f58d"),
        Util.hex2AwtColor("#fffe8a"),
        Util.hex2AwtColor("#ffd365")
    )
    private val vector0 = Vector(0, 0,0)
    private val key = NamespacedKey(INSTANCE, "magicianscloak")

    override fun createItem(): Pair<String, ItemStack> {
        return ItemFactory.builder()
            .name("<b><#FFDA80>M<#FFCF6B>a<#FFC457>g<#FFB842>i<#FFAD2D>c<#F6B55A>i<#EDBE87>a<#E4C6B4>n<#DBCEE1>'<#E3DBE7>s <#F4F4F4>C<#CECDE2>l<#A7A7D1>o<#8180BF>a<#5A59AD>k</b>")
            .customEnchants("<gradient:#8ec4f7:#ff9ccb>Piz</gradient><gradient:#ff9ccb:#8FF37F>za</gradient><gradient:#8FF37F:#ACB5FE>zz!</gradient>")
            .lore("Damage entities to charge up", "your cloak!", "", "Right-click any entity once fully", "charged to release a powerful", "attack spell.")
            .material(Material.ELYTRA)
            .persistentData("magicianscloak")
            .tier(Tier.CARNIVAL_2024)
            .vanillaEnchants(mutableMapOf(Enchantment.MENDING to 1))
            .buildPair()
    }

    override fun executeActions(type: Action, player: Player, event: Any): Boolean {
        when (type) {

            Action.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent
                if (event.isCancelled) return false
                val damage = event.damage.toInt().toShort()
                appendCloakDamage(player, damage)
                showDamageStars(player)
            }

            Action.RIGHT_CLICK -> {
                val target = player.getTargetEntity(15) as? LivingEntity ?: return false
                val itemInHand = ToolType.getToolType(player.inventory.itemInMainHand.type)

                if (getTotalDamage(player) < 500 || AbilityUtil.noDamagePermission(player, target) || (itemInHand != ToolType.TOOL && itemInHand != ToolType.WEAPON)) {
                    //showDamageStars(player)
                    return false
                }

                val particleDisplay = ParticleDisplay.of(Particle.DUST).withLocation(target.location).withColor(colors.random())
                Particles.meguminExplosion(INSTANCE, 5.0, particleDisplay)
                updateCloakDamage(player, 0)
                showDamageStars(player)

                val ticks = AbilityUtil.damageOverTicks(target, player, target.health / 3, 5) {
                    target.velocity = vector0
                    target.world.playSound(target.location, Sound.ITEM_TOTEM_USE, 1.0f, 0.6f)
                }


                target.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, ticks, 3, false, false, false))
            }

            else -> return false
        }
        return true
    }

    private fun appendCloakDamage(player: Player, amt: Short) {
        val item = player.equipment.chestplate ?: return
        val meta = item.itemMeta ?: return

        val currentDamage = meta.persistentDataContainer.get(key, PersistentDataType.SHORT) ?: 0
        if (currentDamage >= 500) return
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
        val stars = "★".repeat((damage / 100).coerceAtLeast(0)) + "☆".repeat((5 - damage / 100).coerceAtLeast(0))

        val color = colors.random()


        player.sendActionBar(Component.text(stars).color(TextColor.color(color.red, color.green, color.blue)))
    }
}