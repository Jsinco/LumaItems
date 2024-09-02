package dev.jsinco.lumaitems.api;

import dev.jsinco.lumaitems.LumaItems;
import dev.jsinco.lumaitems.manager.CustomItem;
import dev.jsinco.lumaitems.manager.ItemManager;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * LumaItemsAPI. Should always grab a new instance of this class because LumaItems may be subject
 * to reloads.
 */
public final class LumaItemsAPI {

    private static boolean valid = true;


    public boolean isValid() {
        return valid;
    }

    /**
     * Check if an item is a custom item by comparing NBT tags
     * @param itemStack ItemStack to check
     * @param customItemKey NBT key of the custom item
     * @return true if the item is a custom item, false otherwise
     */
    public boolean isCustomItem(ItemStack itemStack, String customItemKey) {
        if (!itemStack.hasItemMeta()) return false;
        ItemMeta meta = itemStack.getItemMeta();

        return meta.getPersistentDataContainer().has(new NamespacedKey(LumaItems.getPlugin(), customItemKey), PersistentDataType.SHORT);
    }

    /**
     * Check if an item is a custom item by comparing NBT tags
     * @param itemStack ItemStack to check
     * @return true if the item is a custom item, false otherwise
     */
    public boolean isCustomItem(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;
        ItemMeta meta = itemStack.getItemMeta();

        for (NamespacedKey key : ItemManager.customItems.keySet()) {
            if (meta.getPersistentDataContainer().has(key, PersistentDataType.SHORT)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the CustomItem object of an ItemStack
     * @param itemStack ItemStack to get the CustomItem of
     * @return CustomItem object if the item is a custom item, null otherwise
     */
    @Nullable
    public CustomItem getCustomItem(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return null;
        ItemMeta meta = itemStack.getItemMeta();

        for (Map.Entry<NamespacedKey, CustomItem> customItem : ItemManager.customItems.entrySet()) {
            if (meta.getPersistentDataContainer().has(customItem.getKey(), PersistentDataType.SHORT)) {
                return customItem.getValue();
            }
        }
        return null;
    }

    /**
     * Get a Custom Item by its key.
     * @param key Key of the Custom Item
     * @return Custom Item if found, null otherwise
     */
    @Nullable
    public CustomItem getCustomItem(String key) {
        return ItemManager.getCustomItem(key);
    }

    /**
     * Get a Custom Item by its display name.
     * Spaces are replaced with underscores ('_'), colors are negated, and the name is case-insensitive.
     * @param name Display name of the Custom Item
     * @return Custom Item if found, null otherwise
     */
    @Nullable
    public ItemStack getItemByName(String name) {
        return ItemManager.getItemByName(name);
    }

    /**
     * @return an immutable list of all physical items
     */
    public List<ItemStack> getAllItems() {
        return ItemManager.getAllItems();
    }
}
