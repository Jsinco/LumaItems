package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.util.ToolType
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class VenomSet : CustomItem, AstralSet {

    companion object {
        private val materials: List<Material> = listOf(
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.NETHERITE_SWORD,
            Material.NETHERITE_AXE
        )
        private val enchants: Map<Enchantment, Int> = mapOf(
            Enchantment.PROTECTION_ENVIRONMENTAL to 6,
            Enchantment.DAMAGE_ALL to 7,
            Enchantment.DURABILITY to 5,
            Enchantment.SWEEPING_EDGE to 4,
            Enchantment.THORNS to 5,
            Enchantment.PROTECTION_FALL to 4,
            Enchantment.MENDING to 1,
        )

        private val lores: Map<ToolType, MutableList<String>> = mapOf(
            ToolType.WEAPON to mutableListOf("Upon striking an enemy,", "they will be poisoned", "for a short duration"),
            ToolType.ARMOR to mutableListOf("Upon being struck, your", "attacker will be poisoned", "for a short duration")
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
                "&#fb4d4d&lVenom &f${Util.getGearType(material)}",
                mutableListOf("&#fb4d4dViper"),
                lores[ToolType.getToolType(material)] ?: mutableListOf(),
                material,
                mutableListOf("venom-set"),
                enchants,
            )
            item.tier = "&#fb4d4d&lAstral"
            item.stringPersistentDatas[NamespacedKey(LumaItems.getPlugin(), "relic-rarity")] = Rarity.ASTRAL.name
            items.add(item.createItem())
        }
        return items
    }

    override fun createItem(): Pair<String, ItemStack> {
        return Pair("venom-set", ItemStack(Material.AIR))
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.PLAYER_DAMAGED_BY_ENTITY -> {
                event as EntityDamageByEntityEvent
                val entity = event.damager as? LivingEntity ?: return false
                entity.addPotionEffect(PotionEffect(PotionEffectType.POISON, 60, 1, false, false, false))
            }
            Ability.ENTITY_DAMAGE -> {
                event as EntityDamageByEntityEvent
                val entity = event.entity as LivingEntity
                entity.addPotionEffect(PotionEffect(PotionEffectType.POISON, 60, 1, false, false, false))
            }

            else -> return false
        }
        return true
    }
}