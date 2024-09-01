package dev.jsinco.lumaitems.manager;

import com.google.common.reflect.ClassPath;
import dev.jsinco.lumaitems.LumaItems;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemManager {

    private final LumaItems plugin;

    /**
     * Map of all LumaItems Custom Items
     * Key: Custom Item NBT Key
     * Value: Custom Item Class
     */
    public final static Map<NamespacedKey, CustomItem> customItems = new HashMap<>();


    /**
     * List of all packages to search for Custom Items
     */
    public final static List<String> packages = List.of(
            "dev.jsinco.lumaitems.items.weapons",
            "dev.jsinco.lumaitems.items.tools",
            "dev.jsinco.lumaitems.items.misc",
            "dev.jsinco.lumaitems.items.armor",
            "dev.jsinco.lumaitems.items.magical",
            "dev.jsinco.lumaitems.items.astral",
            "dev.jsinco.lumaitems.items.astral.sets",
            "dev.jsinco.lumaitems.items.test"
    );

    public ItemManager(LumaItems plugin) {
        this.plugin = plugin;
    }

    public void clearAllItems() {
        customItems.clear();
    }


    /**
     * Registers all Custom Items in the packages list
     */
    public void registerItems() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Class<?>> classes = new ArrayList<>();

        for (String packagee : packages) {
            classes.addAll(findClasses(packagee));
        }

        for (Class<?> clazz : classes) {
            try {
                if (CustomItem.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                    CustomItem item = (CustomItem) clazz.getDeclaredConstructor().newInstance();
                    customItems.put(new NamespacedKey(plugin, item.createItem().component1()), item);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        plugin.getLogger().info("Registered " + customItems.size() + " classes through reflection");
    }

    /**
     * Finds all classes in a package
     * @param packageName Package to search
     * @return Set of classes in the package
     * Credit: <a href="https://www.spigotmc.org/threads/register-all-listeners-in-package.399219/">...</a>
     */
    private Set<Class<?>> findClasses(String packageName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
        getFileMethod.setAccessible(true);
        File file = (File) getFileMethod.invoke(plugin);
        URLClassLoader classLoader = new URLClassLoader(
                new URL[] { file.toURI().toURL() },
                this.getClass().getClassLoader()
        );
        return ClassPath.from(classLoader)
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName()
                        .equalsIgnoreCase(packageName))
                .map(clazz -> clazz.load())
                .collect(Collectors.toSet());
    }
}
