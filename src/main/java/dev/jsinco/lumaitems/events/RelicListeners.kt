package dev.jsinco.lumaitems.events

import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.relics.RelicCreator
import dev.jsinco.lumaitems.relics.RelicDisassembler
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Enemy
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random

class RelicListeners : Listener {

    companion object {
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

        if (Random.nextInt(100) > 15 && !isBoss) return // 15% chance to spawn a relic



        val rarity: Rarity = if (isBoss) Rarity.bossRarities[0] else Rarity.genericRarities.random()
        val material: Material = Material.valueOf(FileManager("relics.yml").generateYamlFile().getStringList("relic-materials.${rarity.name.lowercase()}").random())

        val relicCreator = RelicCreator(
            rarity.algorithmWeight,
            -1,
            rarity,
            material
        )

        val item = relicCreator.getRelicItem()
        if (isBoss){
            livingEntity.equipment?.setItemInOffHand(item)
        } else if (livingEntity is Enemy) {
            livingEntity.equipment?.setItemInOffHand(item)
        }
    }

    @EventHandler
    fun onDisassemblerInteract(event: PlayerInteractEvent) {
        if (!RelicDisassembler.disassemblerBlocks.contains(event.clickedBlock ?: return)) return
        event.isCancelled = true
        val player = event.player
        val item = player.inventory.itemInMainHand

        val command = RelicDisassembler.getCommandToExecute(item, event.action, player) ?: return

        item.amount -= 1
        player.playSound(player.location, Sound.ENTITY_SQUID_SQUIRT, 1f, 0.9f)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }
}