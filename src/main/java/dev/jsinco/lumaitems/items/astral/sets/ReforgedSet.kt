package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.util.DefaultAttributes
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ReforgedSet : AstralSet {
    override fun setItems(): List<ItemStack> {
        val astralSetFactory = AstralSetFactory("Reforged", mutableListOf("&#E97979Unwavering"))
        astralSetFactory.commonEnchants = mutableMapOf(
            Enchantment.MENDING to 1,
            Enchantment.DURABILITY to 6,
            Enchantment.PROTECTION_ENVIRONMENTAL to 7
        )

        val attribute: Pair<Attribute, AttributeModifier> = Pair(
            Attribute.GENERIC_MAX_HEALTH,
            AttributeModifier("genericMaxHealth", 2.0, AttributeModifier.Operation.ADD_NUMBER)
        )

        astralSetFactory.astralSetItem(
            Material.NETHERITE_HELMET,
            mutableMapOf(Enchantment.OXYGEN to 3),
            mutableListOf("Increases max amount", "of health"),
            true,
            DefaultAttributes.NETHERITE_HELMET.appendThenGetAttributes(attribute.first, attribute.second)
        )

        astralSetFactory.astralSetItem(
            Material.NETHERITE_CHESTPLATE,
            mutableMapOf(),
            mutableListOf("Increases max amount", "of health"),
            true,
            DefaultAttributes.NETHERITE_CHESTPLATE.appendThenGetAttributes(attribute.first, attribute.second)
        )

        astralSetFactory.astralSetItem(
            Material.NETHERITE_LEGGINGS,
            mutableMapOf(),
            mutableListOf("Increases max amount", "of health"),
            true,
            DefaultAttributes.NETHERITE_LEGGINGS.appendThenGetAttributes(attribute.first, attribute.second)
        )

        astralSetFactory.astralSetItem(
            Material.NETHERITE_BOOTS,
            mutableMapOf(Enchantment.PROTECTION_FALL to 4, Enchantment.DEPTH_STRIDER to 3),
            mutableListOf("Increases max amount", "of health"),
            true,
            DefaultAttributes.NETHERITE_BOOTS.appendThenGetAttributes(attribute.first, attribute.second)
        )

        return astralSetFactory.createdAstralItems
    }

    override fun identifier(): String {
        return "reforged-set"
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        return false
    }
}