package org.exmple.itemmodelenhanced.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import org.exmple.itemmodelenhanced.client.render.ItemScaleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ItemScaleConfigStore {
    private static final Logger LOGGER = LoggerFactory.getLogger("ItemModelEnhanced/Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir()
        .resolve("itemmodelenhanced")
        .resolve("scales.json");
    private static final Path LEGACY_CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir()
        .resolve("itemmodelenhanced-scales.json");

    private ItemScaleConfigStore() {
    }

    public static Path getConfigPath() {
        return CONFIG_PATH;
    }

    public static void load() {
        Path loadPath = Files.exists(CONFIG_PATH) ? CONFIG_PATH : LEGACY_CONFIG_PATH;
        if (!Files.exists(loadPath)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(loadPath)) {
            JsonElement root = JsonParser.parseReader(reader);
            if (!root.isJsonObject()) {
                LOGGER.warn("Scale config is not a JSON object: {}", loadPath);
                return;
            }

            JsonObject json = root.getAsJsonObject();
            Map<String, Float> loaded = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                JsonElement value = entry.getValue();
                if (value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
                    loaded.put(entry.getKey(), value.getAsFloat());
                }
            }

            ItemScaleRegistry.importIdScaleMap(loaded);
            LOGGER.info("Loaded {} item scale entries from {}", loaded.size(), loadPath);

            // Migrate old flat file to the mod-specific directory on first successful load.
            if (loadPath.equals(LEGACY_CONFIG_PATH) && !Files.exists(CONFIG_PATH)) {
                save();
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to load scale config from {}", loadPath, e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Map<String, Float> data = ItemScaleRegistry.exportIdScaleMap();
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(data, writer);
            }
            LOGGER.info("Saved {} item scale entries to {}", data.size(), CONFIG_PATH);
        } catch (IOException e) {
            LOGGER.warn("Failed to save scale config to {}", CONFIG_PATH, e);
        }
    }
}

