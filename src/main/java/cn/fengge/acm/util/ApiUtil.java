package cn.fengge.acm.util;

import cn.fengge.acm.config.ConfigManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ApiUtil {
    // 添加静态HttpClient常量
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static String sendChatRequest(List<String> conversationHistory) {
        try {
            // 使用配置管理器获取最新配置值
            String apiUrl = ConfigManager.getApiUrl();
            String apiKey = ConfigManager.getApiKey();
            String modelName = ConfigManager.getModelName();
            int timeout = ConfigManager.getTimeout();

            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", ConfigManager.getPresetPrompt());
            
            JsonArray messages = new JsonArray();
            messages.add(systemMessage);  // [!code focus] 强制添加预置提示词
            
            for (String message : conversationHistory) {
                JsonObject jsonMessage = new JsonObject();
                if (message.startsWith("Player: ")) {
                    jsonMessage.addProperty("role", "user");
                    jsonMessage.addProperty("content", message.substring(8));
                } else if (message.startsWith("AI: ")) {
                    jsonMessage.addProperty("role", "assistant");
                    jsonMessage.addProperty("content", message.substring(4));
                }
                messages.add(jsonMessage);
            }
            
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", modelName);
            requestBody.addProperty("max_tokens", 2000);
            requestBody.add("messages", messages);
            
            // 发送HTTP请求
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .timeout(Duration.ofSeconds(timeout))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
            
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                // 增强解析逻辑
                if (jsonResponse.has("choices") && jsonResponse.get("choices").isJsonArray()) {
                    JsonArray choices = jsonResponse.getAsJsonArray("choices");
                    if (!choices.isEmpty()) {
                        JsonElement firstChoice = choices.get(0);
                        if (firstChoice.isJsonObject() && firstChoice.getAsJsonObject().has("message")) {
                            JsonObject message = firstChoice.getAsJsonObject().getAsJsonObject("message");
                            if (message.has("content") && message.get("content").isJsonPrimitive()) {
                                String content = message.get("content").getAsString();
                                // 过滤思考过程
                                content = filterThoughtProcesses(content);
                                return content;
                            }
                        }
                    }
                }
                return "Error: API响应结构无效";
            }
            return "Error: HTTP状态码 " + response.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private static String filterThoughtProcesses(String content) {
        // 移除思考过程（以"思考："开头的行）
        return content.replaceAll("(?m)^思考：.*\\n?", "");
    }
}