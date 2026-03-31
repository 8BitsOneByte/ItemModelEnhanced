package org.exmple.itemmodelenhanced.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.exmple.itemmodelenhanced.client.command.ImeCommand;

public class ItemmodelenhancedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> 
            ImeCommand.register(dispatcher)
        );
    }
}
