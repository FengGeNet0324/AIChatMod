package cn.fengge.acm.network;

import cn.fengge.acm.AIChatMod;
import cn.fengge.acm.config.ConfigManager;
import cn.fengge.acm.util.ConversationManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class APIRequester {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    
    public static void sendToAI(String query) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            try {
                // 使用配置管理器获取最新配置值
                String apiUrl = ConfigManager.getApiUrl();
                String apiKey = ConfigManager.getApiKey();
                String modelName = ConfigManager.getModelName();
                
                // 构建消息数组，包含强制预置提示词
                JsonArray messages = new JsonArray();
                
                // 修改：根据API类型构建不同请求
                if ("spark".equals(ConfigManager.getApiType())) {
                    buildSparkRequest(messages, query);
                } else if ("openai".equals(ConfigManager.getApiType())) {
                    buildOpenAIRequest(messages, query); // 添加OpenAI支持
                } else {
                    buildDeepSeekRequest(messages, query);
                }
                
                // 新增：添加上下文消息
                if (ConfigManager.isContextEnabled()) {
                    String playerName = minecraft.player.getName().getString();
                    List<String> history = ConversationManager.getRecentHistory(
                        playerName, 
                        ConfigManager.getContextLimit()
                    );
                    
                    for (String msg : history) {
                        JsonObject historyMsg = new JsonObject();
                        if (msg.startsWith("User: ")) {
                            historyMsg.addProperty("role", "user");
                            historyMsg.addProperty("content", msg.substring(6));
                        } else if (msg.startsWith("AI: ")) {
                            historyMsg.addProperty("role", "assistant");
                            historyMsg.addProperty("content", msg.substring(4));
                        }
                        messages.add(historyMsg);
                    }
                }
                
                // 构建请求体
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("model", modelName);
                requestBody.add("messages", messages);
                
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(
                        requestBody.toString()
                    ))
                    .build();

                CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        // 检查HTTP状态码
                        if (response.statusCode() != 200) { //
                            String errorMsg = "Error: HTTP " + response.statusCode(); //
                            AIChatMod.LOGGER.error("API请求失败，状态码: {}, 响应: {}", //
                                response.statusCode(), response.body()); //
                            // 显示错误消息
                            minecraft.execute(() -> { //
                                String displayName = ConfigManager.getChatDisplayName(); //
                                minecraft.gui.getChat().addMessage( //
                                    Component.literal(displayName + " ").withStyle(ChatFormatting.AQUA) //
                                        .append(Component.literal(errorMsg)) //
                                ); //
                            }); //
                            return; //
                        } //
                        
                        // 简化输出：只提取AI回复内容
                        String aiResponse = extractAiResponse(response.body());
                        
                        // 过滤思考过程
                        aiResponse = filterThoughtProcesses(aiResponse);
                        
                        // 使用配置的显示名称
                        String displayName = ConfigManager.getChatDisplayName();
                        handleSuccessfulResponse(aiResponse, displayName);
                    });
            } catch (Exception e) {
                AIChatMod.LOGGER.error("AI请求失败", e);
            }
        });
    }
    
    // 新增：构建讯飞星火API请求
    private static void buildSparkRequest(JsonArray messages, String query) {
        // 添加系统消息（预置提示词）
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", ConfigManager.getPresetPrompt());
        messages.add(systemMessage);
        
        // 添加用户消息
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", query);
        messages.add(userMessage);
        
        // 构建请求参数
        JsonObject parameters = new JsonObject();
        parameters.addProperty("domain", "generalv3.5");
        parameters.addProperty("temperature", 0.5);
        parameters.addProperty("max_tokens", 4096);
        
        // 修改：添加联网搜索和深度思考参数（不再仅限Spark）
        if (ConfigManager.isWebSearchEnabled() && "spark".equals(ConfigManager.getApiType())) {
            parameters.addProperty("web_search", true);
        }
        if (ConfigManager.isDeepThinkingEnabled() && "spark".equals(ConfigManager.getApiType())) {
            parameters.addProperty("deep_thinking", true);
        }
        
        // 构建请求体
        JsonObject requestBody = new JsonObject();
        requestBody.add("messages", messages);
        requestBody.add("parameters", parameters);
    }
    
    // 新增：构建OpenAI API请求
    private static void buildOpenAIRequest(JsonArray messages, String query) {
        // 添加系统消息（预置提示词）
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", ConfigManager.getPresetPrompt());
        messages.add(systemMessage);
        
        // 添加用户消息
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", query);
        messages.add(userMessage);
    }
    
    // 新增：构建DeepSeek API请求（保持原有逻辑）
    private static void buildDeepSeekRequest(JsonArray messages, String query) {
        // 添加系统消息（预置提示词）
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", ConfigManager.getPresetPrompt());
        messages.add(systemMessage);  // [!code focus] 强制添加预置提示词
        
        // 添加用户消息
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", query);
        messages.add(userMessage);
    }
    
    // 修改：从API响应中提取AI回复内容，支持思考过程分离
    private static String extractAiResponse(String responseBody) {
        try {
            // 检查空响应
            if (responseBody == null || responseBody.isEmpty()) {
                AIChatMod.LOGGER.error("API响应为空");
                return "Error：API返回空响应";
            }
            
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            
            // 检查choices数组存在性
            if (!jsonResponse.has("choices") || !jsonResponse.get("choices").isJsonArray()) {
                AIChatMod.LOGGER.error("无效API响应结构: {}", responseBody);
                return "Error：响应缺少choices数组";
            }
            
            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (choices.isEmpty()) {
                return "Error：choices数组为空";
            }
            
            JsonElement firstChoice = choices.get(0);
            
            // 验证firstChoice类型
            if (!firstChoice.isJsonObject()) {
                AIChatMod.LOGGER.error("choices元素不是对象: {}", responseBody);
                return "Error：无效的choices元素类型";
            }
            
            JsonObject choiceObj = firstChoice.getAsJsonObject();
            
            // 检查message字段
            if (!choiceObj.has("message") || !choiceObj.get("message").isJsonObject()) {
                AIChatMod.LOGGER.error("响应缺少message对象: {}", responseBody);
                return "Error：响应缺少message对象";
            }
            
            JsonObject message = choiceObj.getAsJsonObject("message");
            
            // 检查content字段
            if (!message.has("content") || !message.get("content").isJsonPrimitive()) {
                AIChatMod.LOGGER.error("message对象缺少content字段: {}", responseBody);
                return "Error：message对象缺少content字段";
            }
            
            // 根据API类型解析响应
            if ("spark".equals(ConfigManager.getApiType())) {
                return parseSparkResponse(responseBody);
            } else if ("openai".equals(ConfigManager.getApiType())) {
                return parseOpenAIResponse(responseBody); // 添加OpenAI解析
            } else {
                return parseDeepSeekResponse(responseBody);
            }
        } catch (Exception e) {
            AIChatMod.LOGGER.error("解析API响应失败: {}", responseBody, e);
            return "Error：解析响应失败 - " + e.getMessage();
        }
    }
    
    // 新增：解析讯飞星火API响应
    private static String parseSparkResponse(String responseBody) {
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
        
        // 检查choices数组
        if (!jsonResponse.has("choices") || !jsonResponse.get("choices").isJsonArray()) {
            return "Error：响应缺少choices数组";
        }
        
        JsonArray choices = jsonResponse.getAsJsonArray("choices");
        if (choices.isEmpty()) {
            return "Error：choices数组为空";
        }
        
        JsonObject choice = choices.get(0).getAsJsonObject();
        JsonObject message = choice.getAsJsonObject("message");
        String content = message.get("content").getAsString();
        
        // 分离思考过程和正文
        return separateThoughtAndContent(content);
    }
    
    // 新增：分离思考过程和正文
    private static String separateThoughtAndContent(String content) {
        // 查找思考过程和正文的分隔符
        int thoughtEnd = content.indexOf("\n\n正文：");
        if (thoughtEnd == -1) {
            thoughtEnd = content.indexOf("\n\n回答：");
        }
        
        // 如果用户配置显示思考过程，直接返回完整内容
        if (ConfigManager.isShowThoughtProcess() && thoughtEnd != -1) {
            return content;
        }
        
        // 提取正文部分
        if (thoughtEnd != -1) {
            return content.substring(thoughtEnd + 4); // 跳过分隔符
        }
        
        // 没有找到分隔符，返回原始内容
        return content;
    }
    
    // 新增：解析OpenAI API响应
    private static String parseOpenAIResponse(String responseBody) {
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
        
        // 检查choices数组
        if (!jsonResponse.has("choices") || !jsonResponse.get("choices").isJsonArray()) {
            return "Error：响应缺少choices数组";
        }
        
        JsonArray choices = jsonResponse.getAsJsonArray("choices");
        if (choices.isEmpty()) {
            return "Error：choices数组为空";
        }
        
        JsonObject choice = choices.get(0).getAsJsonObject();
        JsonObject message = choice.getAsJsonObject("message");
        String content = message.get("content").getAsString();
        
        return content;
    }
    
    // 新增：解析DeepSeek API响应（保持原有逻辑）
    private static String parseDeepSeekResponse(String responseBody) {
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
        
        // 检查choices数组
        if (!jsonResponse.has("choices") || !jsonResponse.get("choices").isJsonArray()) {
            return "Error：响应缺少choices数组";
        }
        
        JsonArray choices = jsonResponse.getAsJsonArray("choices");
        if (choices.isEmpty()) {
            return "Error：choices数组为空";
        }
        
        JsonObject choice = choices.get(0).getAsJsonObject();
        JsonObject message = choice.getAsJsonObject("message");
        String content = message.get("content").getAsString();
        
        return content;
    }
    
    // 新增方法：过滤思考过程
    private static String filterThoughtProcesses(String content) {
        // 移除思考过程（以"思考："开头的行）
        return content;
    }
    
    // 修改：在成功获取AI回复后，保存AI消息
    private static void handleSuccessfulResponse(String aiResponse, String displayName) {
        Minecraft.getInstance().gui.getChat().addMessage(
            Component.literal(displayName + " ").withStyle(ChatFormatting.AQUA)
                .append(Component.literal(aiResponse)) // [!code ~] 使用简化后的响应
        );
        
        // 新增：保存AI回复到上下文
        if (ConfigManager.isContextEnabled()) {
            String playerName = Minecraft.getInstance().player.getName().getString();
            ConversationManager.addAiMessage(playerName, aiResponse);
        }
    }
}