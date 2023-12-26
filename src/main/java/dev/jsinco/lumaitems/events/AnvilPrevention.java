package dev.jsinco.lumaitems.events;

import dev.jsinco.lumaitems.LumaItems;
import dev.jsinco.lumaitems.manager.ItemManager;
import dev.jsinco.lumaitems.util.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


/**
 * Class to prevent certain custom items from being used
 * in anvils
 */
public class AnvilPrevention implements Listener {

    public LumaItems plugin;

    public AnvilPrevention(LumaItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        if (event.getResult() == null || !event.getResult().hasItemMeta()) {
            return;
        }

        ItemMeta meta = event.getResult().getItemMeta();
        boolean cancelEvent = false;

        if (meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "lumaitem"), PersistentDataType.SHORT)) {
            cancelEvent = true;
        } else {
            for (String key : ItemManager.customItems.keySet()) {
                if (meta.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.SHORT)) {
                    cancelEvent = true;
                    break;
                }
            }
        }


        if (cancelEvent) {
            event.setResult(null);
            event.getView().getPlayer().sendMessage(Util.prefix + "You cannot use this item in an anvil!");
        }
    }
}
