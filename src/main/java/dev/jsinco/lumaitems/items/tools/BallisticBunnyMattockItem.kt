package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.AbilityUtil
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Interaction
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.UUID
import kotlin.random.Random

class BallisticBunnyMattockItem : CustomItem {

    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
        private val stuckCarrots: LinkedHashMap<UUID, MutableList<CarrotDisplay>> = linkedMapOf()
        private const val EXPLOSION_POWER: Float = 2.3f

        // cant think rn
        private val coolingDownCarrots: MutableMap<UUID, Int> = mutableMapOf()
        private val activelyCoolingDown: MutableSet<UUID> = mutableSetOf()
    }
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#FC6D6D&lB&#FC7474&la&#FD7B7B&ll&#FD8181&ll&#FE8888&li&#FE8F8F&ls&#FF9696&lt&#FE9C9A&li&#FDA49B&lc &#FBAB9B&lB&#F9B29C&lu&#F7B99C&ln&#F5C09C&ln&#F4C89D&ly &#F3CFA0&lM&#F3D7A3&la&#F3DFA7&lt&#F2E7AB&lt&#F2EFAF&lo&#F2F7B2&lc&#F2FFB6&lk",
            mutableListOf("&#FC6D6DBombs Away!"),
            mutableListOf(
                "Right-click to stick up to 3 explosive",
                "carrots onto any block",
                "",
                "Before the timer runs out, crouch",
                "and right-click to detonate any",
                "placed carrots",
                "",
                "&cCooldown: 15s"
            ),
            Material.NETHERITE_PICKAXE,
            mutableListOf("ballisticbunnymattock"),
            mutableMapOf(Enchantment.DIG_SPEED to 9, Enchantment.DURABILITY to 7, Enchantment.LOOT_BONUS_BLOCKS to 5, Enchantment.MENDING to 1)
        )
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"
        return Pair("ballisticbunnymattock", item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.RIGHT_CLICK -> {
                event as PlayerInteractEvent
                if (player.isSneaking) {
                    detonateCarrots(player)
                    return true
                }
                stickCarrot(event.clickedBlock ?: return false, event.blockFace, player)
            }
            Action.ENTITY_DAMAGED_GENERIC -> {
                event as EntityDamageEvent
                detonateCarrot(event.entity as? Interaction ?: return false)
            }
            else -> return false
        }
        return true
    }

    private fun stickCarrot(block: Block, blockFace: BlockFace, player: Player) {
        if (!block.isSolid) return

        val currentCarrots = stuckCarrots[player.uniqueId] ?: mutableListOf()
        for (carrot in currentCarrots) {
            if (carrot.block == block) {
                return
            }
        }
        if (!cooldownPlayer(player.uniqueId, false)) {
            return
        }
        if (AbilityUtil.noBuildPermission(player, block)) {
            return
        }

        val double = Random.nextInt(15) == 5


        val loc = block.location.toCenterLocation().add(blockFace.modX.toDouble()/1.8, blockFace.modY.toDouble()/1.8, blockFace.modZ.toDouble()/1.8)
        loc.direction = blockFace.direction

        val itemDisplay = block.world.spawnEntity(loc, EntityType.ITEM_DISPLAY) as ItemDisplay
        itemDisplay.itemStack = Util.createBasicItem("", mutableListOf(), if (double) Material.GOLDEN_CARROT else Material.CARROT, mutableListOf(), true)
        itemDisplay.isPersistent = false

        val interaction = block.world.spawnEntity(loc, EntityType.INTERACTION) as Interaction
        interaction.isPersistent = false
        interaction.interactionHeight = 0.5f
        interaction.interactionWidth = 0.5f
        interaction.isResponsive = true
        interaction.persistentDataContainer.set(NamespacedKey(plugin, "ballisticbunnymattock"), PersistentDataType.SHORT, 1)

        stuckCarrots.getOrPut(player.uniqueId) { mutableListOf() }.add(CarrotDisplay(itemDisplay, interaction, block, if (double) 1.4f else 1.0f))

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
           if (!itemDisplay.isDead || !interaction.isDead) {
               detonateCarrots(player)
           }
        }, 300)
    }


    private fun detonateCarrots(player: Player) {
        for (carrot in stuckCarrots[player.uniqueId] ?: return) {
            val itemDisplay = carrot.itemDisplay
            itemDisplay.location.world.createExplosion(itemDisplay.location, EXPLOSION_POWER * carrot.multiplier, false, true, player)
            itemDisplay.remove()
            carrot.interaction.remove()
        }
        stuckCarrots.remove(player.uniqueId)
        cooldownPlayer(player.uniqueId, true)
    }

    private fun detonateCarrot(interaction: Interaction) {
        for (carrot in stuckCarrots) {
            for (carrotDisplay in carrot.value) {
                if (carrotDisplay.interaction == interaction) {
                    val itemDisplay = carrotDisplay.itemDisplay
                    itemDisplay.location.world.createExplosion(itemDisplay.location, EXPLOSION_POWER * carrotDisplay.multiplier, false, true)
                    itemDisplay.remove()
                    carrotDisplay.interaction.remove()
                    stuckCarrots[carrot.key]?.remove(carrotDisplay)
                    cooldownPlayer(carrot.key, true)
                    return
                }
            }
        }
    }

    private fun cooldownPlayer(uuid: UUID, postDetonation: Boolean): Boolean {
        var carrots = coolingDownCarrots[uuid] ?: 0

        if (carrots >= 3) {
            if (!activelyCoolingDown.contains(uuid)) {
                activelyCoolingDown.add(uuid)
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
                    activelyCoolingDown.remove(uuid)
                    coolingDownCarrots.remove(uuid)
                }, 300L)
            }
            return false
        }
        if (!postDetonation) carrots++
        coolingDownCarrots[uuid] = carrots
        return true
    }
}

private data class CarrotDisplay(val itemDisplay: ItemDisplay, val interaction: Interaction, val block: Block, val multiplier: Float)