package cn.fengge.acm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fengge.acm.config.ConfigManager;

public class ConversationManager {
    private static final Map<String, List<String>> conversations = new HashMap<>();
    private static final int MAX_HISTORY = 10; // 保存最近10条对话
    
    public static List<String> getConversation(String playerId) {
        return conversations.computeIfAbsent(playerId, k -> new ArrayList<>());
    }
    
    public static void clearConversation(String playerId) {
        conversations.remove(playerId);
    }
    
    // 修改：添加带最大数量限制的消息添加方法
    public static void addMessage(String playerId, String message, boolean isUser) {
        List<String> history = getConversation(playerId);
        String prefix = isUser ? "User: " : "AI: ";
        history.add(prefix + message);
        
        // 动态获取上下文限制数量
        int maxHistory = ConfigManager.isContextEnabled() ? 
                         ConfigManager.getContextLimit() * 2 : // 用户和AI各算一条
                         0;
        
        // 限制历史记录长度
        while (history.size() > maxHistory && maxHistory > 0) {
            history.remove(0);
        }
    }
    
    // 新增：添加用户消息
    public static void addUserMessage(String playerId, String message) {
        addMessage(playerId, message, true);
    }
    
    // 新增：添加AI消息
    public static void addAiMessage(String playerId, String message) {
        addMessage(playerId, message, false);
    }
    
    // 新增：获取最近的历史记录
    public static List<String> getRecentHistory(String playerId, int limit) {
        List<String> fullHistory = getConversation(playerId);
        int startIndex = Math.max(0, fullHistory.size() - limit * 2); // 用户和AI各算一条
        return new ArrayList<>(fullHistory.subList(startIndex, fullHistory.size()));
    }
}