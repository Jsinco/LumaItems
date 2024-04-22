package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Ability
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
import java.util.*

class MistralSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val astralSetFactory = AstralSetFactory("Mistral", mutableListOf("&#E97979Swift"))

        astralSetFactory.commonEnchants = mutableMapOf(
            Enchantment.PROTECTION_ENVIRONMENTAL to 4, Enchantment.PROTECTION_PROJECTILE to 5, Enchantment.PROTECTION_FALL to 5,
            Enchantment.DAMAGE_ALL to 6, Enchantment.DURABILITY to 7, Enchantment.SWEEPING_EDGE to 4,
            Enchantment.DIG_SPEED to 6, Enchantment.SILK_TOUCH to 1, //Enchantment.MENDING to 1,
            Enchantment.LURE to 4, Enchantment.LUCK to 4
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
    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
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