package dev.jsinco.lumaitems.items.astral.upgrades

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class AstralSetUpgradeFactory (astralItem: ItemStack) : AstralSetUpgradeManager() {



    private fun determineUpgradeTier(item: ItemStack): AstralUpgradeTier? {
        val dataContainer = item.itemMeta?.persistentDataContainer ?: return null
        for (upgrade in upgrades) {
            if (dataContainer.has(NamespacedKey(plugin, upgrade.key), PersistentDataType.SHORT)) {
                return upgrades[upgrade.key]
            }
        }
        return null
    }
}