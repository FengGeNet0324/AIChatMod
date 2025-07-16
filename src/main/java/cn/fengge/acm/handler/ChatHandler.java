package cn.fengge.acm.handler;

import cn.fengge.acm.AIChatMod;
import cn.fengge.acm.util.ApiUtil;
import cn.fengge.acm.util.ConversationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ChatHandler {
    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        if (!AIChatMod.isAiChatEnabled()) return;
        
        String message = event.getMessage();
        if (message.startsWith("/")) return;
        
        event.setCanceled(true);
        
        String playerName = Minecraft.getInstance().player.getName().getString();
        List<String> history = ConversationManager.getConversation(playerName);
        history.add("Player: " + message);
        
        new Thread(() -> {
            String response = ApiUtil.sendChatRequest(history);
            history.add("AI: " + response);

            Minecraft.getInstance().execute(() -> {
                Minecraft.getInstance().player.displayClientMessage(
                    Component.literal(response),
                    false
                );
            });
        }).start();
    }
}