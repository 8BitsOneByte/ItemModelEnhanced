package org.example.itemmodelenhanced.client.render;

import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global item scale registry that manages model scale multipliers for items.
 */
public class ItemScaleRegistry {
    private static final Map<Item, Float> SCALE_MAP = new ConcurrentHashMap<>();

    /**
     * Sets the scale multiplier for the given item.
     */
    public static void setScale(Item item, float scale) {
        SCALE_MAP.put(item, scale);
    }

    /**
     * Returns the scale multiplier for the given item, defaulting to 1.0 if not set.
     */
    public static float getScale(Item item) {
        return SCALE_MAP.getOrDefault(item, 1.0f);
    }

    /**
     * Clears the scale setting for the given item.
     */
    public static void clearScale(Item item) {
        SCALE_MAP.remove(item);
    }

    /**
     * Clears all item scale settings.
     */
    public static void clearAll() {
        SCALE_MAP.clear();
    }

    /**
     * Returns true if the given item has a custom scale set.
     */
    public static boolean hasCustomScale(Item item) {
        return SCALE_MAP.containsKey(item);
    }
}
