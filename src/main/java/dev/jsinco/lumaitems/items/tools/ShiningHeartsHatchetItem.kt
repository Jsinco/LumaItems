package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class ShiningHeartsHatchetItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#fbbdb7&lS&#fbb8b7&lh&#fbb4b7&li&#fcafb7&ln&#fcabb7&li&#fca6b6&ln&#fca2b6&lg &#fc9db6&lH&#fd99b6&le&#fd94b6&la&#fd90b1&lr&#fd8ba8&lt&#fd869f&ls &#fd8196&lH&#fd7d8d&la&#fd7884&lt&#fd737b&lc&#fd6e72&lh&#fd6a69&le&#fd6560&lt",
            mutableListOf("&#fd8ba8Lover's Present"),
            mutableListOf("Grants a chance to drop additional", "logs when cutting trees down", "", "When attacking, nearby enemies have", "a chance to be damaged as well"),
            Material.NETHERITE_AXE,
            mutableListOf("shiningheartshatchet"),
            mutableMapOf(Enchantment.MENDING to 1, Enchantment.DURABILITY to 10, Enchantment.DIG_SPEED to 8, Enchantment.LOOT_BONUS_BLOCKS to 5, Enchantment.SWEEPING_EDGE to 4, Enchantment.DAMAGE_ALL to 7)
        )
        item.tier = "&#fb5a5a&lV&#fb6069&la&#fc6677&ll&#fc6c86&le&#fc7294&ln&#fd78a3&lt&#fd7eb2&li&#fb83be&ln&#f788c9&le&#f38dd4&ls &#f092df&l2&#ec97e9&l0&#e89cf4&l2&#e4a1ff&l4"
        return Pair("shiningheartshatchet", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.BREAK_BLOCK -> {
                if (Random.nextInt(100) > 5) return false
                event as BlockBreakEvent

                val drops = event.block.drops
                for (drop in drops) {
                    if (drop.type.name.endsWith("_LOG")) {
                        event.block.world.dropItemNaturally(event.block.location, ItemStack(drop.type, 14))
                        break
                    }
                }
                player.world.spawnParticle(Particle.SPELL_WITCH, event.block.location, 10, 0.5, 0.5, 0.5, 0.0)
                player.world.playSound(event.block.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 0.7f, 1.0f)
            }

            Ability.ENTITY_DAMAGE -> {
                if (Random.nextInt(100) > 45) return false
                event as EntityDamageByEntityEvent

                val entities = event.entity.getNearbyEntities(6.0, 6.0, 6.0)
                entities.add(event.entity)


                event.entity.world.playSound(event.entity.location, Sound.ITEM_AXE_STRIP, 1f, 0.9f)
                event.entity.world.playSound(event.entity.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 0.9f)
                for (entity in entities.mapNotNull { it as? LivingEntity }) {
                    entity.world.spawnParticle(Particle.SWEEP_ATTACK, entity.location, 3, 0.5, 0.5, 0.5, 0.1)
                    entity.damage(6.0, player)
                }
            }

            else -> return false
        }
        return true
    }
}