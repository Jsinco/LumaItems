package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.events.BlockCacheManager
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Levelled
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class HellStridersItem : CustomItem {

    // Usage:
    // - Summer 2024
    // Inspiration:
    // - Frost Walker enchantment
    // - Lilac's implementation of this item
    // Idea:
    // Movement-based boots
    // - Ability to walk on lava exactly like frost walker but with obsidian instead of ice


    companion object {
        const val ID = "hellstriders"
        private val plugin: LumaItems = LumaItems.getPlugin()
    }


    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#C93907&lH&#D15112&le&#DA691D&ll&#E28128&ll&#E69635&l &#E9AA43&lS&#EDBF50&lt&#E9AA43&lr&#E69635&li&#E28128&ld&#DA691D&le&#D15112&lr&#C93907&ls",
            mutableListOf("&#E06A41Lava Walker I"),
            mutableListOf("Allows the wearer to", "walk on lava."),
            Material.NETHERITE_BOOTS,
            mutableListOf(ID),
            mutableMapOf(Enchantment.MENDING to 1, Enchantment.UNBREAKING to 10, Enchantment.PROTECTION to 4, Enchantment.FIRE_PROTECTION to 9, Enchantment.SOUL_SPEED to 3)
        )
        item.tier = "&#F34848&lS&#E36643&lo&#D3843E&ll&#C3A239&ls&#B3C034&lt&#A3DE2F&li&#93FC2A&lc&#7DE548&le&#66CD66&l &#50B684&l2&#399EA1&l0&#2387BF&l2&#0C6FDD&l4"
        return Pair(ID, item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {

            Action.CACHED_BLOCK_BREAK -> {
                event as BlockBreakEvent
                event.isCancelled = true
                event.block.type = Material.LAVA
                BlockCacheManager.unCacheBlock(player.uniqueId, event.block)
            }

            Action.ARMOR_CHANGE -> {
                if (!Util.isItemInSlot(ID, EquipmentSlot.FEET, player)) {
                    delete(player.uniqueId)
                }
            }

            Action.PLAYER_TELEPORT -> {
                delete(player.uniqueId)
            }

            Action.PLAYER_QUIT -> {
                delete(player.uniqueId)
            }

            Action.PLUGIN_DISABLE -> {
                delete(player.uniqueId)
            }

            Action.MOVE -> {
                event as PlayerMoveEvent

                if (!event.hasChangedBlock() || player.isFlying || !player.location.add(0.0, -1.0, 0.0).block.isSolid) {
                    return false
                }

                val locBelow = player.location.add(0.0, -1.0, 0.0)
                if (!checkForAdjacentBlockType(locBelow.block, Material.LAVA)) {
                    return false
                }



                val blockBelow = locBelow.block


                for (block in circle(blockBelow.location, 3, 13)) {
                    if ((block.type == Material.LAVA) && (block.blockData as Levelled).level == 0 && block.location.add(0.0, 1.0, 0.0).block.isEmpty) {
                        block.type = Material.OBSIDIAN
                        BlockCacheManager.cacheBlock(player.uniqueId, block, ID)
                    }
                }
            }

            Action.ASYNC_RUNNABLE -> { // Write this better
                val _B: MutableList<Block> = BlockCacheManager.getCachedBlocks(ID).ifEmpty { return false }.toMutableList()
                val aT: MutableSet<Location> = mutableSetOf()

                if (_B.size >= 40) {
                    for (index in 0 until _B.size / 4) {
                        Bukkit.getScheduler().runTask(plugin, Runnable {
                            val block = _B[index]
                            if (block.type == Material.OBSIDIAN) {
                                block.type = Material.CRYING_OBSIDIAN
                            } else if (block.type == Material.CRYING_OBSIDIAN) {
                                block.type = Material.LAVA
                                BlockCacheManager.unCacheBlock(player.uniqueId, block)
                            }
                        })
                    }
                }

                for (i in 0 until 3) {
                    Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                        for (e in 0 until 6) {
                            val block = _B.random()
                            if (block.location.distance(player.location) > 10) {
                                block.type = Material.LAVA
                                BlockCacheManager.unCacheBlock(player.uniqueId, block)
                            }

                            if (block.location in aT) continue
                            when (block.type) {
                                Material.OBSIDIAN -> {
                                    block.type = Material.CRYING_OBSIDIAN
                                    aT.add(block.location)
                                }

                                Material.CRYING_OBSIDIAN -> {
                                    block.type = Material.LAVA
                                    BlockCacheManager.unCacheBlock(player.uniqueId, block)
                                    aT.add(block.location)
                                }
                                else -> continue
                            }
                        }
                    }, Random.nextLong(1, 10))
                }
            }

            else -> return false
        }
        return true
    }


    fun circle(center: Location, size: Int, segment: Int): Set<Block> {
        val blockList: MutableSet<Block> = mutableSetOf()
        for (radius in 0 until size) {
            var i = 0
            while (i < 360) {
                val angle = i * Math.PI / 180
                val x = Math.round(radius * cos(angle)).toDouble()
                val z = Math.round(radius * sin(angle)).toDouble()
                val loc = center.clone().add(x, 0.0, z)
                blockList.add(loc.block)
                i += 360 / segment
            }
        }
        return blockList
    }

    private fun checkForAdjacentBlockType(center: Block, m: Material): Boolean {
        val loc = center.location
        return loc.clone().add(1.0, 0.0, 0.0).block.type == m || loc.clone().add(-1.0, 0.0, 0.0).block.type == m || loc.clone().add(0.0, 0.0, 1.0).block.type == m || loc.clone().add(0.0, 0.0, -1.0).block.type == m ||
                loc.clone().add(1.0, 0.0, 1.0).block.type == m || loc.clone().add(-1.0, 0.0, -1.0).block.type == m || loc.clone().add(-1.0, 0.0, 1.0).block.type == m || loc.clone().add(1.0, 0.0, -1.0).block.type == m
    }

    private fun delete(uuid: UUID) {
        val cachedBlocks: List<Block> = BlockCacheManager.getCachedBlocks(uuid).ifEmpty { return }
        BlockCacheManager.unCacheBlock(uuid, cachedBlocks)
        for (block in cachedBlocks) {
            block.type = Material.LAVA
        }
    }

}