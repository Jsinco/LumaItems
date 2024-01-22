package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class SapphireShardItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#ba6aec&lS&#be77e8&la&#c185e4&lp&#c592e0&lp&#d5a7e8&lh&#e6bbf1&li&#f6d0f9&lr&#e6bbf1&le &#d5a7e8&lS&#c592e0&lh&#c185e4&la&#be77e8&lr&#ba6aec&ld",
            mutableListOf("&#ba6aecG&#c074edl&#c67eefa&#cc89f0s&#d293f1s&#d89df3c&#dea7f4u&#e4b1f5t&#eabcf6t&#f0c6f8e&#f6d0f9r","&#f6d0f9C&#f6d0f9u&#f6d0f9t&#f6d0f9t&#f6d0f9i&#f6d0f9n&#f6d0f9g &#f6d0f9I&#f6d0f9V"),
            mutableListOf("§fBreaks glass instantly","","§fHitting opponents with this tool grants","§fa chance to deal extra damage"),
            Material.NETHERITE_AXE,
            mutableListOf("sapphireshard"),
            mutableMapOf(Enchantment.DIG_SPEED to 6, Enchantment.DURABILITY to 9, Enchantment.SILK_TOUCH to 1, Enchantment.DAMAGE_ALL to 7, Enchantment.MENDING to 1)
        )
        return Pair("sapphireshard", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        val interactEvent: PlayerInteractEvent? = event as? PlayerInteractEvent
        val entityDamageEvent: EntityDamageByEntityEvent? = event as? EntityDamageByEntityEvent

        when (type) {
            Ability.LEFT_CLICK -> {
                val block = interactEvent?.clickedBlock ?: return false
                glassCutter(block, player)
            }
            Ability.ENTITY_DAMAGE -> {
                entityDamageEvent!!.damage = slicing(entityDamageEvent.entity, entityDamageEvent.damage)
            }
            else -> return false
        }
        return true
    }


    private fun glassCutter(block: Block, p: Player) {
        if (!block.type.toString().contains("GLASS")) return
        block.world.spawnParticle(Particle.BLOCK_DUST, block.location.add(0.5, 0.5, 0.5), 30, 0.5, 0.5, 0.5, 0.1, block.blockData)
        block.world.playSound(block.location, block.blockData.soundGroup.breakSound, 0.5f, 0.8f)
        p.breakBlock(block)
    }

    private fun slicing(hitEntity: Entity, damageInitial: Double): Double {
        var damage = damageInitial
        val chance = Random().nextInt(100)
        if (chance <= 35) {
            damage *= 1.5
            hitEntity.world.spawnParticle(
                Particle.BLOCK_DUST, hitEntity.location
                    .add(0.0, 1.0, 0.0), 30, 0.5, 0.5, 0.5, 0.1, Material.AMETHYST_CLUSTER.createBlockData()
            )
            hitEntity.world.playSound(hitEntity.location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 0.5f, 0.8f)
        }
        return damage
    }
}