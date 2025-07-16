            // 强制使用预置提示词 [!code ~]
            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", ConfigManager.getPresetPrompt()); // [!code ~] 确保使用配置值
            
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", modelName); // [!code ~] 使用配置的模型名称
            requestBody.addProperty("max_tokens", 200);
            
            JsonArray messages = new JsonArray();
            messages.add(systemMessage); // [!code ~] 强制将预置提示词作为第一条消息