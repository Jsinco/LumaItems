package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.relics.RelicCrafting
import dev.jsinco.lumaitems.util.ToolType
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class MistralSet : AstralSet {

    companion object {
        private val materials: List<Material> = listOf(
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.NETHERITE_SWORD,
            Material.NETHERITE_PICKAXE,
            Material.FISHING_ROD
        )
        private val enchants: Map<Enchantment, Int> = mapOf(
            Enchantment.PROTECTION_ENVIRONMENTAL to 6,
            Enchantment.PROTECTION_PROJECTILE to 7,
            Enchantment.PROTECTION_FALL to 5,
            Enchantment.DAMAGE_ALL to 7,
            Enchantment.DURABILITY to 8,
            Enchantment.SWEEPING_EDGE to 4,
            Enchantment.DIG_SPEED to 8,
            Enchantment.SILK_TOUCH to 1,
            Enchantment.MENDING to 1,
            Enchantment.LURE to 4,
            Enchantment.LUCK to 5
        )

        private val lores: Map<ToolType, MutableList<String>> = mapOf(
            ToolType.ARMOR to mutableListOf("&6Set Bonus:&7 Speed I"),
            ToolType.WEAPON to mutableListOf("Grants extra speed", "while held"),
            ToolType.TOOL to mutableListOf("Grants extra speed", "while held")
        )
    }

    override fun setItems(): List<ItemStack> {
        val items: MutableList<ItemStack> = mutableListOf()

        for (material in materials) {
            val enchants = mutableMapOf<Enchantment, Int>()
            for (enchant in Companion.enchants) {
                if (enchant.key.canEnchantItem(ItemStack(material))) {
                    enchants[enchant.key] = enchant.value
                }
            }
            val item = ItemFactory(
                "&#fb4d4d&lMistral &f${Util.getGearType(material)}",
                mutableListOf("&#fb4d4dSwift"),
                lores[ToolType.getToolType(material)] ?: mutableListOf(),
                material,
                mutableListOf("mistral-set"),
                enchants,
            )

            when (material) {
                Material.NETHERITE_PICKAXE -> {
                    item.attributeModifiers[Attribute.GENERIC_ATTACK_DAMAGE] = ToolAttributes.PICKAXE.attackDamage
                    item.attributeModifiers[Attribute.GENERIC_ATTACK_SPEED] = ToolAttributes.PICKAXE.attackSpeed
                    item.attributeModifiers[Attribute.GENERIC_MOVEMENT_SPEED] = ToolAttributes.getSpeedAttribute()
                }

                Material.NETHERITE_SWORD -> {
                    item.attributeModifiers[Attribute.GENERIC_ATTACK_DAMAGE] = ToolAttributes.SWORD.attackDamage
                    item.attributeModifiers[Attribute.GENERIC_ATTACK_SPEED] = ToolAttributes.SWORD.attackSpeed
                    item.attributeModifiers[Attribute.GENERIC_MOVEMENT_SPEED] = ToolAttributes.getSpeedAttribute()
                }

                Material.FISHING_ROD -> {
                    item.attributeModifiers[Attribute.GENERIC_MOVEMENT_SPEED] = ToolAttributes.getSpeedAttribute()
                }

                else -> {}
            }
            item.tier = "&#fb4d4d&lAstral"
            item.stringPersistentDatas[NamespacedKey(LumaItems.getPlugin(), "relic-rarity")] = Rarity.ASTRAL.name
            items.add(item.createItem())
        }
        return items
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

    private enum class ToolAttributes(val attackDamage: AttributeModifier, val attackSpeed: AttributeModifier) {
        PICKAXE(
            AttributeModifier(UUID.randomUUID(), "attackDamage", 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND),
            AttributeModifier(UUID.randomUUID(), "attackSpeed", 1.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)),
        SWORD(
            AttributeModifier(UUID.randomUUID(), "attackDamage", 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND),
            AttributeModifier(UUID.randomUUID(), "attackSpeed", 1.6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        companion object {
            fun getSpeedAttribute(): AttributeModifier {
                return AttributeModifier(UUID.randomUUID(), "speed", 0.025, AttributeModifier.Operation.ADD_NUMBER);
            }
        }
    }
}