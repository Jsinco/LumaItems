package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.UUID

class SeagullFeatherItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&b&lSeagull Feather",
            mutableListOf(),
            mutableListOf("This beautiful seagull feather","shines in the light!", "", "Holding this feather in your", "offhand will give a speed boost."),
            Material.FEATHER,
            mutableListOf("seagullfeather"),
            mutableMapOf(Enchantment.DURABILITY to 10)
        )
        item.tier = "&#F34848&lS&#E06C42&lu&#CD903C&lm&#B9B436&lm&#A6D830&le&#93FC2A&lr &#5DC472&l2&#42A795&l0&#278BB9&l2&#0C6FDD&l4"
        item.attributeModifiers[Attribute.GENERIC_MOVEMENT_SPEED] = AttributeModifier(UUID.randomUUID(), "movementSpeed", 0.025, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND)
        return Pair("seagullfeather", item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        return false
    }
}
