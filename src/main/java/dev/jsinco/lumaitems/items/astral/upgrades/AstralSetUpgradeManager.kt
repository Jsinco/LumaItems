package dev.jsinco.lumaitems.items.astral.upgrades

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.FileManager
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

open class AstralSetUpgradeManager {
    companion object {
        @JvmStatic val upgrades: MutableMap<String, MutableList<AstralUpgradeTier>> = mutableMapOf()
        val plugin: LumaItems = LumaItems.getPlugin()
    }

    init {
        //reloadUpgrades()
    }

    fun reloadUpgrades() {
        val fileManager = FileManager("astral.yml").generateYamlFile()

        for (setKey in fileManager.getConfigurationSection("astral-upgrades")?.getKeys(false) ?: throw NullPointerException("Section does not exist")) {
            val setTierSection = fileManager.getConfigurationSection("astral-upgrades.$setKey") ?: continue
            val setTierSectionList = setTierSection.getKeys(false)
            for (setTier in setTierSectionList) {
                val astralMaterial = AstralMaterial.valueOf(setTierSection.getString("$setTier.material") ?: "IRON")
                val enchantsListString = setTierSection.getStringList("$setTier.enchants")
                val setTierNumber = setTier.replace("tier-", "").trim().toInt()

                val astralUpgradeTier = AstralUpgradeTier(
                    setKey,
                    astralMaterial,
                    getEnchantsFromStringList(enchantsListString),
                    setTierNumber,
                    setTierSectionList.last() == setTier
                )

                if (upgrades.contains(setKey)) {
                    upgrades[setKey]?.add(astralUpgradeTier)
                } else {
                    upgrades[setKey] = mutableListOf(astralUpgradeTier)
                }
            }
        }
    }

    fun getEnchantsFromStringList(stringEnchants: MutableList<String>): Map<Enchantment, Int> {
        val enchants: MutableMap<Enchantment, Int> = mutableMapOf()
        for (stringEnchant in stringEnchants) {
            val split = stringEnchant.split("/")

            val enchantment: Enchantment = Enchantment.getByKey(NamespacedKey.minecraft(split[0])) ?: continue
            enchants[enchantment] = split[1].toInt()
        }
        return enchants
    }
}