package dev.jsinco.lumaitems.items.astral.upgrades

import org.bukkit.enchantments.Enchantment

data class AstralUpgradeEnchantment (
    val enchantment: Enchantment,
    val level: Int,
    val force: Boolean // TODO: Probably just change this whole system to only apply to set gear types.
)