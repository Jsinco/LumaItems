package dev.jsinco.lumaitems.relics

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

class RelicCreator (
    private val algorithmWeight: Int,
    private var forcedMaxEnchantLevel: Int,
    private val rarity: Rarity,
    private val material: Material
) {
    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
        private const val RELIC_SUFFIX_RGB = "&f"
        private val blackListedEnchants: List<Enchantment> = listOf(Enchantment.BINDING_CURSE,  Enchantment.VANISHING_CURSE, Enchantment.FROST_WALKER, Enchantment.SWIFT_SNEAK)
        private val limitedEnchantLevel: Map<Enchantment, Int> = mapOf(
            Enchantment.SILK_TOUCH to 1,
            Enchantment.MENDING to 1,
            Enchantment.ARROW_FIRE to 1,
        )
    }

    private val file = FileManager("relics.yml").generateYamlFile()
    private val relicPrefixes: List<String> = file.getStringList("names.prefixes")
    private val relicSuffixes: List<String> = file.getStringList("names.suffixes")

    private val itemCreator: CreateItem

    init {
        if (forcedMaxEnchantLevel < 1) forcedMaxEnchantLevel = algorithmWeight

        val enchantments: Map<Enchantment, Int> = determineEnchants()
        val name: String = determineName()

        itemCreator = CreateItem(
            name,
            mutableListOf(),
            mutableListOf(),
            material,
            mutableListOf("relic-item"),
            enchantments.toMutableMap()
        )
        itemCreator.stringPersistentDatas[NamespacedKey(plugin, "relic-rarity")] = rarity.name
        itemCreator.tier = rarity.tier
        itemCreator.addHiddenCharacter = false
    }



    private fun determineEnchants(): Map<Enchantment, Int> {
        val enchantments: MutableMap<Enchantment, Int> = mutableMapOf()
        val numOfEnchants = Random.nextInt(1 + (algorithmWeight - 3).coerceAtMost(1), algorithmWeight)

        for (i in 0..numOfEnchants) {
            var enchantment = Enchantment.values().random()
            while (enchantments.keys.contains(enchantment) || blackListedEnchants.contains(enchantment)) {
                enchantment = Enchantment.values().random()
            }

            var containsMendingAndSilk = enchantments.keys.contains(Enchantment.MENDING) && enchantment == Enchantment.SILK_TOUCH || enchantments.keys.contains(Enchantment.SILK_TOUCH) && enchantment == Enchantment.MENDING
            if (5 - algorithmWeight < Random.nextInt(algorithmWeight) && containsMendingAndSilk) {
                while (containsMendingAndSilk) {
                    enchantment = Enchantment.values().random()
                    containsMendingAndSilk = enchantments.keys.contains(Enchantment.MENDING) && enchantment == Enchantment.SILK_TOUCH || enchantments.keys.contains(Enchantment.SILK_TOUCH) && enchantment == Enchantment.MENDING
                }
            }
            // need to give higher numbers less of a likelyhood to be chosen
            var enchantmentLevel: Int = if (forcedMaxEnchantLevel >= 5) {
                if (Random.nextInt(100) < 15) {
                    Random.nextInt(5, forcedMaxEnchantLevel + 1) //
                } else {
                    Random.nextInt(1, 5)
                }
            } else {
                Random.nextInt(1, forcedMaxEnchantLevel + 1)
            }

            if (limitedEnchantLevel.containsKey(enchantment)) {
                enchantmentLevel = limitedEnchantLevel[enchantment]!!
            }

            enchantments[enchantment] = enchantmentLevel
        }

        return enchantments
    }

    private fun determineName(): String {
        return Util.colorcode("${rarity.getRgb()}&l${relicPrefixes.random()} $RELIC_SUFFIX_RGB${relicSuffixes.random()}")
    }

    fun getRelicItem(): ItemStack {
        return itemCreator.createItem()
    }
}

/*
    /**
     * Algorithm to determine the enchantments of the relic.
     * Higher algorithm weight:
     * - Higher chance of getting more enchantments
     * - Higher chance of getting higher enchantment levels
     * - Higher chance of getting enchantments that are 'allowed' to go on the item ".canEnchantItem(item)"
     * - Higher chance of getting mending with no silk touch or silk touch with no mending
     */
    fun determineEnchants(): Map<Enchantment, Int> {
        val numOfEnchants = Random.nextInt(algorithmWeight, algorithmWeight + Random.nextInt(1, algorithmWeight + 1))

        val enchantments = mutableMapOf<Enchantment, Int>()

        for (i in 0..numOfEnchants) {
            var enchantment = Enchantment.values().random()
            while (enchantments.keys.contains(enchantment)) {
                enchantment = Enchantment.values().random()
            }

            val enchantmentLevel = Random.nextInt(1, forcedMaxEnchantLevel + 1)

            if (3 - algorithmWeight < Random.nextInt(algorithmWeight)) {
                if (!enchantment.canEnchantItem(item)) continue
            }
            if (2 - algorithmWeight < Random.nextInt(algorithmWeight)) {
                if (enchantments.keys.contains(Enchantment.MENDING) && enchantment == Enchantment.SILK_TOUCH) continue
                else if (enchantments.keys.contains(Enchantment.SILK_TOUCH) && enchantment == Enchantment.MENDING) continue
            }


            enchantments[enchantment] = enchantmentLevel
        }
        return enchantments
    }
 */