package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.relics.RelicCrafting
import dev.jsinco.lumaitems.util.DefaultAttributes
import dev.jsinco.lumaitems.util.GenericMCToolType
import dev.jsinco.lumaitems.util.ToolType
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID

class MistralSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val astralSetFactory = AstralSetFactory("Mistral", mutableListOf("&#AC87FBSwift"))

        astralSetFactory.commonEnchants = mutableMapOf(
            Enchantment.PROTECTION to 4, Enchantment.PROJECTILE_PROTECTION to 5, Enchantment.FEATHER_FALLING to 5,
            Enchantment.SHARPNESS to 6, Enchantment.UNBREAKING to 7, Enchantment.SWEEPING_EDGE to 4,
            Enchantment.EFFICIENCY to 6, Enchantment.SILK_TOUCH to 1, //Enchantment.MENDING to 1,
            Enchantment.LURE to 4, Enchantment.LUCK_OF_THE_SEA to 4
        )

        val materials: List<Material> = listOf(
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS,
            Material.IRON_BOOTS, Material.IRON_SWORD, Material.IRON_PICKAXE,
            Material.FISHING_ROD
        )

        for (material in materials) {
            val toolType = ToolType.getToolType(material)
            val genericMCToolType = GenericMCToolType.getToolType(material)

            astralSetFactory.astralSetItemGenericEnchantOnly(
                material,

                if (toolType == ToolType.ARMOR) {
                    mutableListOf("&6Set Bonus:&7 Speed I")
                } else {
                    mutableListOf("Grants extra speed", "while being held")
                },

                when (genericMCToolType) {
                    GenericMCToolType.SWORD -> {
                        DefaultAttributes.NETHERITE_SWORD.appendThenGetAttributes(
                            Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier(UUID.randomUUID(), "movementSpeed", 0.025, AttributeModifier.Operation.ADD_NUMBER)
                        )
                    }
                    GenericMCToolType.PICKAXE -> {
                        DefaultAttributes.NETHERITE_PICKAXE.appendThenGetAttributes(
                            Attribute.GENERIC_MOVEMENT_SPEED, AttributeModifier(UUID.randomUUID(), "movementSpeed", 0.025, AttributeModifier.Operation.ADD_NUMBER)
                        )
                    }
                    GenericMCToolType.FISHING_ROD -> {
                        mutableMapOf(Attribute.GENERIC_MOVEMENT_SPEED to AttributeModifier(UUID.randomUUID(), "movementSpeed", 0.025, AttributeModifier.Operation.ADD_NUMBER))
                    }
                    else -> null
                }
            )
        }

        return astralSetFactory.createdAstralItems
    }

    override fun identifier(): String {
        return "swift-set"
    }
    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.RUNNABLE -> {
                if (RelicCrafting.hasFullSet("mistral-set", player)) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 240, 0, false, false, false))
                }
            }

            else -> {
                return false
            }
        }
        return true
    }
}