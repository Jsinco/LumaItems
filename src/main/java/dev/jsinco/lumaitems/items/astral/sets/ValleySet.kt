package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.util.Util
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class ValleySet : CustomItem, AstralSet {

    companion object {
        private val materials: Map<Material, Boolean> = mapOf(
            Material.DIAMOND_SHOVEL to true,
            Material.DIAMOND_SWORD to true,
            Material.DIAMOND_AXE to false,
            Material.DIAMOND_HOE to true,
            Material.FISHING_ROD to false
        )

        private val enchants = mapOf(
            Enchantment.MENDING to 1,
            Enchantment.DURABILITY to 7,
            Enchantment.DAMAGE_ALL to 6,
            Enchantment.DIG_SPEED to 8,
            Enchantment.LOOT_BONUS_BLOCKS to 5,
            Enchantment.LOOT_BONUS_MOBS to 5,
            Enchantment.LUCK to 4,
            Enchantment.LURE to 5,
            Enchantment.SWEEPING_EDGE to 4,
            Enchantment.DAMAGE_UNDEAD to 6,
        )

        private val lores = mapOf(
            Material.DIAMOND_SHOVEL to mutableListOf("Has the ability to remove", "water from the direction the", "user is looking"),
            Material.DIAMOND_SWORD to mutableListOf("Grants the user potion buffs", "upon damaging an enemy"),
            Material.DIAMOND_HOE to mutableListOf("Has a chance to drop rare", "crops when breaking blocks")
        )
    }


    override fun setItems(): List<ItemStack> {
        val setItems: MutableList<ItemStack> = mutableListOf()
        for (material in materials) {
            val toolEnchants = mutableMapOf<Enchantment, Int>()
            val itemMaterial = ItemStack(material.key)
            for (enchant in enchants) {
                if (enchant.key.canEnchantItem(itemMaterial)) {
                    toolEnchants[enchant.key] = enchant.value
                }
            }

            if (Material.DIAMOND_AXE == material.key) { // Axe isn't a weapon and im lazy
                toolEnchants.remove(Enchantment.DAMAGE_ALL)
                toolEnchants.remove(Enchantment.DAMAGE_UNDEAD)
            }

            val item = ItemFactory(
                "&#fb4d4d&lValley &f${Util.getGearType(material.key)}",
                if (material.value) mutableListOf("&#fb4d4dIlk") else mutableListOf(),
                lores[material.key] ?: mutableListOf(),
                material.key,
                mutableListOf("valley-set"),
                toolEnchants
            )
            item.tier = "&#fb4d4d&lAstral"
            item.stringPersistentDatas[NamespacedKey(LumaItems.getPlugin(), "relic-rarity")] = Rarity.ASTRAL.name
            setItems.add(item.createItem())
        }
        return setItems
    }

    override fun createItem(): Pair<String, ItemStack> {
        return Pair("valley-set", ItemStack(Material.AIR))
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.LEFT_CLICK, Ability.RIGHT_CLICK -> {
                if (!player.inventory.itemInMainHand.type.name.contains("SHOVEL")) return false
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