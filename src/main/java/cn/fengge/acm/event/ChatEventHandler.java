package cn.fengge.acm.event;

import cn.fengge.acm.AIChatMod;
import cn.fengge.acm.config.ConfigManager;
import cn.fengge.acm.network.APIRequester;
import cn.fengge.acm.util.ConversationManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(value = Dist.CLIENT)
public class ChatEventHandler {
    // 添加日志记录器
    private static final Logger LOGGER = LogManager.getLogger(AIChatMod.MODID);

    @SubscribeEvent
    public static void onChatMessage(ClientChatEvent event) {
        // 检查AI聊天开关状态
        if (!AIChatMod.isAiChatEnabled()) {
            LOGGER.info("大模型聊天未开启，跳过信息过滤");
            return;
        }
        
        String message = event.getMessage();
        boolean shouldProcess = false;
        String query = "";
        
        // 根据配置决定处理方式
        if (ConfigManager.isPrefixEnabled()) {
            // 前缀模式：只处理以#开头的消息
            if (message.startsWith("#")) {
                shouldProcess = true;
                query = message.substring(1).trim();
            }
        } else {
            // 无前缀模式：处理所有非命令消息
            if (!message.startsWith("/")) {
                shouldProcess = true;
                query = message.trim();
            }
        }
        
        if (shouldProcess) {
            event.setCanceled(true);
            LOGGER.info("将玩家消息发送到API: {}", query);
            
            // 如果启用了上下文，保存用户消息
            if (ConfigManager.isContextEnabled()) {
                String playerName = Minecraft.getInstance().player.getName().getString();
                ConversationManager.addUserMessage(playerName, query);
            }
            
            APIRequester.sendToAI(query);
        }
    }
}