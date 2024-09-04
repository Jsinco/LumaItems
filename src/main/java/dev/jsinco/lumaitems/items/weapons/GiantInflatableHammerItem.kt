package dev.jsinco.lumaitems.items.weapons

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.AbilityUtil
import dev.jsinco.lumaitems.util.Tier
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

class GiantInflatableHammerItem : CustomItem {

    val key = NamespacedKey(INSTANCE, "giantinflatablehammer")
    val colors = listOf(
        Util.hex2BukkitColor("#fca2ab"),
        Util.hex2BukkitColor("#fccba8"),
        Util.hex2BukkitColor("#fbfcb5"),
        Util.hex2BukkitColor("#b0fc9f"),
        Util.hex2BukkitColor("#a3f1fc")
    )

    override fun createItem(): Pair<String, ItemStack> {
        return ItemFactory.builder()
            .name("<b><#FFA4AD>G<#FFABAD>i<#FFB2AC>a<#FFB9AC>n<#FFBFAB>t <#FFCDAA>I<#FFD7AD>n<#FFE1AF>f<#FEEBB2>l<#FEF5B4>a<#FEFFB7>t<#F1FFB3>a<#E5FFB0>b<#D8FFAC>l<#CBFFA8>e <#B2FFA1>H<#AFFDB4>a<#ADFBC7>m<#AAF8D9>m<#A8F6EC>e<#A5F4FF>r</b>")
            .customEnchants("<#FFA4AD>Astonish")
            .lore("No lore yet")
            .material(Material.NETHERITE_AXE)
            .persistentData("giantinflatablehammer")
            .tier(Tier.CARNIVAL_2024)
            .vanillaEnchants(mutableMapOf(Enchantment.MENDING to 1))
            .buildPair()
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent

                val entity = event.entity as? LivingEntity ?: return false

                if (AbilityUtil.noDamagePermission(player, entity) || !event.isCritical || !entity.hasAI()) return false

                entity.setAI(false)
                entity.world.spawnParticle(Particle.DUST, entity.eyeLocation, 300, 0.2, -1.5, 0.2, 0.1, Particle.DustOptions(colors.random(), 1f))
                event.isCancelled = true

                Bukkit.getScheduler().runTaskLaterAsynchronously(INSTANCE, Runnable {
                    entity.setAI(true)
                }, 100)
            }

            else -> return false
        }
        return true
    }
}