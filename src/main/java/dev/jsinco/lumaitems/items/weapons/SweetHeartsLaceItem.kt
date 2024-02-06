package dev.jsinco.lumaitems.items.weapons

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Enemy
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class SweetHeartsLaceItem : CustomItem {

    companion object {
        private val p: LumaItems = LumaItems.getPlugin()
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#fb5ab6&lS&#fb5db5&lw&#fb61b4&le&#fb64b3&le&#fc68b1&lt&#fc6bb0&lH&#fc6eaf&le&#fc72ae&la&#fc75ad&lr&#fc79ac&lt&#fc7cab&l'&#fc7faa&ls &#fd83a8&lL&#fd86a7&la&#fd8aa6&lc&#fd8da5&le",
            mutableListOf("&#FB5AB6Cupid"),
            mutableListOf("Shot enemies may be", "temporarily charmed, causing them", "to briefly become passive"),
            Material.BOW,
            mutableListOf("sweetheartslace"),
            mutableMapOf(Enchantment.ARROW_DAMAGE to 7, Enchantment.ARROW_KNOCKBACK to 3, Enchantment.ARROW_INFINITE to 1, Enchantment.DURABILITY to 10, Enchantment.MENDING to 1)
        )
        item.tier = "&#fb5a5a&lV&#fb6069&la&#fc6677&ll&#fc6c86&le&#fc7294&ln&#fd78a3&lt&#fd7eb2&li&#fb83be&ln&#f788c9&le&#f38dd4&ls &#f092df&l2&#ec97e9&l0&#e89cf4&l2&#e4a1ff&l4"

        return Pair("sweetheartslace", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.PROJECTILE_LAUNCH -> {
                event as ProjectileLaunchEvent
                val nearbyWatchers = player.getNearbyEntities(50.0, 50.0, 50.0).mapNotNull { it as? Player }

                val snowball: Snowball = event.entity.location.world.spawn(event.entity.location, Snowball::class.java)
                snowball.velocity = event.entity.velocity
                event.entity.remove() // Can remove after we pull its location and velocity
                snowball.setGravity(false)

                //player.hideEntity(p, snowball)
                //for (watcher in nearbyWatchers) {
                //    watcher.hideEntity(p, snowball)
                //}
                snowball.persistentDataContainer.set(NamespacedKey(p, "sweetheartslace"), PersistentDataType.SHORT, 1)
                snowball.shooter = player
                object : BukkitRunnable() {
                    override fun run() {
                        if (snowball.isDead || snowball.ticksLived > 200) {
                            this.cancel()
                            if (!snowball.isDead) snowball.remove()
                            return
                        }
                        snowball.world.spawnParticle(Particle.HEART, snowball.location, 2, 0.3, 0.2, 0.3, 0.3)
                    }
                }.runTaskTimer(p, 0L, 1L);

            }
            Ability.PROJECTILE_LAND -> {
                event as ProjectileHitEvent
                val entity = event.hitEntity as? LivingEntity ?: return false


                entity.world.spawnParticle(Particle.SPELL_WITCH, entity.location, 30, 0.5, 0.5, 0.5, 0.5)

                if (entity is Enemy && Random.nextInt(100) <= 40) {
                    val nameSpace = NamespacedKey(p, "sweetheartslace")
                    entity.persistentDataContainer.set(nameSpace, PersistentDataType.SHORT, 1.toShort())
                    Bukkit.getScheduler().scheduleSyncDelayedTask(p, {
                        if (entity.isDead) return@scheduleSyncDelayedTask
                        entity.persistentDataContainer.remove(nameSpace)
                    }, 600L)
                }
            }
            Ability.ENTITY_TARGET_LIVING_ENTITY -> {
                event as EntityTargetLivingEntityEvent
                event.isCancelled = true
            }
            Ability.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent
                event.damage += 7.0
            }

            else -> return false
        }
        return true
    }

}