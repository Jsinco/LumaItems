package dev.jsinco.lumaitems.items.magical

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.enums.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.enums.Tier
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BookOfKnowledgeItem : CustomItem {

    companion object {
        const val STRING_KEY = "bookofknowledge"
    }

    override fun createItem(): Pair<String, ItemStack> {
        return ItemFactory.builder()
            .name("<b><#7A2E19>B<#8F4A2D>o<#A56541>o<#BA8154>k <#B48559>o<#9A6F49>f <#64412A>K<#724B2E>n<#815531>o<#8F5E35>w<#9D6838>l<#9D6838>e<#9D6838>d<#9D6838>g<#9D6838>e</b>")
            .customEnchants("<#CF9C68>Mastery")
            .lore("Experience orbs give more experience", "while holding this item.", "",
                "If paired with a <b><#C7305D>M<#962F72>a<#642D87>g<#8D3A71>i<#B6475C>c <#C45078>W<#A94CAA>a<#A94CAA>n<#A94CAA>d</b><white>,",
                "extra spells will be accessible.",
                "",
                "<#CF9C68>Drain <dark_gray>- <white>Click to siphon health", "from nearby entities.",
                "",
                "<#CF9C68>Valiant Explosion <dark_gray>- <white>Cast a", "spell which will damage", "and explode entities.")
            .material(Material.BOOK)
            .persistentData(STRING_KEY)
            .tier(Tier.CARNIVAL_2024)
            .vanillaEnchants(mutableMapOf(Enchantment.UNBREAKING to 10))
            .buildPair()
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when(type) {
            Action.PLAYER_PICKUP_EXP -> {
                event as PlayerPickupExperienceEvent
                event.experienceOrb.experience += 1
            }
            else -> return false
        }
        return true
    }
}