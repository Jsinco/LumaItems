package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.util.ToolType
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MelukaSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val astralSetFactory = AstralSetFactory("Meluka", mutableListOf("&#AC87FBMarine"))

        val materials = listOf(
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL
        )

        astralSetFactory.commonEnchants = mutableMapOf(
            Enchantment.PROTECTION_ENVIRONMENTAL to 6, Enchantment.DAMAGE_ALL to 5, Enchantment.DURABILITY to 7,
            Enchantment.SWEEPING_EDGE to 3, Enchantment.DIG_SPEED to 7, Enchantment.SILK_TOUCH to 1, Enchantment.MENDING to 1,
            Enchantment.DEPTH_STRIDER to 4, Enchantment.WATER_WORKER to 2, Enchantment.OXYGEN to 5
        )

        for (material in materials) {
            val lore = if (ToolType.getToolType(material) == ToolType.TOOL) {
                mutableListOf("Breaking blocks underwater will", "automatically teleport them", "to the user's inventory")
            } else {
                mutableListOf("Grants the wearer dolphin's", "grace while in water")
            }

            astralSetFactory.astralSetItemGenericEnchantOnly(
                material,
                lore
            )
        }

        return astralSetFactory.createdAstralItems
    }

    override fun identifier(): String {
        return "meluka-set"
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        if (!player.isInWater) return false
        when (type) {
            Ability.RUNNABLE -> {
                if (Util.isWearingWithNBT(player, "meluka-set")) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.DOLPHINS_GRACE, 240, 0, false, false, false))
                }
            }
            Ability.BREAK_BLOCK -> {
                event as BlockBreakEvent
                val item = player.inventory.itemInMainHand
                if (ToolType.getToolType(item.type) != ToolType.TOOL) return false

                event.isDropItems = false

                val drops = event.block.getDrops(item)
                for (drop in drops) {
                    Util.giveItem(player, drop)
                }
            }
            else -> return false
        }
        return true
    }
}