package org.exmple.itemmodelenhanced.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.exmple.itemmodelenhanced.client.command.ImeCommand;
import org.exmple.itemmodelenhanced.client.config.ItemScaleConfigStore;

public class ItemmodelenhancedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItemScaleConfigStore.load();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> 
            ImeCommand.register(dispatcher)
        );
    }
}
