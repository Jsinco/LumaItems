package dev.jsinco.lumaitems.events

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.ItemManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable

/**
 * This class is used to listen for passive events
 */
class PassiveListeners(val plugin: LumaItems) : BukkitRunnable() {
    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            val dataList: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)

            for (data: PersistentDataContainer in dataList) {
                for (customItem in ItemManager.customItems) {
                    if (!data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
                    customItem.value.executeAbilities(Ability.RUNNABLE, player, 0)
                }
            }
        }
    }

}