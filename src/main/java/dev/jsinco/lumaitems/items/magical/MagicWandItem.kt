package dev.jsinco.lumaitems.items.magical

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.particles.ParticleDisplay
import dev.jsinco.lumaitems.particles.Particles
import dev.jsinco.lumaitems.util.AbilityUtil
import dev.jsinco.lumaitems.util.Tier
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import java.awt.Color

class MagicWandItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        return ItemFactory.builder()
            .name("<b><#C7305D>M<#962F72>a<#642D87>g<#8D3A71>i<#B6475C>c <#C45078>W<#A94CAA>a<#A94CAA>n<#A94CAA>d</b>")
            .customEnchants("<dark_purple>Illusion")
            .lore("No lore yet")
            .material(Material.BLAZE_ROD)
            .persistentData("magicwand")
            .tier(Tier.CARNIVAL_2024)
            .vanillaEnchants(mutableMapOf(Enchantment.MENDING to 1))
            .buildPair()
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.RIGHT_CLICK, Action.LEFT_CLICK -> {
                spawnSpellBall(player)
            }

            Action.PROJECTILE_LAND -> {
                event as ProjectileHitEvent
                val entity = event.hitEntity as? LivingEntity ?: return false

                val particleDisplay = ParticleDisplay.of(Particle.DUST).withLocation(entity.location).withColor(Color.WHITE).mixWith(Color.PINK)
                Particles.meguminExplosion(INSTANCE, 4.0, particleDisplay)

                AbilityUtil.damageOverTicks(entity, player, 50.0, 5) {
                    entity.world.playSound(entity.location, Sound.ITEM_TOTEM_USE, 1.0f, 0.6f)
                }
            }
            else -> return false
        }
        return true
    }

    fun spawnSpellBall(player: Player) {
        val particleDisplay = ParticleDisplay.of(Particle.DUST).withColor(Color.WHITE).mixWith(Color.GREEN)
        AbilityUtil.spawnSpell(player, null, "magicwand", 150) {
            Particles.sphere(0.2, 4.0, particleDisplay.withLocation(it.location))
        }
    }
}