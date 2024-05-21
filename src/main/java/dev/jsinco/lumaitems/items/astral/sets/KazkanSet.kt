package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.util.AbilityUtil
import dev.jsinco.lumaitems.util.GenericMCToolType
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class KazkanSet : AstralSet {

    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()

        private val holdingPlayers: ConcurrentLinkedQueue<ClickHoldingPlayer> = ConcurrentLinkedQueue()
        private val playerLinkedArrows: ConcurrentHashMap<Player, List<Projectile>> = ConcurrentHashMap()
    }

    override fun setItems(): List<ItemStack> {
        val factory = AstralSetFactory("Kazkan", mutableListOf("&#AC87FBInvigorated"))

        factory.commonEnchants = mutableMapOf(
            Enchantment.DURABILITY to 7,
        )

        factory.astralSetItem(
            Material.IRON_AXE,
            mutableMapOf(Enchantment.DAMAGE_ALL to 6, Enchantment.LOOT_BONUS_MOBS to 3, Enchantment.DAMAGE_ARTHROPODS to 5),
            mutableListOf("Right-click and hold to begin", "charging up this weapon's", "power.", "", "Attack damage increases", "with time spent charging.")
        )

        factory.astralSetItem(
            Material.CROSSBOW,
            mutableMapOf(Enchantment.QUICK_CHARGE to 2, Enchantment.PIERCING to 4),
            mutableListOf("Arrows launched from this bow", "will be frozen in place.", "", "Left-click to launch them in", "the direction you're looking."),
            includeCommonEnchants = true,
            customName = null,
            attributeModifiers = null,
            customEnchants = listOf("&#AC87FBTime Dilation")
        )

        factory.astralSetItem(
            Material.BLAZE_ROD,
            mutableMapOf(),
            mutableListOf("&7Right-click &fon entities", "slow them down for a short", "period of time.", "", "&7Left-click &fto cast a spell", "that will damage enemies.", "", "&c13 Lapis per spell"),
            includeCommonEnchants = true,
            customName = "&#AC87FB&lKazkan &fStaff",
            attributeModifiers = null,
            customEnchants = null
        )

        factory.astralSetItem(
            Material.SHIELD,
            mutableMapOf(Enchantment.MENDING to 1, Enchantment.FIRE_ASPECT to 4),
            mutableListOf(),
            includeCommonEnchants = true,
            attributeModifiers = null,
            customName = null,
            customEnchants = null
        )

        return factory.createdAstralItems
    }

    override fun identifier(): String {
        return "kazkan-set"
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        val genericMCToolType: GenericMCToolType = GenericMCToolType.getToolType(player.inventory.itemInMainHand) ?: return false

        when (type) {
            Ability.RIGHT_CLICK -> {
                if (genericMCToolType == GenericMCToolType.AXE) {
                    val holdingPlayer = holdingPlayers.find { it.player.uniqueId == player.uniqueId }
                        ?: ClickHoldingPlayer(player).also { holdingPlayers.add(it) }
                    holdingPlayer.updateClickTime()
                } else if (genericMCToolType == GenericMCToolType.MAGICAL) {
                    val entity = player.getTargetEntity(15) as? LivingEntity ?: return false
                    if (entity !is Player && AbilityUtil.takeSpellLapisCost(player, 13)) {
                        entity.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 200, 30, false, false, false))
                    }
                }
            }

            Ability.LEFT_CLICK -> {
                if (genericMCToolType == GenericMCToolType.CROSSBOW) {
                    handleArrowFiring(player)
                } else if (genericMCToolType == GenericMCToolType.MAGICAL) {
                    if (AbilityUtil.takeSpellLapisCost(player, 13)) {
                        AbilityUtil.spawnSpell(player, Particle.SPELL_WITCH, "kazkan-set", 120L)
                    }
                }

            }

            Ability.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent
                if (genericMCToolType != GenericMCToolType.AXE) return false
                val holdingPlayer = holdingPlayers.find { it.player.uniqueId == player.uniqueId } ?: return false
                if (!holdingPlayer.isHolding()) return false

                event.damage += (holdingPlayer.totalUpdates / 4).coerceAtMost(14)
            }

            Ability.ASYNC_RUNNABLE -> {
                for (holdingPlayer in holdingPlayers) {
                    if (!holdingPlayer.isHolding()) {
                        holdingPlayers.remove(holdingPlayer)
                    }
                }
                for (linkedArrowEntry in playerLinkedArrows) {
                    for (arrow in linkedArrowEntry.value) {
                        if (arrow.isDead) {
                            playerLinkedArrows.remove(linkedArrowEntry.key)
                        }
                        if (linkedArrowEntry.value.isEmpty()) {
                            playerLinkedArrows.remove(linkedArrowEntry.key)
                        }
                    }
                }
            }

            Ability.PROJECTILE_LAUNCH -> {
                event as ProjectileLaunchEvent

                val projectile = event.entity as? Arrow ?: return false

                projectile.velocity = projectile.velocity.multiply(0.01)
                projectile.setGravity(false)
                projectile.isPersistent = false
                projectile.isCritical = false

                val linkedArrows: MutableList<Projectile> = playerLinkedArrows[player]?.toMutableList() ?: mutableListOf()
                if (linkedArrows.size >= 5) {
                    linkedArrows[0].setGravity(true)
                    linkedArrows.removeAt(0)
                }

                playerLinkedArrows[player] = linkedArrows.plus(projectile)

                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    if (!projectile.isOnGround) {
                        projectile.setGravity(true)
                        val arrows = playerLinkedArrows[player]?.toMutableList() ?: return@Runnable
                        arrows.remove(projectile)
                        playerLinkedArrows[player] = arrows
                    }
                }, 150)
            }

            Ability.PROJECTILE_LAND -> {
                event as ProjectileHitEvent
                val entity = event.hitEntity as? LivingEntity ?: return false
                if (AbilityUtil.noDamagePermission(player, entity)) return false

                entity.world.playSound(entity.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1f, 1f)
                entity.world.playSound(entity.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f)
                entity.world.spawnParticle(Particle.SOUL_FIRE_FLAME, entity.eyeLocation, 20, 0.5, 0.5, 0.5, 0.5)
                entity.world.spawnParticle(Particle.REDSTONE, entity.eyeLocation, 10, 0.4, 0.4, 0.4, 0.8,
                    Particle.DustOptions(Util.hex2BukkitColor("#AC87FB"), 2f)
                )

                entity.damage(17.0, player)
            }

            else -> return false
        }

        return true
    }

    fun adjustArrowDirection(arrow: Projectile, player: Player) {
        // adjust arrows direction based on where player is looking
        // loc.setDirection(player.location.toVector().subtract(armorStand.location.toVector()).normalize());
        val targetEntity = player.getTargetEntity(75)

        val direction: Vector = if (targetEntity != null) {
            val targetLocation = targetEntity.location
            targetLocation.toVector().subtract(arrow.location.toVector()).normalize()
        } else {
            player.location.direction
        }
        arrow.location.yaw = player.location.yaw
        arrow.location.pitch = player.location.pitch
        arrow.velocity = direction.multiply(3.2)
        arrow.setGravity(true)
    }

    fun handleArrowFiring(player: Player) {
        val linkedArrows = playerLinkedArrows[player] ?: return
        val arrow = if (linkedArrows.isNotEmpty()) linkedArrows.first() else {
            playerLinkedArrows.remove(player)
            return
        }
        adjustArrowDirection(arrow, player)
        playerLinkedArrows[player] = linkedArrows.drop(1)
    }

}

private class ClickHoldingPlayer(val player: Player) {
    val initialClickTime: Long = System.currentTimeMillis()

    var lastClickTime: Long = initialClickTime
    var totalClickTime: Long = lastClickTime - initialClickTime
    var totalUpdates = 0

    fun updateClickTime() {
        lastClickTime = System.currentTimeMillis()
        totalClickTime = lastClickTime - initialClickTime
        totalUpdates++
    }

    fun isHolding(): Boolean {
        return System.currentTimeMillis() - lastClickTime < 300
    }
}