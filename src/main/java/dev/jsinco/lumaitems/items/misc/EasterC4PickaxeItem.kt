package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.EntityType
import org.bukkit.entity.Interaction
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class EasterC4PickaxeItem : CustomItem {

    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
        private val stuckCarrots: LinkedHashMap<UUID, MutableList<CarrotDisplay>> = linkedMapOf()
        private val lastCarrotPlacement: LinkedHashMap<UUID, Long> = linkedMapOf()
        private const val EXPLOSION_POWER: Float = 2.3f
        private const val CARROT_TIMEOUT: Long = 30000L
    }
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "easterc4pickaxe",
            mutableListOf(),
            mutableListOf("Stick carrots to any block!", "Right-click to stick a carrot", "to the block you're looking at."),
            Material.NETHERITE_PICKAXE,
            mutableListOf("easterc4pickaxe"),
            mutableMapOf()
        )
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"
        return Pair("easterc4pickaxe", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RIGHT_CLICK -> {
                event as PlayerInteractEvent
                if (player.isSneaking) {
                    detonateCarrots(player)
                    return true
                }
                stickCarrot(event.clickedBlock ?: return false, event.blockFace, player)
            }
            Ability.ENTITY_DAMAGED_GENERIC -> {
                event as EntityDamageEvent
                detonateCarrot(event.entity as? Interaction ?: return false)
            }

            Ability.RUNNABLE -> {
                val currentTime = System.currentTimeMillis()
                for (carrotPlacement in lastCarrotPlacement) {
                    if (carrotPlacement.value + CARROT_TIMEOUT > currentTime) {
                        stuckCarrots[carrotPlacement.key]?.forEach {
                            it.itemDisplay.remove()
                            it.interaction.remove()
                            stuckCarrots.remove(carrotPlacement.key)
                        } ?: continue
                    }
                }
            }
            else -> return false
        }
        return true
    }

    fun stickCarrot(block: Block, blockFace: BlockFace, player: Player) {
        val currentCarrots = stuckCarrots[player.uniqueId] ?: mutableListOf()
        if (currentCarrots.size >= 3) {
            return
        }
        for (carrot in currentCarrots) {
            if (carrot.block == block) {
                return
            }
        }

        val loc = block.location.toCenterLocation().add(blockFace.modX.toDouble()/1.8, blockFace.modY.toDouble()/1.8, blockFace.modZ.toDouble()/1.8)
        loc.direction = blockFace.direction

        val itemDisplay = block.world.spawnEntity(loc, EntityType.ITEM_DISPLAY) as ItemDisplay
        itemDisplay.itemStack = Util.createBasicItem("", mutableListOf(), Material.CARROT, mutableListOf(), true)
        itemDisplay.isPersistent = false

        val interaction = block.world.spawnEntity(loc, EntityType.INTERACTION) as Interaction
        interaction.isPersistent = false
        interaction.interactionHeight = 0.5f
        interaction.interactionWidth = 0.5f
        interaction.isResponsive = true
        interaction.persistentDataContainer.set(NamespacedKey(plugin, "easterc4pickaxe"), PersistentDataType.SHORT, 1)

        stuckCarrots.getOrPut(player.uniqueId) { mutableListOf() }.add(CarrotDisplay(itemDisplay, interaction, block))
    }


    fun detonateCarrots(player: Player) {
        for (carrot in stuckCarrots[player.uniqueId] ?: return) {
            val itemDisplay = carrot.itemDisplay
            itemDisplay.location.world.createExplosion(itemDisplay.location, EXPLOSION_POWER, false, true, player)
            itemDisplay.remove()
            carrot.interaction.remove()
        }
        stuckCarrots.remove(player.uniqueId)
    }

    fun detonateCarrot(interaction: Interaction) {
        for (carrot in stuckCarrots) {
            for (carrotDisplay in carrot.value) {
                if (carrotDisplay.interaction == interaction) {
                    val itemDisplay = carrotDisplay.itemDisplay
                    itemDisplay.location.world.createExplosion(itemDisplay.location, EXPLOSION_POWER, false, true)
                    itemDisplay.remove()
                    carrotDisplay.interaction.remove()
                    stuckCarrots[carrot.key]?.remove(carrotDisplay)
                    return
                }
            }
        }
    }
}
private data class CarrotDisplay(val itemDisplay: ItemDisplay, val interaction: Interaction, val block: Block)