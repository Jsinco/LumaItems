package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.util.ToolType
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class VenomSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val astralSetFactory = AstralSetFactory("Viper", mutableListOf("&#AC87FBVenom"))
        astralSetFactory.identifier = "venom-set"

        astralSetFactory.commonEnchants = mutableMapOf(
            Enchantment.PROTECTION to 5, Enchantment.SHARPNESS to 6, Enchantment.UNBREAKING to 5,
            Enchantment.SWEEPING_EDGE to 4, Enchantment.THORNS to 4, Enchantment.FEATHER_FALLING to 4,
        )

        val materials: List<Material> = listOf(
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD, Material.DIAMOND_AXE
        )

        for (material in materials) {
            astralSetFactory.astralSetItemGenericEnchantOnly(
                material,
                if (ToolType.getToolType(material) == ToolType.WEAPON) {
                    mutableListOf("Upon striking an enemy,", "they will be poisoned", "for a short duration.")
                } else {
                    mutableListOf("Upon being struck, your", "attacker will be poisoned", "for a short duration.")
                }
            )
        }

        return astralSetFactory.createdAstralItems
    }

    override fun identifier(): String {
        return "venom-set"
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.PLAYER_DAMAGED_BY_ENTITY -> {
                event as EntityDamageByEntityEvent
                val entity = event.damager as? LivingEntity ?: return false
                entity.addPotionEffect(PotionEffect(PotionEffectType.POISON, 60, 1, false, false, false))
            }
            Action.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent
                val entity = event.entity as LivingEntity
                entity.addPotionEffect(PotionEffect(PotionEffectType.POISON, 60, 1, false, false, false))
            }

            else -> return false
        }
        return true
    }
}