package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.util.GenericMCToolType
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BlitzSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val factory = AstralSetFactory("Blitz", mutableListOf("&#AC87FBSwift"))

        factory.commonEnchants = mutableMapOf(
            Enchantment.DURABILITY to 6
        )

        factory.astralSetItem(
            Material.DIAMOND_AXE,
            mutableMapOf(Enchantment.DIG_SPEED to 5, Enchantment.LOOT_BONUS_BLOCKS to 3),
            mutableListOf("Grants haste while", "being held")
        )

        factory.astralSetItem(
            Material.ELYTRA,
            mutableMapOf(Enchantment.PROTECTION_FALL to 4, Enchantment.PROTECTION_EXPLOSIONS to 3),
            mutableListOf("Grants extra speed", "while boosting midair"),
            includeCommonEnchants = true,
            customName = "&#AC87FB&lBlitz &fWings",
            attributeModifiers = null,
            customEnchants = null
        )

        return factory.createdAstralItems
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        val genericMCToolType = GenericMCToolType.getToolType(player.inventory.itemInMainHand)

        when (type) {
            Action.RUNNABLE -> {
                if (genericMCToolType == GenericMCToolType.AXE) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 220, 0, false, false, true))
                }
            }
            Action.ELYTRA_BOOST -> {
                if (Util.isItemInSlot("blitz-set", EquipmentSlot.CHEST, player)) {
                    player.velocity = player.location.getDirection().multiply(1.5)
                    player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)
                }

            }
            else -> return false
        }
        return true
    }

    override fun identifier(): String {
        return "blitz-set"
    }
}

/*factory.astralSetItem(
    Material.DIAMOND_SWORD,
    mutableMapOf(Enchantment.DAMAGE_ALL to 6, Enchantment.LOOT_BONUS_MOBS to 3, Enchantment.SILK_TOUCH to 1),
    mutableListOf("When killing mobs, dropped items", "will automatically be placed", "in your inventory")
)

factory.astralSetItem(
    Material.DIAMOND_AXE,
    mutableMapOf(Enchantment.DIG_SPEED to 7, Enchantment.LOOT_BONUS_BLOCKS to 5),
    mutableListOf("When breaking blocks, dropped items", "will automatically be placed", "in your inventory")
)*/