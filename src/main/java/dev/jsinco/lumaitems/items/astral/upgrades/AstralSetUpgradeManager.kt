package dev.jsinco.lumaitems.items.astral.upgrades

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.util.GenericMCToolType
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

open class AstralSetUpgradeManager {
    companion object {
        val modifiableMaterials: List<GenericMCToolType> = listOf(
            GenericMCToolType.HELMET, GenericMCToolType.CHESTPLATE, GenericMCToolType.LEGGINGS, GenericMCToolType.BOOTS,
            GenericMCToolType.SWORD, GenericMCToolType.PICKAXE, GenericMCToolType.AXE, GenericMCToolType.SHOVEL, GenericMCToolType.HOE
        )

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

    fun getEnchantsFromStringList(stringEnchants: MutableList<String>): List<AstralUpgradeEnchantment> {
        val enchants: MutableList<AstralUpgradeEnchantment> = mutableListOf()
        for (stringEnchant in stringEnchants) {
            var force = false
            var finalStringEnchant = stringEnchant

            if (finalStringEnchant.contains("-force")) {
                force = true
                finalStringEnchant = finalStringEnchant.replace("-force", "").replace(" ", "").trim()
            }

            val split = finalStringEnchant.split("/")

            val enchantment: Enchantment = Enchantment.getByKey(NamespacedKey.minecraft(split[0])) ?: continue
            enchants.add(AstralUpgradeEnchantment(enchantment, split[1].toInt(), force))
        }
        return enchants
    }
}