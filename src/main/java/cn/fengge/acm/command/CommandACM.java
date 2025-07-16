package cn.fengge.acm.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import cn.fengge.acm.config.ConfigManager;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import cn.fengge.acm.AIChatMod;

public class CommandACM {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("acm")
            // 帮助命令
            .then(Commands.literal("help")
                .executes(context -> {
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("===== AIChatMod 命令帮助 =====").withStyle(ChatFormatting.AQUA),
                        false
                    );
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("/acm enable/disable - 启用/禁用 AI聊天功能"),
                        false
                    );
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("/acm context enable - 启用上下文功能"),
                        false
                    );
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("/acm context disable - 禁用上下文功能"),
                        false
                    );
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("/acm context set <数量> - 设置上下文消息数量 (1-20)"),
                        false
                    );
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("/acm prefix enable - 启用消息前缀检测（只处理以#开头的消息）"),
                        false
                    );
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("/acm prefix disable - 禁用消息前缀检测（处理所有非命令消息）"),
                        false
                    );
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("/acm help - 显示此帮助信息"),
                        false
                    );
                    return 1;
                })
            )
            // 启用AI聊天功能
            .then(Commands.literal("enable")
                .executes(context -> {
                    AIChatMod.enableAIChat();
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("AI聊天功能：开启"),
                        false
                    );
                    return 1;
                })
            )
            // 禁用AI聊天功能
            .then(Commands.literal("disable")
                .executes(context -> {
                    AIChatMod.disableAIChat();
                    context.getSource().getPlayer().displayClientMessage(
                        Component.literal("AI聊天功能：关闭"),
                        false
                    );
                    return 1;
                })
            )
            .then(Commands.literal("context")
                .then(Commands.literal("enable")
                    .executes(context -> {
                        ConfigManager.setContextEnabled(true);
                        context.getSource().getPlayer().displayClientMessage(
                            Component.literal("上下文功能：开启"),
                            false
                        );
                        return 1;
                    })
                )
                .then(Commands.literal("disable")
                    .executes(context -> {
                        ConfigManager.setContextEnabled(false);
                        context.getSource().getPlayer().displayClientMessage(
                            Component.literal("上下文功能：关闭"),
                            false
                        );
                        return 1;
                    })
                )
                .then(Commands.literal("set")
                    .then(Commands.argument("limit", IntegerArgumentType.integer(1, 20))
                    .executes(context -> {
                        int limit = IntegerArgumentType.getInteger(context, "limit");
                        ConfigManager.setContextLimit(limit);
                        context.getSource().getPlayer().displayClientMessage(
                            Component.literal("上下文消息数量设置为: " + limit),
                            false
                        );
                        return 1;
                    })
                )
            )
            .then(Commands.literal("prefix")
                .then(Commands.literal("enable")
                    .executes(context -> {
                        ConfigManager.setEnablePrefix(true);
                        context.getSource().getPlayer().displayClientMessage(
                            Component.literal("消息前缀检测：开启"),
                            false
                        );
                        return 1;
                    })
                )
                .then(Commands.literal("disable")
                    .executes(context -> {
                        ConfigManager.setEnablePrefix(false);
                        context.getSource().getPlayer().displayClientMessage(
                            Component.literal("消息前缀检测：关闭"),
                            false
                        );
                        return 1;
                    })
                )
            )
        ));
    }
}