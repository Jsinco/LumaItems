package dev.jsinco.lumaitems.items.astral.sets

import com.gamingmesh.jobs.Jobs
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.util.GenericMCToolType
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack


class FalterSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val astralSetFactory = AstralSetFactory("Falter", mutableListOf("&#fb4d4dFoster"))
        val commonLore = mutableListOf("Damage dealt to enemies scales", "with %s Job level")

        astralSetFactory.commonEnchants = mutableMapOf(
            Enchantment.DAMAGE_ALL to 7,
            Enchantment.DAMAGE_UNDEAD to 7,
            Enchantment.DURABILITY to 10,
            Enchantment.MENDING to 1
        )

        astralSetFactory.astralSetItem(
            Material.DIAMOND_PICKAXE,
            mutableMapOf(Enchantment.DIG_SPEED to 10, Enchantment.DURABILITY to 9, Enchantment.LOOT_BONUS_BLOCKS to 5),
            commonLore.map { it.format("Miner") }
        )
        astralSetFactory.astralSetItem(
            Material.DIAMOND_HOE,
            mutableMapOf(Enchantment.DIG_SPEED to 7,Enchantment.LOOT_BONUS_BLOCKS to 5),
            commonLore.map { it.format("Farmer") }
        )
        astralSetFactory.astralSetItem(
            Material.DIAMOND_AXE,
            mutableMapOf(Enchantment.DIG_SPEED to 9, Enchantment.DURABILITY to 10, Enchantment.LOOT_BONUS_BLOCKS to 4, Enchantment.LOOT_BONUS_MOBS to 6),
            commonLore.map { it.format("Lumberjack") }
        )
        astralSetFactory.astralSetItem(
            Material.FISHING_ROD,
            mutableMapOf(Enchantment.LURE to 5, Enchantment.LUCK to 6),
            commonLore.map { it.format("Fisherman") }
        )

        return astralSetFactory.createdAstralItems
    }

    override fun identifier(): String {
        return "falter-set"
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent
                event.damage += getJobLevel(player.inventory.itemInMainHand.type, player) / 10
            }
            else -> return false
        }
        return true
    }


    private fun getJobLevel(material: Material, player: Player): Int {
        val jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player)

        val genericMCToolType = GenericMCToolType.getToolType(material)
        val job = when (genericMCToolType) {
            GenericMCToolType.PICKAXE -> Jobs.getJob("Miner")
            GenericMCToolType.HOE -> Jobs.getJob("Farmer")
            GenericMCToolType.AXE -> Jobs.getJob("Lumberjack")
            GenericMCToolType.FISHING_ROD -> Jobs.getJob("Fisherman")
            else -> return 0
        }
        return if (jobsPlayer.isInJob(job)) jobsPlayer.getJobProgression(job).level else 0
    }

}