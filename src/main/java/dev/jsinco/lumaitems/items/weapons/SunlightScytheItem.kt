package dev.jsinco.lumaitems.items.weapons

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.util.AbilityUtil
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import java.util.function.Consumer

class SunlightScytheItem : CustomItem {

    companion object {
        val plugin: LumaItems = LumaItems.getPlugin()
        val cooldown: MutableList<Player> = mutableListOf()
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#f8a5a5&lS&#eb9fac&lu&#dd98b2&ln&#d092b9&ll&#c28bbf&li&#b585c6&lg&#a77ecc&lh&#9a78d3&lt &#8c71d9&lS&#7f6be0&lc&#7164e6&ly&#645eed&lt&#5657f3&lh&#4951fa&le",
            mutableListOf("&#4a52fdD&#6260f4a&#796debz&#917be1z&#a888d8l&#c096cfi&#d8a4c6n&#efb1bdg &#fab9b7S&#f9bcb4o&#f7bfb2l&#f5c1afs&#f4c4adt&#f2c7aai&#f1c9a8c&#efcca5e", "&#6369fbU&#7072f3n&#7c7bebd&#8984e4e&#968ddca&#a396d4d &#af9fccH&#bca8c4a&#c9b1bct&#d6bab5r&#e2c3ade&#efcca5d"),
            mutableListOf("§fThis weapon deals significantly more", "§fdamage to mobs that burn in the daylight","","§fRight click to unleash a dazzling barrage","§fof slashes","","§cCooldown: 8 secs"),
            Material.NETHERITE_HOE,
            mutableListOf("sunlightscythe"),
            mutableMapOf(Enchantment.DAMAGE_ALL to 9, Enchantment.DAMAGE_ARTHROPODS to 7, Enchantment.DURABILITY to 10, Enchantment.LOOT_BONUS_MOBS to 6, Enchantment.MENDING to 1)
            )
        return Pair("sunlightscythe", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        val entityDamageEvent: EntityDamageByEntityEvent? = event as? EntityDamageByEntityEvent

        when (type) {
            Ability.RIGHT_CLICK -> {
                sunLightScytheFinal(player)
            }
            Ability.ENTITY_DAMAGE -> {
                particles(entityDamageEvent!!.entity)
            }

            else -> return false
        }
        return true
    }

    private fun sunLightScytheFinal(p: Player) {
        if (cooldown.contains(p)) return
        val entity = p.getTargetEntity(65) as LivingEntity?
        val loc = if (p.getTargetBlockExact(10) != null) p.getTargetBlockExact(10)!!.location else p.location.add(
            p.location.getDirection().multiply(10)
        )
        if (entity != null) dazzlingSolstice(p, entity.location) else dazzlingSolstice(p, loc)

        // cooldown
        cooldown.add(p)
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
            { cooldown.remove(p) }, 160L
        )
    }

    private fun particles(entity: Entity?) {
        entity?.world?.spawnParticle(
            Particle.GLOW,
            entity.location.add(0.0, Random().nextDouble(2.0), 0.0),
            2,
            Random().nextDouble(1.5),
            Random().nextDouble(0.1),
            Random().nextDouble(1.5)
        )
    }

    private fun dazzlingSolstice(p: Player, loc: Location) {
        val entities: MutableList<LivingEntity> = ArrayList()
        loc.getWorld().getNearbyEntities(loc, 2.0, 2.5, 2.0).forEach(Consumer { e: Entity ->
            if (e is LivingEntity && !AbilityUtil.noDamagePermission(p, e) && e != p) {
                entities.add(e)
            }
        })
        entities.forEach(Consumer { livingEntity: LivingEntity ->
            livingEntity.damage(14.0, p)
            livingEntity.velocity = Vector(0, 0, 0)
            livingEntity.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 56, 3, false, false, false))
        })

        // runnables
        val slashes = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1.23f)
            loc.getWorld().spawnParticle(
                Particle.SWEEP_ATTACK,
                loc.clone().add(0.0, Random().nextDouble(2.0), 0.0),
                1,
                Random().nextDouble(1.5),
                Random().nextDouble(0.1),
                Random().nextDouble(1.5)
            )
        }, 0, 1)
        for (i in 0..5) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
                loc.getWorld().getNearbyEntities(loc, 2.0, 2.5, 2.0)
                    .forEach(Consumer { e: Entity ->
                        if (e is LivingEntity && AbilityUtil.noDamagePermission(p, e) && e != p) {
                            entities.add(e)
                        }
                    })
                entities.forEach(Consumer { livingEntity: LivingEntity ->
                    livingEntity.damage(14.0, p)
                })
                if (i == 5) Bukkit.getScheduler().cancelTask(slashes)
            }, (7 * (i + 1)).toLong())
        }
    }
}