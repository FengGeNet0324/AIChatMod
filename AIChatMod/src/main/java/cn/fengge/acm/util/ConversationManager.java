package cn.fengge.acm.util;

import cn.fengge.acm.config.ModConfig;
import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConversationManager {
    private static final Map<String, List<String>> conversations = new ConcurrentHashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    // 持久化存储路径
    private static final Path HISTORY_PATH = Paths.get("config/aichatmod/history");

    static {
        // 初始化持久化存储
        try {
            Files.createDirectories(HISTORY_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getConversation(String playerId) {
        // 优先从内存读取
        List<String> history = conversations.computeIfAbsent(playerId, k -> new ArrayList<>());
        
        // 如果内存中为空且启用持久化
        if (history.isEmpty() && ModConfig.PERSIST_HISTORY.get()) {
            loadHistoryFromDisk(playerId, history);
        }
        
        return history;
    }

    // 从磁盘加载历史记录
    private static void loadHistoryFromDisk(String playerId, List<String> history) {
        Path historyFile = HISTORY_PATH.resolve(playerId + ".json");
        try {
            if (Files.exists(historyFile)) {
                String content = new String(Files.readAllBytes(historyFile));
                JsonArray array = GSON.fromJson(content, JsonArray.class);
                for (JsonElement element : array) {
                    history.add(element.getAsString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 保存历史记录到磁盘
    public static void saveHistoryToDisk(String playerId, List<String> history) {
        if (!ModConfig.PERSIST_HISTORY.get()) return;
        
        Path historyFile = HISTORY_PATH.resolve(playerId + ".json");
        try {
            Files.createDirectories(historyFile.getParent());
            
            JsonArray array = new JsonArray();
            for (String entry : history) {
                array.add(entry);
            }
            
            try (Writer writer = Files.newBufferedWriter(historyFile)) {
                GSON.toJson(array, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearConversation(String playerId) {
        List<String> history = conversations.get(playerId);
        if (history != null) {
            history.clear();
            // 同时清除磁盘数据
            if (ModConfig.PERSIST_HISTORY.get()) {
                Path historyFile = HISTORY_PATH.resolve(playerId + ".json");
                try {
                    Files.deleteIfExists(historyFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addMessage(String playerId, String message) {
        List<String> history = getConversation(playerId);
        history.add(message);
        
        // 限制历史记录长度
        int maxHistory = ModConfig.MAX_HISTORY.get();
        if (maxHistory > 0 && history.size() > maxHistory) {
            history.remove(0);
        }
        
        // 持久化保存
        if (ModConfig.PERSIST_HISTORY.get()) {
            saveHistoryToDisk(playerId, history);
        }
    }
}