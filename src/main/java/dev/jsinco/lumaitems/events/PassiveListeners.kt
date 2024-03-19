package dev.jsinco.lumaitems.events

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.ItemManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

/**
 * This class is used to listen for passive events
 */
class PassiveListeners(val plugin: LumaItems) {

    // TODO: turn to abstract class and use a runnable manager?

    fun startMainRunnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            for (player in Bukkit.getOnlinePlayers()) {
                val datas: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)
                for (data in datas) {
                    for (customItem in ItemManager.customItems) {
                        if (!data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT))  continue
                        val customItemClass = customItem.value
                        customItemClass.executeAbilities(Ability.RUNNABLE, player, 0)
                    }
                }
            }
        }, 0L, 70L)
    }
}