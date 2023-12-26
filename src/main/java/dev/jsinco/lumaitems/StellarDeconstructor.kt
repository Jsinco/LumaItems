package dev.jsinco.lumaitems

import dev.jsinco.lumaitems.items.misc.StellarStarItem
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.random.Random


class StellarDeconstructor(val plugin: LumaItems) : Listener {

    companion object {
        private val deconstructorBlockLocations: MutableList<Location> = mutableListOf()
        private val file = FileManager("stellarstars.yml").getFileYaml()

        private val confirmCooldownTasks: MutableMap<UUID, Int> = mutableMapOf()
    }

    init {
        if (file.getConfigurationSection("deconstructor-blocks") == null) {
            throw IllegalArgumentException("deconstructor-blocks config section is null!")
        }
        for (key in file.getConfigurationSection("deconstructor-blocks")?.getKeys(false)!!) {
            val loc = Location(
                Bukkit.getWorld(file.getString("deconstructor-blocks.$key.world")!!),
                file.getDouble("deconstructor-blocks.$key.x"),
                file.getDouble("deconstructor-blocks.$key.y"),
                file.getDouble("deconstructor-blocks.$key.z")
            ).toCenterLocation()

            deconstructorBlockLocations.add(loc)
        }
    }

    init {
        val key = NamespacedKey(plugin, "stellar")

        if (Bukkit.getRecipe(key) == null) {
            val recipe = ShapedRecipe(key, StellarStarItem().createItem().second)
            recipe.shape(
                "AAA",
                "A A",
                "AAA")
            recipe.setIngredient('A', getStellarDustItem())
            Bukkit.addRecipe(recipe)
        }
    }




    fun getStellarDustItem(): ItemStack {
        return Util.createBasicItem("&#abd7f7&lS&#b6d2f8&lt&#c2ccf9&le&#cdc7fa&ll&#d8c2fb&ll&#e4bcfc&la&#efb7fd&lr &#E2E2E2Dust",
            listOf("No Lore Yet"),
            Material.SUGAR,
            listOf("stellardust"),
            glint = true)
    }

    private fun confirmMessage(player: Player, itemName: String): Boolean {
        if (confirmCooldownTasks.contains(player.uniqueId)) {
            rescheduleCooldownTask(player.uniqueId)
            return true
        }

        player.sendMessage(Util.colorcode("${Util.prefix} Are you sure you want to deconstruct your $itemName&#E2E2E2? This action cannot be undone!"))
        rescheduleCooldownTask(player.uniqueId)
        return false
    }

    private fun rescheduleCooldownTask(uuid: UUID) {
        if (confirmCooldownTasks.contains(uuid)) {
            Bukkit.getScheduler().cancelTask(confirmCooldownTasks[uuid]!!)
        }

        confirmCooldownTasks[uuid] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            confirmCooldownTasks.remove(uuid)
        }, 200)
    }

    fun giveItemForDeconstruction(player: Player) {
        val randomItemCommands = file.getStringList("deconstructor-random-items")

        if (Random.nextBoolean()) {
            Util.giveItem(player, getStellarDustItem())
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), randomItemCommands.random()
                .replace("%player%", player.name))
        }
    }


    @EventHandler
    fun onInteractWithStellarDeconstructor(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        if (!deconstructorBlockLocations.contains(block.location.toCenterLocation())) return
        event.isCancelled = true
        val itemMeta = event.item?.itemMeta ?: return

        if (itemMeta.persistentDataContainer.has(NamespacedKey(plugin, "stellar"), PersistentDataType.SHORT)) {

            if (confirmMessage(event.player, itemMeta.displayName)) {
                event.item!!.amount -= 1
                giveItemForDeconstruction(event.player)
            }
        }
    }
}