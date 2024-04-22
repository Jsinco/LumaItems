package dev.jsinco.lumaitems.items.astral.upgrades

import dev.jsinco.lumaitems.util.GenericMCToolType
import dev.jsinco.lumaitems.util.Util
import org.bukkit.ChatColor
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
        if (upgradeTier.maxTier) {
            updateAstralItemTier(item)
        }
        return true
    }
    

    private fun determineUpgradeTier(): AstralUpgradeTier? {
        val dataContainer = item.itemMeta?.persistentDataContainer ?: return null
        for (upgrade in upgrades) {
            val tierNumber = dataContainer.get(NamespacedKey(plugin, upgrade.key), PersistentDataType.SHORT) ?: continue
            for (upgradeTier: AstralUpgradeTier in upgrade.value) {
                if (upgradeTier.tierNumber == tierNumber.toInt() + 1) {
                    return upgradeTier
                }
            }
        }
        return null
    }

    private fun updateAstralItemTier(item: ItemStack) {
        val meta = item.itemMeta ?: return

        val currentLore = meta.lore ?: return
        for ((i, loreLine) in currentLore.withIndex()) {
            val loreLineStripped = ChatColor.stripColor(loreLine) ?: continue
            if (loreLineStripped.contains("Tier • Astral")) {
                currentLore[i] = Util.colorcode("&#EEE1D5Tier • &#E97979&lAstral&c+")
                break
            }
        }

        meta.lore = currentLore
        item.itemMeta = meta
    }

    companion object {
        fun upgradeAstralItem(item: ItemStack, upgradeTier: AstralUpgradeTier) {

            if (modifiableMaterials.contains(GenericMCToolType.getToolType(item))) {
                // TODO: Look more into exactly why this is deprecated. Haven't experienced any of the issues mentioned
                val originalGearType = item.type.toString().split("_")[1]
                item.type = Material.valueOf("${upgradeTier.newMaterial}_${originalGearType}")
            }

            val meta = item.itemMeta ?: return

            for (astralUpgradeEnchant in upgradeTier.newEnchantments) {
                val enchantment = astralUpgradeEnchant.enchantment
                if (enchantment.canEnchantItem(item) || astralUpgradeEnchant.force) {
                    meta.addEnchant(enchantment, astralUpgradeEnchant.level, true)
                }
            }

            meta.persistentDataContainer.set(NamespacedKey(plugin, upgradeTier.tierName), PersistentDataType.SHORT, upgradeTier.tierNumber.toShort())
            item.itemMeta = meta
        }
    }
}