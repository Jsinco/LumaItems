package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.*
import org.bukkit.entity.Entity
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class Unnamed : CustomItem {

    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
        val pinataFile = FileManager("saves/pinata.yml").getFileYaml()
        private val normalItems: List<Material> = listOf(
            Material.SPONGE,
            Material.DIAMOND,
            Material.GOLD_BLOCK,
            Material.SCUTE,
            Material.PHANTOM_MEMBRANE,
            Material.ANCIENT_DEBRIS,
            Material.GOLDEN_APPLE,
            Material.EMERALD_BLOCK
        )
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = CreateItem(
            "Unnamed",
            mutableListOf(""),
            mutableListOf(""),
            Material.FISHING_ROD,
            mutableListOf("unnamed"),
            mutableMapOf()
        )
        return Pair("unnamed", item.createItem())
    }



    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.FISH -> {
                event as PlayerFishEvent
                if (event.state !=  PlayerFishEvent.State.CAUGHT_FISH) return false
                spawnBarrel(event.caught!!)
                watchBarrel()
            }
            Ability.ENTITY_CHANGE_BLOCK -> {
                event as EntityChangeBlockEvent
                event.isCancelled = true
            }
            else -> return false
        }
        return true
    }

    private lateinit var barrel: FallingBlock

    fun spawnBarrel(hook: Entity) {
        barrel = hook.world.spawnFallingBlock(hook.location, Material.BARREL.createBlockData())
        barrel.persistentDataContainer.set(NamespacedKey(plugin, "unnamed"), PersistentDataType.SHORT, 1)
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            barrel.velocity = hook.velocity.multiply(1.1)
        }, 1)
    }

    fun watchBarrel() {
        var limit = 0
        object : BukkitRunnable() {
            override fun run() {
                if (barrel.isDead || limit >= 60) {
                    dropLoot()
                    barrel.remove()
                    this.cancel()
                }
                limit++
            }
        }.runTaskTimer(plugin, 0, 1)
    }

    fun dropLoot(loc: Location = barrel.location) {
        loc.world.playSound(loc, Sound.ITEM_TOTEM_USE, 1f, 1f)
        loc.world.spawnParticle(Particle.TOTEM, loc, 50, 0.5, 0.5, 0.5, 0.1)
        for (i in 0..Random.nextInt(3,5)) {
            if (Random.nextBoolean()) {
                val rareItems = pinataFile.getConfigurationSection("rare-items")!!.getKeys(false)
                val itemStack = pinataFile.getItemStack("rare-items.${rareItems.random()}")!!
                loc.world.dropItem(loc, itemStack)

            }
            loc.world.dropItem(loc, ItemStack(normalItems.random(), Random.nextInt(7)))
        }
    }
}