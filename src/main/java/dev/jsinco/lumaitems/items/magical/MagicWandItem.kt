package dev.jsinco.lumaitems.items.magical

import dev.jsinco.lumaitems.enums.Action
import dev.jsinco.lumaitems.enums.Tier
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.obj.MagicItemCooldown
import dev.jsinco.lumaitems.particles.ParticleDisplay
import dev.jsinco.lumaitems.particles.Particles
import dev.jsinco.lumaitems.shapes.Sphere
import dev.jsinco.lumaitems.util.AbilityUtil
import dev.jsinco.lumaitems.util.MiniMessageUtil
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.awt.Color
import java.util.UUID
import java.util.concurrent.ConcurrentLinkedQueue

class MagicWandItem : CustomItem {

    //private data class CoolDown(val playerUUID: UUID, val spell: Spell, val cooldownStart: Long)

    enum class Spell(val cooldownInSecs: Int) {
        BOOST_EFFECTS(30),
        LAUNCH(10),
        EXPLOSION_SPHERE(35),
        DRAIN(30),
        VALIANT_EXPLODE(40)
    }

    companion object {
        private const val SPELL_KEY = "magicwand_spell"
        private const val DEFAULT_SPELL = "BOOST_EFFECTS"
        private val vector0 = Vector(0, 0, 0)
        private val cooldowns: ConcurrentLinkedQueue<MagicItemCooldown> = ConcurrentLinkedQueue()
    }

    override fun createItem(): Pair<String, ItemStack> {
        return ItemFactory.builder()
            .name("<b><#C7305D>M<#962F72>a<#642D87>g<#8D3A71>i<#B6475C>c <#C45078>W<#A94CAA>a<#A94CAA>n<#A94CAA>d</b>")
            .customEnchants("<dark_purple>Illusion <gray>// <-- Change this")
            .lore("<dark_purple>Boost <dark_gray>- <white>Temporarily", "gain multiple buff effects.",
                "",
                "<dark_purple>Launch <dark_gray>- <white>Click to cast", "a spell which launches and", "damages nearby entities.",
                "",
                "<dark_purple>Clear <dark_gray>- <white>Cast a spell", "which clears out an area", "of blocks upon landing.")
            .material(Material.BLAZE_ROD)
            .persistentData("magicwand")
            .tier(Tier.CARNIVAL_2024)
            .vanillaEnchants(mutableMapOf(Enchantment.MENDING to 1))
            .stringPersistentDatas(mutableMapOf(NamespacedKey(INSTANCE, SPELL_KEY) to DEFAULT_SPELL))
            .buildPair()
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.RIGHT_CLICK, Action.LEFT_CLICK, Action.GENERIC_INTERACT -> {
                event as PlayerInteractEvent

                if (player.isSneaking && event.action.isRightClick) {
                    nextSpell(player, event.item ?: return false)
                    return true
                }

                val spell = event.item?.let { getSpell(it) } ?: return false

                if (cooldowns.any { it.playerUUID == player.uniqueId && (it.spellEnum as Spell) == spell }) {
                    return false
                }


                when (spell) {
                    Spell.BOOST_EFFECTS -> boostEffectsSpell(player)
                    Spell.LAUNCH -> spawnSpellBall(player, Spell.LAUNCH)
                    Spell.EXPLOSION_SPHERE -> spawnSpellBall(player, Spell.EXPLOSION_SPHERE)
                    Spell.DRAIN -> drainSpell(player)
                    Spell.VALIANT_EXPLODE -> spawnSpellBall(player, Spell.VALIANT_EXPLODE)
                }
            }

            Action.PROJECTILE_LAND -> {
                event as ProjectileHitEvent

                val spell = event.entity.persistentDataContainer.get(NamespacedKey(INSTANCE, SPELL_KEY), PersistentDataType.STRING)?.uppercase()?.let { Spell.valueOf(it) } ?: return false

                when (spell) {
                    Spell.LAUNCH -> launchSpell(player, event.hitBlock?.location ?: event.hitEntity?.location ?: return false)
                    Spell.EXPLOSION_SPHERE -> spawnExplosionSphere(event.hitBlock?.location ?: event.hitEntity?.location ?: return false, player)
                    Spell.VALIANT_EXPLODE -> valiantExplodeSpell(player, event.hitEntity as? LivingEntity ?: return false)
                    else -> return false
                }
            }

            Action.ASYNC_RUNNABLE -> {
                if (cooldowns.isEmpty()) return false

                val currentTime = System.currentTimeMillis()
                for (cooldown in cooldowns) {
                    if (cooldown.cooldown + ((cooldown.spellEnum as Spell).cooldownInSecs * 1000) < currentTime) {
                        cooldowns.remove(cooldown)
                    }
                }
            }

            else -> return false
        }
        return true
    }


    private fun spawnSpellBall(player: Player, spell: Spell) {
        val particleDisplay = ParticleDisplay.of(Particle.DUST).withColor(Color.WHITE).mixWith(Color.GREEN)
        AbilityUtil.spawnSpell(player, null, "magicwand", 150) {
            Particles.sphere(0.2, 4.0, particleDisplay.withLocation(it.location))
        }.also {
            it.persistentDataContainer.set(NamespacedKey(INSTANCE, SPELL_KEY), PersistentDataType.STRING, spell.name)
        }
    }


    private fun getSpell(item: ItemStack) =
        item.itemMeta?.persistentDataContainer?.get(NamespacedKey(INSTANCE, SPELL_KEY), PersistentDataType.STRING)?.uppercase()?.let { Spell.valueOf(it) }

    private fun nextSpell(player: Player, item: ItemStack) {
        val currentSpell = getSpell(item) ?: return
        val nextSpell = Spell.entries.let { spells ->
            val currentIndex = spells.indexOf(currentSpell)
            val size = if (!Util.isItemInSlots(BookOfKnowledgeItem.STRING_KEY, listOf(EquipmentSlot.OFF_HAND, EquipmentSlot.HAND, EquipmentSlot.HEAD), player)) {
                spells.size - 2
            } else {
                spells.size
            }
            val nextIndex = if (currentIndex >= size - 1) 0 else currentIndex + 1
            spells[nextIndex]
        }
        item.itemMeta = item.itemMeta?.apply {
            persistentDataContainer.set(NamespacedKey(INSTANCE, SPELL_KEY), PersistentDataType.STRING, nextSpell.name)
        }
        MiniMessageUtil.msg(player, "Spell changed to <dark_purple>${Util.formatMaterialName(nextSpell.name)}")
    }

    // Spells

    private fun boostEffectsSpell(player: Player) {
        cooldowns.add(MagicItemCooldown(player.uniqueId, Spell.BOOST_EFFECTS, System.currentTimeMillis()))
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 500, 2))
        player.addPotionEffect(PotionEffect(PotionEffectType.JUMP_BOOST, 500, 2))
        player.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 500, 2))
    }

    private fun launchSpell(player: Player, loc: Location) {
        cooldowns.add(MagicItemCooldown(player.uniqueId, Spell.LAUNCH, System.currentTimeMillis()))
        val particleDisplay = ParticleDisplay.of(Particle.DUST).withColor(Color.WHITE).mixWith(Color.YELLOW)
        for (livingEntity in loc.getNearbyLivingEntities(5.0)) {
            if (AbilityUtil.noDamagePermission(player, livingEntity)) continue
            object : BukkitRunnable() {
                var i = 0
                override fun run() {
                    if (i++ > 2) {
                        cancel()
                    }
                    livingEntity.velocity = livingEntity.location.toVector().subtract(loc.toVector()).add(Vector(0.0,5.0,0.0)).multiply(23.5).normalize()

                    Particles.line(livingEntity.location, loc, 0.2, particleDisplay)
                    livingEntity.damage(5.0)
                }
            }.runTaskTimer(INSTANCE, 0, 2)
        }
    }

    private fun spawnExplosionSphere(loc: Location, player: Player) {
        if (AbilityUtil.noBuildPermission(player, loc.block)) return
        cooldowns.add(MagicItemCooldown(player.uniqueId, Spell.EXPLOSION_SPHERE, System.currentTimeMillis()))
        val sphere = Sphere(loc, 4.0, 9.0)
        for (block in sphere.sphere) {
            block.breakNaturally(false, true)
        }
        loc.world.spawnParticle(Particle.EXPLOSION, loc, 1, 0.0, 0.0, 0.0, 0.0)
        loc.world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)
    }


    private fun drainSpell(player: Player) {
        cooldowns.add(MagicItemCooldown(player.uniqueId, Spell.DRAIN, System.currentTimeMillis()))
        val particleDisplay = ParticleDisplay.of(Particle.DUST).withColor(Color.WHITE).mixWith(Color.RED)
        val entities: List<LivingEntity> = player.getNearbyEntities(15.0, 15.0,15.0).filterIsInstance<LivingEntity>()
        var i = 0
        object : BukkitRunnable() {
            override fun run() {
                if (i++ > 5) {
                    cancel()
                    return
                }

                for (entity in entities) {
                    if (AbilityUtil.noDamagePermission(player, entity) || entity.isDead) {
                        continue
                    }

                    entity.damage(2.0)
                    entity.world.playSound(entity.location, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f)
                    player.heal(2.0)
                    Particles.line(player.boundingBox.center.toLocation(player.world), entity.boundingBox.center.toLocation(entity.world), 0.2, particleDisplay)
                }
            }
        }.runTaskTimer(INSTANCE, 0, 20)
    }

    private fun valiantExplodeSpell(player: Player, target: LivingEntity) {
        if (AbilityUtil.noDamagePermission(player, target)) return
        cooldowns.add(MagicItemCooldown(player.uniqueId, Spell.VALIANT_EXPLODE, System.currentTimeMillis()))
        val particleDisplay = ParticleDisplay.of(Particle.DUST).withColor(Color.WHITE).mixWith(Color.CYAN).withLocation(target.location)


        AbilityUtil.damageOverTicks(target, player, target.health / 3, 4, {
            target.velocity = vector0
            target.world.playSound(target.location, Sound.ITEM_TOTEM_USE, 1.0f, 7f)
        }, {
            if (!AbilityUtil.noBuildPermission(player, target.location.block)) {
                target.world.spawnParticle(Particle.FLAME, target.location, 25, 0.5, 0.5, 0.5, 0.5)
                target.world.spawnParticle(Particle.EXPLOSION, target.location, 1, 0.0, 0.0, 0.0, 0.0)
                target.world.createExplosion(target.location, 7.0f, true, true, player)
            }
        })
        Particles.meguminExplosion(INSTANCE, 5.0, particleDisplay)
        target.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 40, 50, false, false, false))
    }
}