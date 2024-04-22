package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.util.AbilityUtil
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import java.util.*

// TODO
class MagmaticSet : AstralSet {

    companion object {
        private val materials: List<Material> = listOf(
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_SWORD,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_SHOVEL
        )
        private val enchants: Map<Enchantment, Int> = mapOf(
            Enchantment.PROTECTION_ENVIRONMENTAL to 5,
            Enchantment.DAMAGE_ALL to 6,
            Enchantment.DURABILITY to 8,
            Enchantment.SWEEPING_EDGE to 4,
            Enchantment.FIRE_ASPECT to 4,
            Enchantment.DIG_SPEED to 6,
            Enchantment.LOOT_BONUS_BLOCKS to 4,
            Enchantment.LOOT_BONUS_MOBS to 4,
            Enchantment.PROTECTION_FALL to 5,
            Enchantment.THORNS to 3,
            Enchantment.MENDING to 1,
        )

        private var smeltOreTypes = listOf(
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.NETHER_GOLD_ORE,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.COPPER_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.ANCIENT_DEBRIS
        )

        private val lores: Map<Material, MutableList<String>> = mapOf(
            Material.DIAMOND_PICKAXE to mutableListOf("Breaking ores with this", "tool will automatically", "smelt them"),
            Material.DIAMOND_SHOVEL to mutableListOf("Breaking sand with this", "tool will automatically", "convert it to glass"),
            Material.DIAMOND_SWORD to mutableListOf("Right-click to send out flames", "and ignite entities", "", "&cCooldown: 10s")
        )
        private val cooldown: MutableList<UUID> = mutableListOf()
    }

    override fun setItems(): List<ItemStack> {
        val items: MutableList<ItemStack> = mutableListOf()
        for (material in materials) {
            val toolEnchants = mutableMapOf<Enchantment, Int>()
            for (enchant in enchants) {
                if (enchant.key.canEnchantItem(ItemStack(material))) {
                    toolEnchants[enchant.key] = enchant.value
                }
            }

            val item = ItemFactory(
                "&#E97979&lMagmatic &f${Util.getGearType(material)}",
                if (lores.keys.contains(material)) mutableListOf("&#E97979Volcanic") else mutableListOf(),
                lores[material] ?: mutableListOf(),
                material,
                mutableListOf("magmatic-set"),
                toolEnchants,
            )
            item.tier = "&#E97979&lAstral"
            item.stringPersistentDatas[NamespacedKey(LumaItems.getPlugin(), "relic-rarity")] = Rarity.ASTRAL.name
            items.add(item.createItem())
        }
        return items
    }

    override fun identifier(): String {
        return "magmatic-set"
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        val material = player.inventory.itemInMainHand.type
        when (type) {
            Ability.RIGHT_CLICK -> {
                if (material.name.contains("SWORD") && !cooldown.contains(player.uniqueId)) {
                    AbilityUtil.spawnSpell(player, Particle.FLAME, "magmatic-set", 120L)
                    cooldownPlayer(player.uniqueId)
                }
            }
            Ability.PROJECTILE_LAND -> {
                event as ProjectileHitEvent
                if (AbilityUtil.noDamagePermission(player, event.hitEntity ?: return false)) return false
                igniteEntity(event.hitEntity as LivingEntity)
            }
            Ability.BREAK_BLOCK -> {
                event as BlockBreakEvent
                if (material.name.contains("PICKAXE")) {
                    if (pickaxeSmelt(event.block, event.block.getDrops(player.inventory.itemInMainHand))) event.isDropItems = false
                } else if (material.name.contains("SHOVEL")) {
                    if (shovelSmelt(event.block, event.block.getDrops(player.inventory.itemInMainHand))) event.isDropItems = false
                }
            }

            else -> return false
        }
        return true
    }

    private fun igniteEntity(entity: LivingEntity) {
        for (i in 0..60) {
            entity.world.spawnParticle(Particle.FLAME, entity.location, 1, 0.3, 0.3, 0.3, 0.2)
        }
        entity.world.playSound(entity.location, Sound.ENTITY_BLAZE_SHOOT, 1f, 0.9f)
        entity.fireTicks = 100
    }

    private fun pickaxeSmelt(blockBroken: Block, drops: Collection<ItemStack>): Boolean {
        if (!smeltOreTypes.contains(blockBroken.type)) return false

        for (drop in drops) {
            when (drop.type) {
                Material.RAW_GOLD, Material.GOLD_NUGGET -> drop.setType(Material.GOLD_INGOT)
                Material.RAW_IRON -> drop.setType(Material.IRON_INGOT)
                Material.RAW_COPPER -> drop.setType(Material.COPPER_INGOT)
                Material.ANCIENT_DEBRIS -> drop.setType(Material.NETHERITE_SCRAP)
                else -> continue
            }

        }
        blockBroken.world.spawn(blockBroken.location, ExperienceOrb::class.java).experience = 1
        for (i in drops.indices) {
            blockBroken.world.dropItemNaturally(blockBroken.location, drops.iterator().next())
        }
        return true
    }

    private fun shovelSmelt(blockBroken: Block, drops: Collection<ItemStack>): Boolean {
        if (blockBroken.type != Material.SAND && blockBroken.type != Material.RED_SAND) return false
        for (drop in drops) {
            drop.setType(Material.GLASS)
        }
        for (i in drops.indices) {
            blockBroken.world.dropItemNaturally(blockBroken.location, drops.iterator().next())
        }
        return true
    }

    fun cooldownPlayer(uuid: UUID) {
        cooldown.add(uuid)
        Bukkit.getScheduler().scheduleSyncDelayedTask(LumaItems.getPlugin(), {
            cooldown.remove(uuid)
        },200L)
    }
}