package dev.jsinco.lumaitems.events

import dev.jsinco.lumaitems.guis.GUI
import dev.jsinco.lumaitems.guis.GUIHolder
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.relics.Rarity
import dev.jsinco.lumaitems.relics.RelicCreator
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.inventory.InventoryClickEvent
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
        if (Random.nextInt(100) > 15) return // 15% chance to spawn a relic

        val isBoss = bosses.contains(livingEntity.type)

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
        } else if (livingEntity is Monster) {
            livingEntity.equipment.setItemInOffHand(item)
        }
    }


    // TODO: Move this
    @EventHandler
    fun onInvClick(event: InventoryClickEvent) {
        if (event.inventory.holder !is GUIHolder) return

        val guiHolder = event.inventory.holder as GUIHolder
        guiHolder.guiClass.handleClick(event)
    }
}