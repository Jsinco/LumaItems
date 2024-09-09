package dev.jsinco.lumaitems.items.weapons

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.enums.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.enums.Tier
import dev.jsinco.lumaitems.util.NeedsEdits
import dev.jsinco.lumaitems.util.Util
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

@NeedsEdits
class GiantInflatableHammerItem : CustomItem {

    companion object {
        private val queuedEntityDamages: MutableList<DamageStore> = mutableListOf()

        private val colors = listOf(
            Util.hex2BukkitColor("#fca2ab"),
            Util.hex2BukkitColor("#fccba8"),
            Util.hex2BukkitColor("#fbfcb5"),
            Util.hex2BukkitColor("#b0fc9f"),
            Util.hex2BukkitColor("#a3f1fc")
        )

        private val key = NamespacedKey(LumaItems.getInstance(), "giantinflatablehammer")
    }




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

    override fun executeActions(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent

                val entity = event.entity as? LivingEntity ?: return false

                if (event.isCritical && !entity.persistentDataContainer.has(key, PersistentDataType.SHORT) && entity.health > event.damage) {
                    entity.world.spawnParticle(
                        Particle.DUST, entity.eyeLocation, 100, 0.3, -1.5, 0.3, 0.1, Particle.DustOptions(colors.random(), 1f)
                    )
                    addQueueEntityDamage(player, entity, event.damage.toFloat())

                    Bukkit.getScheduler().runTaskLater(INSTANCE, Runnable {
                        val damageStore = getQueuedEntityDamage(entity) ?: return@Runnable
                        damageStore.executeAndRemove(player, entity)
                    }, 100L)
                } else if (entity.persistentDataContainer.has(key, PersistentDataType.SHORT)) {
                    updateQueuedEntityDamage(entity, event.damage.toFloat())
                    event.isCancelled = true
                }

            }

            Action.ENTITY_MOVE -> {
                event as EntityMoveEvent
                event.isCancelled = true
            }

            Action.RIGHT_CLICK -> {
                for (damageStore in getQueuedEntityDamages(player)) {
                    damageStore.executeAndRemove(player)
                }
            }

            else -> return false
        }
        return true
    }

    private fun addQueueEntityDamage(player: Player, entity: LivingEntity, damage: Float) {
        entity.persistentDataContainer.set(key, PersistentDataType.SHORT, 1)
        queuedEntityDamages.add(DamageStore(player.uniqueId, entity.uniqueId, damage))
    }

    private fun getQueuedEntityDamages(player: Player) = queuedEntityDamages.filter { it.player == player.uniqueId }
    private fun getQueuedEntityDamage(entity: LivingEntity) = queuedEntityDamages.find { it.entity == entity.uniqueId }

    private fun updateQueuedEntityDamage(entity: LivingEntity, damage: Float) {
        val damageStore = queuedEntityDamages.find { it.entity == entity.uniqueId } ?: return
        damageStore.updateDamage(damage)
    }

    private data class DamageStore(val player: UUID, val entity: UUID, var damage: Float) {

        fun updateDamage(damage: Float) {
            this.damage += damage
        }

        fun executeAndRemove(player: Player, entity: LivingEntity) {
            if (!entity.isDead) {
                entity.persistentDataContainer.remove(key)
                entity.damage(damage.toDouble(), player)
                entity.world.spawnParticle(
                    Particle.DUST, entity.eyeLocation, 100, 0.3, -1.5, 0.3, 0.1, Particle.DustOptions(Color.MAROON, 1f)
                )
            }
            queuedEntityDamages.remove(this)
        }

        fun executeAndRemove(player: Player) {
            val entity = Bukkit.getEntity(entity) as? LivingEntity ?: return
            executeAndRemove(player, entity)
        }

        fun executeAndRemove() {
            val entity = Bukkit.getEntity(entity) as? LivingEntity ?: return
            Bukkit.getPlayer(player)?.let { executeAndRemove(it, entity) }
            queuedEntityDamages.remove(this)
        }
    }
}