// 添加预置提示词配置项并完善保存逻辑
public class ConfigScreen extends Screen {
    private EditBox presetPromptField; // 新增字段
    
    @Override
    protected void init() {
        int yPos = this.height / 2 - 100;

        // 添加模型名称标签和输入框
        addLabel("Model Name:", this.width / 2 - 100, yPos - 10);
        modelNameField = new EditBox(this.font, this.width / 2 - 100, yPos, 200, 20, Component.literal(""));
        modelNameField.setValue(ConfigManager.getModelName());
        modelNameField.setMaxLength(64);
        addRenderableWidget(modelNameField);
        yPos += 30;

        // 添加API URL标签和输入框
        addLabel("API URL:", this.width / 2 - 100, yPos - 10);
        // 添加示例URL说明
        addRenderableWidget(new StringWidget(
            this.width / 2 + 110, yPos - 10, 150, 10,
            Component.literal("(示例: https://api.example.com/v1)"),
            this.font
        ));
        apiUrlField = new EditBox(this.font, this.width / 2 - 100, yPos, 200, 20, Component.literal(""));
        apiUrlField.setValue(ConfigManager.getApiUrl());
        apiUrlField.setMaxLength(256);
        addRenderableWidget(apiUrlField);
        yPos += 30;

        // 添加API密钥标签和输入框
        addLabel("API Key:", this.width / 2 - 100, yPos - 10);
        apiKeyField = new EditBox(this.font, this.width / 2 - 100, yPos, 200, 20, Component.literal(""));
        apiKeyField.setValue(ConfigManager.getApiKey());
        apiKeyField.setMaxLength(256);
        addRenderableWidget(apiKeyField);
        yPos += 30;

        // 添加超时时间标签和输入框
        addLabel("Timeout (s):", this.width / 2 - 100, yPos - 10);
        timeoutField = new EditBox(this.font, this.width / 2 - 100, yPos, 200, 20, Component.literal(""));
        timeoutField.setValue(String.valueOf(ConfigManager.getTimeout()));
        timeoutField.setMaxLength(3);
        addRenderableWidget(timeoutField);
        yPos += 30;

        // 添加聊天显示名称标签和输入框
        addLabel("Chat Display Name:", this.width / 2 - 100, yPos - 10);
        chatDisplayNameField = new EditBox(this.font, this.width / 2 - 100, yPos, 200, 20, Component.literal(""));
        chatDisplayNameField.setValue(ConfigManager.getChatDisplayName());
        chatDisplayNameField.setMaxLength(32);
        addRenderableWidget(chatDisplayNameField);
        yPos += 30;

        // 新增预置提示词输入框
        addLabel("Preset Prompt:", this.width / 2 - 100, yPos - 10);
        presetPromptField = new EditBox(this.font, this.width / 2 - 100, yPos, 200, 20, Component.literal(""));
        presetPromptField.setValue(ConfigManager.getPresetPrompt());
        presetPromptField.setMaxLength(256);
        addRenderableWidget(presetPromptField);
        yPos += 30;

        // 添加保存按钮
        addRenderableWidget(Button.builder(
            Component.literal("Save"),
            button -> saveConfig())
            .pos(this.width / 2 - 100, yPos)
            .size(200, 20)
            .tooltip(Tooltip.create(Component.literal("保存配置")))
            .build()
        );

        // 添加清除会话按钮
        addRenderableWidget(Button.builder(
            Component.literal("Clear History"), 
            button -> {
                ConversationManager.clearConversation(
                    Minecraft.getInstance().player.getName().getString()
                );
                Minecraft.getInstance().player.displayClientMessage(
                    Component.literal("会话历史已清除！"),
                    false
                );
            })
            .pos(this.width / 2 - 100, yPos + 40)
            .size(200, 20)
            .tooltip(Tooltip.create(Component.literal("重置对话历史")))
            .build()
        );
    }
    
    private void saveConfig() {
        String modelName = modelNameField.getValue().trim();
        String apiUrl = apiUrlField.getValue().trim();
        String apiKey = apiKeyField.getValue().trim();
        String timeoutStr = timeoutField.getValue().trim();
        String chatDisplayName = chatDisplayNameField.getValue().trim();
        String presetPrompt = presetPromptField.getValue().trim(); //
        
        // 验证完整性
        if (apiUrl.isEmpty() || apiKey.isEmpty() || timeoutStr.isEmpty() || 
            chatDisplayName.isEmpty() || presetPrompt.isEmpty()) { //
            Minecraft.getInstance().player.displayClientMessage(
                Component.literal("所有字段都必须填写！").withStyle(ChatFormatting.RED),
                false
            );
            return;
        }
        
        // 传递新参数
        ConfigManager.saveConfig(modelName, apiUrl, apiKey, timeout, chatDisplayName, presetPrompt);
    }
}