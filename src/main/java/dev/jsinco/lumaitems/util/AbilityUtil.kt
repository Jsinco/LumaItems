package dev.jsinco.lumaitems.util

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.FileManager
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

object AbilityUtil {

    val plugin: LumaItems = LumaItems.getPlugin()
    val blockTypeBlacklist = mutableListOf( // Move to YAML file
        Material.CHEST,
        Material.BARREL,
        Material.TRAPPED_CHEST,
        Material.FURNACE,
        Material.BLAST_FURNACE,
        Material.SMOKER,
        Material.HOPPER,
        Material.BREWING_STAND,
        Material.DROPPER,
        Material.DISPENSER,
        Material.BEDROCK,
        Material.END_PORTAL_FRAME,
        Material.SPAWNER,
        Material.COMMAND_BLOCK,
        Material.BARRIER,
        Material.STRUCTURE_BLOCK,
        Material.JIGSAW,
        Material.END_GATEWAY,
        Material.BUDDING_AMETHYST,
        Material.FARMLAND,
        Material.DIRT_PATH,
        Material.END_PORTAL,
        Material.END_GATEWAY,
        Material.LAVA,
        Material.WATER,
        Material.AIR,
        Material.CAVE_AIR,
        Material.VOID_AIR
    )
    private val blockedAbility: MutableSet<UUID> = mutableSetOf()

    init {
        // Shulker boxes
        for (material in Material.entries) {
            if (material.name.contains("SHULKER_BOX")) {
                blockTypeBlacklist.add(material)
            }
        }
    }

    @JvmStatic
    fun noDamagePermission(attacker: Player, damagee: Entity): Boolean {
        val event = EntityDamageByEntityEvent(attacker, damagee, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0.1)
        Bukkit.getPluginManager().callEvent(event)
        return event.isCancelled
    }

    @JvmStatic
    fun noBuildPermission(player: Player, block: Block): Boolean {
        val event = BlockPlaceEvent(block, block.state, block.getRelative(BlockFace.DOWN), ItemStack(Material.AIR), player, true, EquipmentSlot.HAND)
        Bukkit.getPluginManager().callEvent(event)
        return event.isCancelled
    }

    fun getDirectionBetweenLocations(start: Location, end: Location): Vector {
        val from = start.toVector()
        val to = end.toVector()
        return to.subtract(from)
    }

    fun findMostCommonItem(collection: Collection<ItemStack>): ItemStack {
        return collection.groupingBy { it }
            .eachCount()
            .maxBy { it.value }
            .key
    }

    fun isOnGround(entity: Entity): Boolean {
        return !entity.location.add(0.0,-0.1, 0.0).block.type.isAir
    }

    fun breakRelativeBlock(block: Block, player: Player, particle: Particle?, type: String, limiterInitial: Int) {
        var limiter = limiterInitial
        if (blockedAbility.contains(player.uniqueId)) return
        val faces =
            arrayOf(BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST)
        //Loop through all block faces (All 6 sides around the block)
        if (limiter > 8) return
        // edit this
        for (face in faces) {
            val b = block.getRelative(face)
            if (b.type.toString().lowercase(Locale.getDefault()).contains(type)) {
                if (particle != null) {
                    b.world.spawnParticle(Particle.BLOCK_CRACK, b.location, 5, 0.5, 0.5, 0.5, 0.1, b.blockData)
                    b.world.spawnParticle(particle, b.location, 2, 0.5, 0.5, 0.5, 0.1)
                }
                blockedAbility.add(player.uniqueId)
                player.breakBlock(b)
                blockedAbility.remove(player.uniqueId)
                block.breakNaturally(player.inventory.itemInMainHand)
                if (type == "leaves") {
                    limiter++
                }
                val finalLimiter = limiter
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                    { breakRelativeBlock(b, player, particle, type, finalLimiter) }, 1L
                )
            }
        }
    }

    // Usage: DarkMoonMattockItem, DarkMoonShovelItem
    fun breakThreeByThree(block: Block, player: Player, restrict: List<Material?>?) {
        if (blockedAbility.contains(player.uniqueId)) return
        val cube = Cuboid(
            block.location.add(-1.0, -1.0, -1.0),
            block.location.add(1.0, 1.0, 1.0)
        )
        blockedAbility.add(player.uniqueId)
        if (restrict != null) {
            for (i in 0 until cube.blockList().size) {
                val b: Block = cube.blockList()[i]
                if (blockTypeBlacklist.contains(b.type) || !restrict.contains(b.type)) continue
                b.world.spawnParticle(Particle.BLOCK_DUST, b.location.add(0.5, 0.5, 0.5), 10, 0.5, 0.5, 0.5, 0.1, b.blockData)
                player.breakBlock(b)
            }
        } else {
            for (i in 0 until cube.blockList().size) {
                val b: Block = cube.blockList()[i]
                if (blockTypeBlacklist.contains(b.type)) continue
                b.world.spawnParticle(
                    Particle.BLOCK_DUST, b.location.add(0.5, 0.5, 0.5), 10, 0.5, 0.5, 0.5, 0.1, b.blockData)
                player.breakBlock(b)
            }
        }
        blockedAbility.remove(player.uniqueId)
    }

    // Usage: Stellaris' Set
    fun pinataAbility(block: Block) {
        if (Random().nextInt(32000) >= 14) return

        val pinataFile = FileManager("saves/pinata.yml").getFileYaml()
        val items = pinataFile.getConfigurationSection("items")!!.getKeys(false)
        val rareItems = pinataFile.getConfigurationSection("rare-items")!!.getKeys(false)

        val item = if (Random().nextInt(20) <= 5) {
            val item = rareItems.random()
            val itemStack = pinataFile.getItemStack("rare-items.$item")
            itemStack
        } else {
            val item = items.random()
            val itemStack = pinataFile.getItemStack("items.$item")
            itemStack
        }

        if (item != null) {
            block.world.dropItemNaturally(block.location, item)
        }
        block.world.spawnParticle(Particle.REDSTONE, block.location, 50, 0.5, 0.5, 0.5, 0.1, DustOptions(Color.fromRGB(106, 219, 255), 2f))
        block.world.spawnParticle(Particle.REDSTONE, block.location, 50, 0.5, 0.5, 0.5, 0.1, DustOptions(Color.fromRGB(255, 121, 209), 2f))
        block.world.playSound(block.location, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1f, 1f)

    }

    fun spawnSpell(player: Player, particle: Particle?, meta: String, ticksAlive: Long) {
        val snowball = player.launchProjectile(Snowball::class.java)
        snowball.setGravity(false)
        snowball.velocity = player.location.direction.multiply(3)
        snowball.persistentDataContainer.set(NamespacedKey(plugin, meta), PersistentDataType.SHORT, 1)
        player.hideEntity(plugin, snowball)
        for (entity in player.getNearbyEntities(65.0, 65.0, 65.0)) {
            if (entity is Player) {
                entity.hideEntity(plugin, snowball)
            }
        }

        object : BukkitRunnable() {
            override fun run() {
                if (snowball.isDead) {
                    cancel()
                }
                if (particle != null) {
                    snowball.world.spawnParticle(particle, snowball.location, 4, 0.1, 0.1, 0.1, 0.0)
                }
            }
        }.runTaskTimer(plugin, 0, 1)
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            if (!snowball.isDead) {
                snowball.remove()
            }
        }, ticksAlive)
    }
}