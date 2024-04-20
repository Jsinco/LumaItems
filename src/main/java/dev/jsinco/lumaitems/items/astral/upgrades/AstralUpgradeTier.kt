package dev.jsinco.lumaitems.items.astral.upgrades

import org.bukkit.enchantments.Enchantment

class AstralUpgradeTier(
    val tierName: String,
    val newMaterial: AstralMaterial,
    val newEnchantments: Map<Enchantment, Int>,
    val tierNumber: Int,
    val maxTier: Boolean
) {
    override fun toString(): String {
        return "AstralUpgradeTier(tierName='$tierName', newMaterial=$newMaterial, newEnchantments=$newEnchantments, tierNumber=$tierNumber, maxTier=$maxTier)"
    }
}