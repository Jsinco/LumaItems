package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.util.ToolType
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MelukaSet : CustomItem, AstralSet {

    companion object {
        private val materials = listOf(
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.NETHERITE_PICKAXE,
            Material.NETHERITE_SHOVEL
        )
        private val enchants = mapOf(
            Enchantment.PROTECTION_ENVIRONMENTAL to 6,
            Enchantment.DAMAGE_ALL to 5,
            Enchantment.DURABILITY to 7,
            Enchantment.SWEEPING_EDGE to 3,
            Enchantment.DIG_SPEED to 7,
            Enchantment.SILK_TOUCH to 1,
            Enchantment.MENDING to 1,
            Enchantment.DEPTH_STRIDER to 4,
            Enchantment.WATER_WORKER to 2,
            Enchantment.OXYGEN to 5
        )
        private val lores: Map<ToolType, MutableList<String>> = mapOf(
            ToolType.ARMOR to mutableListOf("Grants the wearer dolphin's", "grace while in water"),
            ToolType.TOOL to mutableListOf("Breaking blocks underwater will", "automatically teleport them", "to the user's inventory"),
        )

    }


    override fun setItems(): List<ItemStack> {
        val items: MutableList<ItemStack> = mutableListOf()
        for (material in materials) {
            val toolEnchants = mutableMapOf<Enchantment, Int>()
            for (enchant in enchants) {
                if (enchant.key.canEnchantItem(ItemStack(material))) {
                    toolEnchants[enchant.key] = enchant.value
                }
            }

            val item = CreateItem(
                "&#fb4d4d&lMeluka &f${Util.getGearType(material)}",
                mutableListOf("&#fb4d4dMarine"),
                lores[ToolType.getToolType(material)] ?: mutableListOf(),
                material,
                mutableListOf("meluka-set"),
                toolEnchants
            )
            item.tier = "&#fb4d4d&lAstral"
            item.stringPersistentDatas[NamespacedKey(LumaItems.getPlugin(), "relic-rarity")] = Rarity.ASTRAL.name
            items.add(item.createItem())
        }
        return items
    }

    override fun createItem(): Pair<String, ItemStack> {
        return Pair("melukaset", ItemStack(Material.AIR))
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        if (!player.isInWater) return false
        when (type) {
            Ability.RUNNABLE -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.DOLPHINS_GRACE, 240, 0, false, false, false))
            }
            Ability.BREAK_BLOCK -> {
                event as BlockBreakEvent
                event.isDropItems = false

                val drops = event.block.getDrops(player.inventory.itemInMainHand)
                for (drop in drops) {
                    Util.giveItem(player, drop)
                }
            }
            else -> return false
        }
        return true
    }
}