package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.obj.Cuboid
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class DarkMoonMattockItem : CustomItem { // Todo: Rename

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#35306d&lD&#45417c&la&#56518b&lr&#66629a&lk&#7673a8&lm&#8684b7&lo&#9794c6&lo&#a7a5d5&ln &#a9a2d4&lM&#ac9fd2&la&#ae9cd1&lt&#b09acf&lt&#b297ce&lo&#b594cc&lc&#b791cb&lk",
            mutableListOf("&#5a6bc6D&#5867c1e&#5763bcs&#555fb7t&#535bb2r&#5257aeu&#5052a9c&#4e4ea4t&#4c4a9fi&#4b469av&#494295e"),
            mutableListOf("Breaks blocks in a 3x3 radius"),
            Material.NETHERITE_PICKAXE,
            mutableListOf("darkmoonmattock","cuboid"),
            mutableMapOf(Enchantment.DIG_SPEED to 8, Enchantment.DURABILITY to 10, Enchantment.SILK_TOUCH to 1, Enchantment.MENDING to 1)
        )
        item.tier = "&#E5EE4E&lS&#E7E154&lu&#EAD35A&lm&#ECC661&lm&#EEB967&le&#F1AB6D&lr &#F5917A&l2&#F88380&l0&#FA7686&l2&#FA7686&l4"
        return Pair("darkmoonmattock", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.BREAK_BLOCK -> {
                event as BlockBreakEvent
                val breakLoc = event.block.location
                val cuboid = Cuboid(breakLoc.add(-1.0, -1.0, -1.0), breakLoc.add(1.0, 1.0, 1.0))

                for (block in cuboid.blockList()) {
                    block.breakNaturally(player.inventory.itemInMainHand)
                    block.world.spawnParticle(Particle.BLOCK_CRACK, block.location.add(0.5, 0.5, 0.5), 10, 0.5, 0.5, 0.5, 0.1, block.blockData)
                }
            }
            else -> return false
        }
        return true
    }
}