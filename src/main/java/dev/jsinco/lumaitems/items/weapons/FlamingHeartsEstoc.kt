package dev.jsinco.lumaitems.items.weapons

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.manager.GlowManager
import dev.jsinco.lumaitems.util.AbilityUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Fireball
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*

class FlamingHeartsEstoc : CustomItem {
    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
        private val cooldown: MutableSet<UUID> = mutableSetOf()
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#fb4863&lF&#fb4e62&ll&#fb5562&la&#fc5b61&lm&#fc6260&li&#fc685f&ln&#fc6f5f&lg &#fd755e&lH&#fd7c5d&le&#fd825c&la&#fd8859&lr&#fd8d57&lt&#fd9355&ls &#fd9952&lE&#fd9f50&ls&#fda44e&lt&#fdaa4b&lo&#fdb049&lc",
            mutableListOf("&#fd9355Barrock", "&#fd9355Rumble"),
            mutableListOf("Grants resistance at the cost", "of slowness whilst attacking", "", "Right-click to send out an incendiary", "that explodes on impact", "", "&cCooldown: 30s"),
            Material.NETHERITE_SWORD,
            mutableListOf("flamingheartssword"),
            mutableMapOf(Enchantment.MENDING to 1, Enchantment.DAMAGE_ALL to 8, Enchantment.FIRE_ASPECT to 4, Enchantment.DAMAGE_UNDEAD to 7, Enchantment.DURABILITY to 10,
                Enchantment.LOOT_BONUS_MOBS to 4, Enchantment.SWEEPING_EDGE to 4)
        )
        item.tier = "&#fb5a5a&lV&#fb6069&la&#fc6677&ll&#fc6c86&le&#fc7294&ln&#fd78a3&lt&#fd7eb2&li&#fb83be&ln&#f788c9&le&#f38dd4&ls &#f092df&l2&#ec97e9&l0&#e89cf4&l2&#e4a1ff&l4"
        return Pair("flamingheartssword", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RIGHT_CLICK -> {
                if (cooldown.contains(player.uniqueId)) return false

                val fireBall: Fireball = player.launchProjectile(Fireball::class.java)
                fireBall.yield = 0.0f
                fireBall.persistentDataContainer.set(NamespacedKey(plugin, "flamingheartssword"), PersistentDataType.SHORT, 1)
                GlowManager.setGlowColor(fireBall, ChatColor.DARK_RED)
                fireBall.isGlowing = true
                fireBall.setIsIncendiary(false)


                cooldown.add(player.uniqueId)
                plugin.server.scheduler.runTaskLater(plugin, Runnable {
                    cooldown.remove(player.uniqueId)
                    if (fireBall.isValid) {
                        fireBall.remove()
                    }
                }, 600L)
            }

            Ability.PROJECTILE_LAND -> {
                event as ProjectileHitEvent

                event.entity.getNearbyEntities(7.3, 7.3, 7.3).forEach {
                    if (it !is LivingEntity || it == player || AbilityUtil.noDamagePermission(player, it)) return@forEach
                    // knockback entity away from fireball
                    it.velocity = it.location.toVector().subtract(event.entity.location.toVector()).add(Vector(0.0,5.0,0.0)).multiply(23.5).normalize()
                    it.fireTicks = 100
                    it.damage(25.0, player)
                }
                event.entity.world.playSound(event.entity.location, Sound.ENTITY_WITHER_SHOOT, 1.0f, 2.0f)
                event.entity.world.spawnParticle(Particle.FLAME, event.entity.location, 25, 0.5, 0.5, 0.5, 0.5)
                event.entity.world.spawnParticle(Particle.EXPLOSION_HUGE, event.entity.location, 1, 0.0, 0.0, 0.0, 0.0)
                event.entity.world.playSound(event.entity.location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 2.0f)
            }
            Ability.ENTITY_DAMAGE -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 220, 3, false, false, true))
                player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 220, 0, false, false, true))
            }
            else -> return false
        }
        return true
    }

}