package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.util.GenericMCToolType
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class ValleySet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val astralSetFactory = AstralSetFactory("Valley", mutableListOf("&#fb4d4dIlk"))
        astralSetFactory.commonEnchants = mutableMapOf(
            Enchantment.MENDING to 1,
            Enchantment.DURABILITY to 7,
        )

        astralSetFactory.astralSetItem(
            Material.DIAMOND_SHOVEL,
            mutableMapOf(Enchantment.DIG_SPEED to 8, Enchantment.LOOT_BONUS_BLOCKS to 5),
            mutableListOf("Has the ability to remove", "water from the direction the", "user is looking")
        )

        astralSetFactory.astralSetItem(
            Material.DIAMOND_SWORD,
            mutableMapOf(Enchantment.DAMAGE_ALL to 6, Enchantment.DAMAGE_UNDEAD to 6, Enchantment.SWEEPING_EDGE to 4),
            mutableListOf("Grants the user potion buffs", "upon damaging an enemy")
        )

        astralSetFactory.astralSetItem(
            Material.DIAMOND_AXE,
            mutableMapOf(Enchantment.DIG_SPEED to 8, Enchantment.LOOT_BONUS_BLOCKS to 5),
            mutableListOf()
        )

        astralSetFactory.astralSetItem(
            Material.DIAMOND_HOE,
            mutableMapOf(Enchantment.DIG_SPEED to 8, Enchantment.LOOT_BONUS_BLOCKS to 5),
            mutableListOf("Has a chance to drop rare", "crops when breaking blocks")
        )

        astralSetFactory.astralSetItem(
            Material.FISHING_ROD,
            mutableMapOf(Enchantment.LURE to 5, Enchantment.LUCK to 4),
            mutableListOf()
        )

        return astralSetFactory.createdAstralItems
    }

    override fun identifier(): String {
        return "valley-set"
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.LEFT_CLICK, Ability.RIGHT_CLICK -> {
                if (GenericMCToolType.getToolType(player.inventory.itemInMainHand) != GenericMCToolType.SHOVEL) return false
                val targetBlock = player.getTargetBlockExact(45, FluidCollisionMode.ALWAYS) ?: return false

                if (targetBlock.type.name.contains("WATER")) {
                    targetBlock.type = Material.AIR
                }
            }

            Ability.ENTITY_DAMAGE -> {
                if (!player.inventory.itemInMainHand.type.name.contains("SWORD")) return false
                event as EntityDamageByEntityEvent
                if (event.entity !is Monster) return false
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 100, 0, false, false, false))
                player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, false, false, false))
            }

            Ability.BREAK_BLOCK -> {
                if (player.inventory.itemInMainHand.type.name.contains("HOE") && Random.nextInt(100) < 3) {
                    event as BlockBreakEvent
                    event.block.world.dropItem(event.block.location, ItemStack(if (Random.nextBoolean()) Material.WHEAT else Material.GOLDEN_CARROT))
                }
            }

            else -> {
                return false
            }
        }
        return true
    }
}