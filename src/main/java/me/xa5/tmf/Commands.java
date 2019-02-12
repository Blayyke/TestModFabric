package me.xa5.tmf;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.command.arguments.BlockArgumentType;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SetBlockCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Commands {
    private LiteralArgumentBuilder<ServerCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal(TestModFabric.MOD_ID);

        subCommand(builder, "help");
        subCommand(builder, "pos1");
        subCommand(builder, "pos2");
        subCommand(builder, "set");
        subCommand(builder, "hset");

        builder
                .then(literal("set"))
                .then(ServerCommandManager.argument("block", BlockArgumentType.create()))
                .executes(context -> {
                    try {
                        executeCommand(context.getSource().getPlayer(), "set");
                    } catch (Exception e) {
                        TestModFabric.getInstance().getLogger().warn("Failed command " + "set", e);
                    }
                    return 0;
                });

        dispatcher.register(builder);
    }

    private void subCommand(LiteralArgumentBuilder<ServerCommandSource> builder, String subCommandName) {
        builder.then(literal(subCommandName)
                .executes(context -> {
                    try {
                        executeCommand(context.getSource().getPlayer(), subCommandName);
                    } catch (Exception e) {
                        TestModFabric.getInstance().getLogger().warn("Failed command " + subCommandName, e);
                    }
                    return 0;
                }));
    }

    private void executeCommand(ServerPlayerEntity player, String commandName, String... args) {
        PlayerSelectionInfo selectionInfo = TestModFabric.getInstance().getSelectionInfo(player);

        switch (commandName) {
            case "pos1": {
                if (PlayerSelectionInfo.isInvalidPos(player.getPosVector(), player.world)) {
                    PlayerSelectionInfo.message(player, "pos.invalid");
                    return;
                }
                selectionInfo.setPos1(player.getPosVector());
                PlayerSelectionInfo.message(player, "pos1.set");
                break;
            }
            case "pos2": {
                if (PlayerSelectionInfo.isInvalidPos(player.getPosVector(), player.world)) {
                    PlayerSelectionInfo.message(player, "pos.invalid");
                    return;
                }
                selectionInfo.setPos2(player.getPosVector());
                PlayerSelectionInfo.message(player, "pos2.set");
                break;
            }
            case "set": {
                String blockId = args[0];
                Block block = Registry.BLOCK.get(new Identifier(blockId));
                PlayerSelectionInfo.setArea(selectionInfo, player.world, block, false);
                PlayerSelectionInfo.message(player, "Pos set to " + I18n.translate(block.getItem().getTranslationKey()));
                break;
            }
            case "hset": {
                String blockId = args[0];
                Block block = Registry.BLOCK.get(new Identifier(blockId));
                PlayerSelectionInfo.setArea(selectionInfo, player.world, block, true);
                break;
            }
        }
    }
}