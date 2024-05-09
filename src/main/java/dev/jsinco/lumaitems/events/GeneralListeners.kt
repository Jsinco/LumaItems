package dev.jsinco.lumaitems.events

import com.gamingmesh.jobs.api.JobsPrePaymentEvent
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.guis.AbstractGui
import dev.jsinco.lumaitems.guis.DisassemblerGui
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.relics.RelicCreator
import dev.jsinco.lumaitems.relics.RelicDisassembler
import dev.jsinco.lumaitems.util.EntityArmor
import dev.jsinco.lumaitems.util.ToolType
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Enemy
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random

class GeneralListeners(val plugin: LumaItems) : Listener {

    companion object {
        val relicFile = FileManager("relics.yml").generateYamlFile()
        private val bosses: List<EntityType> = listOf(
            EntityType.ENDER_DRAGON,
            EntityType.WITHER,
            EntityType.ELDER_GUARDIAN,
            EntityType.WARDEN
        )
    }

    @EventHandler
    fun onEntitySpawn(event: EntitySpawnEvent) {
        val livingEntity = event.entity as? LivingEntity ?: return
        val isBoss = bosses.contains(livingEntity.type)

        if (Random.nextInt(100) > 15 || livingEntity !is Enemy) return // 15% chance to spawn a relic

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val rarity: Rarity = if (isBoss) Rarity.bossRarities[0] else Rarity.genericRarities.random()
            val material: Material =
                Material.valueOf(relicFile.getStringList("relic-materials.${rarity.name.lowercase()}").random())

            val relic = RelicCreator(
                rarity.algorithmWeight,
                -1,
                rarity,
                material
            ).getRelicItem()

            Bukkit.getScheduler().runTask(plugin, Runnable {
                if (ToolType.getToolType(relic.type) == ToolType.ARMOR) {
                    val entityArmor = EntityArmor.getEquipmentSlotFromType(relic.type)
                    entityArmor?.setEntityArmorSlot(livingEntity, relic)
                } else {
                    livingEntity.equipment?.setItemInOffHand(relic)
                }
            })
        })
    }

    @EventHandler
    fun onDisassemblerInteract(event: PlayerInteractEvent) {
        if (!RelicDisassembler.disassemblerBlocks.contains(event.clickedBlock ?: return)) return

        event.isCancelled = true
        val player = event.player
        val item = player.inventory.itemInMainHand

        if (event.action.isLeftClick && player.hasPermission("lumaitems.disassemblergui")) {
            val gui = DisassemblerGui()
            player.openInventory(gui.getInventory())
            return
        }

        val command = RelicDisassembler.getCommandToExecute(item, event.action, player) ?: return

        item.amount -= 1
        player.playSound(player.location, Sound.ENTITY_SQUID_SQUIRT, 1f, 0.9f)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory.getHolder(false) is AbstractGui) {
            (event.inventory.holder as AbstractGui).onInventoryClick(event)
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryCloseEvent) {
        if (event.inventory.getHolder(false) !is AbstractGui) return
        (event.inventory.holder as AbstractGui).onInventoryClose(event)
    }


    @EventHandler
    fun onJobsPrePayment(event: JobsPrePaymentEvent) {
        if (Random.nextInt(3000) > 2 || event.job.name == "Hunter") return
        val player = event.player?.player ?: return


        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val rarity = Rarity.genericRarities.random()
            val material: Material =
                Material.valueOf(relicFile.getStringList("relic-materials.${rarity.name.lowercase()}").random())

            val relic = RelicCreator(
                rarity.algorithmWeight,
                -1,
                rarity,
                material
            ).getRelicItem()

            Bukkit.getScheduler().runTask(plugin, Runnable {
                Util.giveItem(player, relic)
            })
        })
    }
}