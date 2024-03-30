package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random


class DarkRabbitHatchetItem : CustomItem {

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#1B1024&lD&#33243F&la&#4B375A&lr&#634B75&lk &#7B5F91&lR&#9372AC&la&#AB86C7&lb&#C399E2&lb&#DBADFD&li&#DAA7FA&lt &#D9A2F7&lH&#D89CF3&la&#D796F0&lt&#D590ED&lc&#D48BEA&lh&#D385E6&le&#D27FE3&lt",
            mutableListOf("&#7B5F91Chancity"),
            mutableListOf("Upon breaking blocks, drops will", "randomly be converted to charcoal", "or they will be multiplied."),
            Material.NETHERITE_AXE,
            mutableListOf("darkrabbithatchet"),
            mutableMapOf(Enchantment.DIG_SPEED to 8, Enchantment.DURABILITY to 9, Enchantment.SILK_TOUCH to 1, Enchantment.MENDING to 1, Enchantment.DAMAGE_UNDEAD to 5)
        )
        item.addQuote("&#7B5F91\"Eugh, it's got charcoal all over it!\"")
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"
        return Pair("darkrabbithatchet", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.BREAK_BLOCK -> {
                if (Random.nextInt(245) > 3) {
                    return false
                }
                event as BlockBreakEvent
                event.isDropItems = false
                chancityAbility(event.block.getDrops(player.inventory.itemInMainHand), event.block)
            }

            else -> return false
        }
        return true
    }

    private fun chancityAbility(drops: Collection<ItemStack>, block: Block) {
        var doDropCharCoal = Random.nextBoolean()
        val doMultiplyDrops = Random.nextBoolean()

        if (!doMultiplyDrops && !doDropCharCoal) {
            doDropCharCoal = true
        }

        if (doMultiplyDrops) {
            val itemStack = drops.iterator().next()
            itemStack.amount *= Random.nextInt(2, 6)
        }

        if (doDropCharCoal) {
            for (drop in drops) {
                drop.setType(Material.CHARCOAL)
            }
        }

        for (drop in drops) {
            block.world.dropItemNaturally(block.location, drop)
        }


        val color = if (doDropCharCoal) Color.BLACK else Util.javaAwtColorToBukkitColor(Util.getColor(block))
        block.world.spawnParticle(Particle.REDSTONE, block.location.add(0.5, 0.5, 0.5), 20, 0.5, 0.5, 0.5, 0.1, Particle.DustOptions(color, 1f))
        block.world.playSound(block.location, Sound.ENTITY_GLOW_SQUID_AMBIENT,0.6f, 1f)
    }


}