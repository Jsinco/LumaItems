package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.util.Cuboid
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.AbilityUtil
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class MistralMattockItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = CreateItem(
            "&#fff1be&lM&#fee7bc&li&#fedcb9&ls&#fdd2b7&lt&#fdc8b4&lr&#fcbdb2&la&#fbb3af&ll &#fbaeb4&lM&#fbadc0&la&#fbadcc&lt&#fbacd9&lt&#fbace5&lo&#fbabf1&lc&#fbabfd&lk",
            mutableListOf("&#fffba7S&#fef1ade&#fde7b3e&#fddcb9k&#fcd2bfe&#fbc8c5r"),
            mutableListOf("&#e790b5\"&#e593b2B&#e497afe &#e29aadg&#e09daau&#dfa0a7i&#dda4a4d&#dba7a1e&#daaa9fd &#d8ad9cb&#d6b199y &#d4b496t&#d3b794h&#d1ba91e &#cfbe8ew&#cec18bi&#ccc488n&#cac786d&#c9cb83.&#c7ce80\"","","§fSeeks out nearby ores and points the","§fwielder to them","","§fWhen breaking ores, Seeker will try to break", "§fneighboring ores as well"),
            Material.NETHERITE_PICKAXE,
            mutableListOf("mistralmattock"),
            mutableMapOf(Enchantment.DIG_SPEED to 8, Enchantment.DURABILITY to 10, Enchantment.LOOT_BONUS_BLOCKS to 6, Enchantment.MENDING to 1)
        )
        return Pair("mistralmattock", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        val blockBreakEvent: BlockBreakEvent? = event as? BlockBreakEvent

        when (type) {
            Ability.BREAK_BLOCK -> {
                seeker(blockBreakEvent!!.block, player)
            }
            else -> return false
        }
        return true
    }


    private fun seeker(block: Block, player: Player) {
        if (block.type.toString().lowercase().contains("ore")) {
            AbilityUtil.breakRelativeBlock(block, player, Particle.GLOW, "ore", 0)
        } else {
            val chance = Random().nextInt(100)
            if (chance <= 5) seekOres(block)
        }
    }


    private fun seekOres(blockBroken: Block) {
        val cuboid = Cuboid(
            blockBroken.location.add(7.0, 7.0, 7.0),
            blockBroken.location.add(-7.0, -7.0, -7.0)
        )
        val blocks: MutableList<Block> = ArrayList()
        for (i in 0 until cuboid.blockList().size) {
            val block: Block = cuboid.blockList()[i]
            if (block.type.toString().lowercase(Locale.getDefault()).contains("ore")) {
                blocks.add(block)
            }
        }
        if (blocks.isEmpty()) return
        val seekedBlock = blocks[Random().nextInt(blocks.size)]
        val Loc1 = blockBroken.location
        val Loc2 = seekedBlock.location
        val repeatTracker = intArrayOf(0)
        object : BukkitRunnable() {
            override fun run() {
                val vector: Vector = AbilityUtil.getDirectionBetweenLocations(Loc1, Loc2)
                var i = 1.0
                while (i <= blockBroken.location.distance(Loc2)) {
                    vector.multiply(i)
                    Loc1.add(vector)
                    Loc1.getWorld().spawnParticle(Particle.GLOW, Loc1, 1, 0.1, 0.1, 0.1, 0.1)
                    Loc1.subtract(vector)
                    vector.normalize()
                    i += 0.5
                }
                if (repeatTracker[0] == 5) {
                    cancel()
                } else repeatTracker[0]++
            }
        }.runTaskTimer(LumaItems.getPlugin(), 0L, 1L)
        seekedBlock.world.playSound(seekedBlock.location, Sound.ENTITY_ALLAY_ITEM_TAKEN, 1f, 1f)
    }
}