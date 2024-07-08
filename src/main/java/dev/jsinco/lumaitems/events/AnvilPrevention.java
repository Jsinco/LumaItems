package dev.jsinco.lumaitems.events;

import dev.jsinco.lumaitems.LumaItems;
import dev.jsinco.lumaitems.manager.ItemManager;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
            for (NamespacedKey key : ItemManager.customItems.keySet()) {
                if (meta.getPersistentDataContainer().has(key, PersistentDataType.SHORT)) {
                    cancelEvent = true;
                    break;
                }
            }
        }


        if (cancelEvent && event.getInventory().getSecondItem() != null) {
            event.setResult(null);
        }
    }
}
