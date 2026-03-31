package org.example.itemmodelenhanced.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.example.itemmodelenhanced.client.render.ItemScaleRegistry;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class ImeCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // /ime <item> set|get|reset
        dispatcher.register(
            literal("ime")
                .then(
                    argument("item", ItemIdArgumentType.itemId())
                    .then(
                        literal("set")
                        .then(
                            argument("scale", FloatArgumentType.floatArg(0.01f, 10.0f))
                            .executes(ImeCommand::executeSet)
                        )
                    )
                    .then(
                        literal("get")
                        .executes(ImeCommand::executeGet)
                    )
                    .then(
                        literal("reset")
                        .executes(ImeCommand::executeReset)
                    )
                )
        );

        dispatcher.register(
            literal("imereset")
            .executes(ctx -> {
                ItemScaleRegistry.clearAll();
                ctx.getSource().sendFeedback(Component.literal("Reset all item scales").withColor(0x55FF55));
                return 1;
            })
        );
    }

    private static int executeSet(com.mojang.brigadier.context.CommandContext<FabricClientCommandSource> ctx) {
        FabricClientCommandSource source = ctx.getSource();
        String itemIdStr = ctx.getArgument("item", String.class);
        float scale = ctx.getArgument("scale", Float.class);

        Item item = parseItemById(itemIdStr);
        if (item == null || item == Items.AIR) {
            source.sendFeedback(Component.literal("Item not found: ").withColor(0xFF5555)
                .append(Component.literal(itemIdStr).withColor(0xFF5555)));
            return 0;
        }

        ItemScaleRegistry.setScale(item, scale);

        MutableComponent message = Component.literal("Item model scale: ").withColor(0xFFFFFF);
        message.append(Component.literal(itemIdStr).withColor(0xFFFF55));
        message.append(Component.literal(" was set to ").withColor(0xFFFFFF));
        message.append(Component.literal(String.format("%.2f", scale)).withColor(0x55FF55));

        source.sendFeedback(message);
        return 1;
    }

    private static int executeGet(com.mojang.brigadier.context.CommandContext<FabricClientCommandSource> ctx) {
        FabricClientCommandSource source = ctx.getSource();
        String itemIdStr = ctx.getArgument("item", String.class);

        Item item = parseItemById(itemIdStr);
        if (item == null || item == Items.AIR) {
            source.sendFeedback(Component.literal("Item not found: ").withColor(0xFF5555)
                .append(Component.literal(itemIdStr).withColor(0xFF5555)));
            return 0;
        }

        float scale = ItemScaleRegistry.getScale(item);
        MutableComponent message = Component.literal("Current scale for ").withColor(0xFFFFFF);
        message.append(Component.literal(itemIdStr).withColor(0xFFFF55));
        message.append(Component.literal(" is ").withColor(0xFFFFFF));
        message.append(Component.literal(String.format("%.2f", scale)).withColor(0x55FF55));

        source.sendFeedback(message);
        return 1;
    }

    private static int executeReset(com.mojang.brigadier.context.CommandContext<FabricClientCommandSource> ctx) {
        FabricClientCommandSource source = ctx.getSource();
        String itemIdStr = ctx.getArgument("item", String.class);

        Item item = parseItemById(itemIdStr);
        if (item == null || item == Items.AIR) {
            source.sendFeedback(Component.literal("Item not found: ").withColor(0xFF5555)
                .append(Component.literal(itemIdStr).withColor(0xFF5555)));
            return 0;
        }

        ItemScaleRegistry.clearScale(item);
        MutableComponent message = Component.literal("Reset scale for ").withColor(0xFFFFFF)
            .append(Component.literal(itemIdStr).withColor(0xFFFF55));
        source.sendFeedback(message);
        return 1;
    }

    private static Item parseItemById(String itemIdStr) {
        String normalizedId = itemIdStr.contains(":") ? itemIdStr : "minecraft:" + itemIdStr;
        ResourceLocation location = ResourceLocation.tryParse(normalizedId);
        if (location == null) {
            return null;
        }
        return BuiltInRegistries.ITEM.getValue(location);
    }
}
