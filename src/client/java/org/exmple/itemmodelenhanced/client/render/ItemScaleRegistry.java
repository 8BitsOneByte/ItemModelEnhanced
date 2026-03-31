package org.exmple.itemmodelenhanced.client.render;

import net.minecraft.world.item.Item;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局物品缩放注册表，管理物品的模型缩放倍率
 */
public class ItemScaleRegistry {
    private static final Map<Item, Float> SCALE_MAP = new HashMap<>();

    /**
     * 设置物品缩放倍率
     */
    public static void setScale(Item item, float scale) {
        SCALE_MAP.put(item, scale);
    }

    /**
     * 获取物品缩放倍率，如果未设置则返回 1.0
     */
    public static float getScale(Item item) {
        return SCALE_MAP.getOrDefault(item, 1.0f);
    }

    /**
     * 清除单个物品的缩放设置
     */
    public static void clearScale(Item item) {
        SCALE_MAP.remove(item);
    }

    /**
     * 清除所有缩放设置
     */
    public static void clearAll() {
        SCALE_MAP.clear();
    }

    /**
     * 检查物品是否有自定义缩放
     */
    public static boolean hasCustomScale(Item item) {
        return SCALE_MAP.containsKey(item);
    }
}

