package dev.jsinco.lumaitems.items.astral.sets

import com.gamingmesh.jobs.Jobs
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack


class FalterSet : CustomItem, AstralSet {

    companion object {
        private val materials = listOf(
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_HOE,
            Material.DIAMOND_AXE,
            Material.FISHING_ROD
        )

        private val enchantMap: Map<Material, MutableMap<Enchantment, Int>> = mapOf(
            Material.DIAMOND_PICKAXE to mutableMapOf(
                Enchantment.DAMAGE_ALL to 7,
                Enchantment.DAMAGE_UNDEAD to 7,
                Enchantment.DIG_SPEED to 10,
                Enchantment.DURABILITY to 9,
                Enchantment.MENDING to 1,
                Enchantment.LOOT_BONUS_BLOCKS to 5),
            Material.DIAMOND_HOE to mutableMapOf(
                Enchantment.DAMAGE_ALL to 7,
                Enchantment.DAMAGE_UNDEAD to 7,
                Enchantment.DIG_SPEED to 7,
                Enchantment.DURABILITY to 10,
                Enchantment.MENDING to 1,
                Enchantment.LOOT_BONUS_BLOCKS to 5),
            Material.DIAMOND_AXE to mutableMapOf(
                Enchantment.DAMAGE_ALL to 7,
                Enchantment.DAMAGE_UNDEAD to 7,
                Enchantment.DIG_SPEED to 9,
                Enchantment.DURABILITY to 10,
                Enchantment.MENDING to 1,
                Enchantment.LOOT_BONUS_BLOCKS to 4),
            Material.FISHING_ROD to mutableMapOf(
                Enchantment.DAMAGE_ALL to 7,
                Enchantment.DAMAGE_UNDEAD to 7,
                Enchantment.LURE to 5,
                Enchantment.LUCK to 6,
                Enchantment.MENDING to 1)
        )
        private val lores = mapOf(
            Material.DIAMOND_PICKAXE to "Miner",
            Material.DIAMOND_HOE to "Farmer",
            Material.DIAMOND_AXE to "Lumberjack",
            Material.FISHING_ROD to "Fisherman"
        )
    }

    override fun setItems(): List<ItemStack> {
        val items: MutableList<ItemStack> = mutableListOf()
        for (material in materials) {
            val item = CreateItem(
                "&#fb4d4d&lFalter &f${Util.getGearType(material)}",
                mutableListOf("&#fb4d4dFoster"),
                mutableListOf("Damage dealt to enemies scales", "with ${lores[material]} Job level"),
                material,
                mutableListOf("falter-set"),
                enchantMap[material] ?: mutableMapOf()
            )
            item.tier = "&#fb4d4d&lAstral"
            item.stringPersistentDatas[NamespacedKey(LumaItems.getPlugin(), "relic-rarity")] = Rarity.ASTRAL.name
            items.add(item.createItem())
        }
        return items
    }

    override fun createItem(): Pair<String, ItemStack> {
        return Pair("falter-set", ItemStack(Material.AIR))
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
        val job = when (material) {
            Material.DIAMOND_PICKAXE -> Jobs.getJob("Miner")
            Material.DIAMOND_HOE -> Jobs.getJob("Farmer")
            Material.DIAMOND_AXE -> Jobs.getJob("Lumberjack")
            else -> return 0
        }
        return if (jobsPlayer.isInJob(job)) jobsPlayer.getJobProgression(job).level else 0
    }

}