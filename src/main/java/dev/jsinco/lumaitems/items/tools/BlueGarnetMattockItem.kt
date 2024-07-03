package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.obj.Cuboid
import dev.jsinco.lumaitems.util.AbilityUtil
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class BlueGarnetMattockItem : CustomItem { // Todo: Rename

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#3A7FEA&lB&#3790E4&ll&#34A0DD&lu&#31B1D7&le &#2BD2CA&lG&#28E2C4&la&#49CABA&lr&#6AB1B0&ln&#8B99A7&le&#AB819D&lt &#ED5089&lM&#EC4C7F&la&#EB4875&lt&#EA456C&lt&#E94162&lo&#E83D58&lc&#E7394E&lk",
            mutableListOf("&#6AB1B0Destructive"),
            mutableListOf("Breaks blocks in a 3x3 radius."),
            Material.NETHERITE_PICKAXE,
            mutableListOf("bluegarnetmattock"),
            mutableMapOf(Enchantment.DIG_SPEED to 8, Enchantment.DURABILITY to 10, Enchantment.SILK_TOUCH to 1, Enchantment.MENDING to 1)
        )
        item.tier = "&#F34848&lS&#E06C42&lu&#CD903C&lm&#B9B436&lm&#A6D830&le&#93FC2A&lr &#5DC472&l2&#42A795&l0&#278BB9&l2&#0C6FDD&l4"
        return Pair("bluegarnetmattock", item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.BREAK_BLOCK -> {
                event as BlockBreakEvent
                val b = event.block

                val blocklist = Cuboid.getCuboidBlocks(b.location.add(1.0, 1.0, 1.0), b.location.add(-1.0, -1.0, -1.0)).filter {
                    !AbilityUtil.blockTypeBlacklist.contains(it.type) && it.isSolid
                }

                for (block in blocklist) {
                    block.breakNaturally(player.inventory.itemInMainHand)
                    block.world.spawnParticle(Particle.BLOCK_CRACK, block.location.add(0.5, 0.5, 0.5), 10, 0.5, 0.5, 0.5, 0.1, block.blockData)
                }
            }
            else -> return false
        }
        return true
    }

}