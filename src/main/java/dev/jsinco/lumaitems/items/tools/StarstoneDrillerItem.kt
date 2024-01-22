package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class StarstoneDrillerItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#fbf8ae&lS&#fbf4a5&lt&#fcf09c&la&#fcec93&lr&#fce98a&ls&#fce581&lt&#fde178&lo&#fddd6f&ln&#fdd967&le &#fdd55e&lD&#fed155&lr&#fecd4c&li&#feca43&ll&#fec63a&ll&#ffc231&le&#ffbe28&lr",
            mutableListOf("&#f7d149H&#f7d149a&#f7d149s&#f7d149t&#f7d149e&#f7d149n"),
            mutableListOf("&#ffbe28\"&#ffc02cF&#ffc231o&#ffc435r&#fec63ag&#fec83ee&#feca43d &#fecc47i&#fecd4cn &#fecf50t&#fed155h&#fed359e &#fdd55eh&#fdd762e&#fdd967a&#fddb6br&#fddd6ft &#fddf74o&#fde178f &#fce37da &#fce581f&#fce786a&#fce98al&#fcea8fl&#fcec93e&#fcee98n &#fcf09cs&#fbf2a1t&#fbf4a5a&#fbf6aar&#fbf8ae\"","","&fGrants different levels of Haste varying","&fon how deep you are underground"),
            Material.NETHERITE_PICKAXE,
            mutableListOf("starstonedriller"),
            mutableMapOf(Enchantment.DIG_SPEED to 7, Enchantment.LOOT_BONUS_BLOCKS to 5, Enchantment.DURABILITY to 7, Enchantment.MENDING to 1)
        )
        return Pair("starstonedriller", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RUNNABLE -> {
                hasten(player)
            }
            else -> return false
        }
        return true
    }


    private fun hasten(player: Player) {
        val y = player.location.y
        if (y <= -20) player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 220, 2, false, false, true))
        else if (y <= 20) player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 220, 1, false, false, true))
        else if (y <= 60) player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 220, 0, false, false, true))
    }
}