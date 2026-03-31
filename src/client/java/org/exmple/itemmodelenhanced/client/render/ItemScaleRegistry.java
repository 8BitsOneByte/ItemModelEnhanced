package org.exmple.itemmodelenhanced.client.render;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.exmple.itemmodelenhanced.client.config.ItemScaleConfigStore;
import java.util.HashMap;
import java.util.Map;


public class ItemScaleRegistry {
    private static final Map<Item, Float> SCALE_MAP = new HashMap<>();
    private static boolean loaded;

    private static void ensureLoaded() {
        if (!loaded) {
            loaded = true;
            ItemScaleConfigStore.load();
        }
    }

    public static void setScale(Item item, float scale) {
        ensureLoaded();
        SCALE_MAP.put(item, scale);
        ItemScaleConfigStore.save();
    }

    public static float getScale(Item item) {
        ensureLoaded();
        return SCALE_MAP.getOrDefault(item, 1.0f);
    }

    public static void clearScale(Item item) {
        ensureLoaded();
        SCALE_MAP.remove(item);
        ItemScaleConfigStore.save();
    }

    public static void clearAll() {
        ensureLoaded();
        SCALE_MAP.clear();
        ItemScaleConfigStore.save();
    }

    public static boolean hasCustomScale(Item item) {
        ensureLoaded();
        return SCALE_MAP.containsKey(item);
    }

    public static Map<String, Float> exportIdScaleMap() {
        ensureLoaded();
        Map<String, Float> out = new HashMap<>();
        for (Map.Entry<Item, Float> entry : SCALE_MAP.entrySet()) {
            out.put(BuiltInRegistries.ITEM.getKey(entry.getKey()).toString(), entry.getValue());
        }
        return out;
    }

    public static void importIdScaleMap(Map<String, Float> data) {
        SCALE_MAP.clear();
        for (Map.Entry<String, Float> entry : data.entrySet()) {
            Item item = findItemById(entry.getKey());
            Float scale = entry.getValue();
            if (item != null && item != Items.AIR && scale != null && scale > 0.0f) {
                SCALE_MAP.put(item, scale);
            }
        }
        loaded = true;
    }

    public static Item findItemById(String itemIdStr) {
        String normalizedId = itemIdStr.contains(":") ? itemIdStr : "minecraft:" + itemIdStr;
        for (Item item : BuiltInRegistries.ITEM) {
            var key = BuiltInRegistries.ITEM.getKey(item);
            if (key.toString().equals(normalizedId)) {
                return item;
            }
        }
        return null;
    }
}

