package dev.jsinco.lumaitems.items.astral.upgrades

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

// Notes:
// Upgrade tier names should be the set's identifiers (e.g. mistral-set) and should have a short value of the actual tier.

class AstralSetUpgradeFactory (val item: ItemStack) : AstralSetUpgradeManager() {

    fun upgrade(): Boolean {
        val upgradeTier: AstralUpgradeTier = determineUpgradeTier() ?: return false
        upgradeAstralItem(item, upgradeTier)
        return true
    }


    private fun determineUpgradeTier(): AstralUpgradeTier? {
        val dataContainer = item.itemMeta?.persistentDataContainer ?: return null
        for (upgrade in upgrades) {
            val tierNumber = dataContainer.get(NamespacedKey(plugin, upgrade.key), PersistentDataType.SHORT) ?: continue

            for (upgradeTier in upgrade.value) {
                if (upgradeTier.tierNumber == tierNumber.toInt()) {
                    return upgradeTier
                }
            }
        }
        return null
    }

    companion object {
        fun upgradeAstralItem(item: ItemStack, upgradeTier: AstralUpgradeTier) {
            val originalGearType = item.type.toString().split("_")[0]
            item.type = Material.valueOf("${originalGearType}_${originalGearType}")

            val meta = item.itemMeta ?: return

            for (enchant in upgradeTier.newEnchantments) {
                meta.addEnchant(enchant.key, enchant.value, true)
            }

            meta.persistentDataContainer.set(NamespacedKey(plugin, upgradeTier.tierName), PersistentDataType.SHORT, upgradeTier.tierNumber.toShort())
            item.itemMeta = meta
        }
    }
}