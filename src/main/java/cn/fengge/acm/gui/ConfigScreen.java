package cn.fengge.acm.gui;

import cn.fengge.acm.AIChatMod;
import cn.fengge.acm.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiGraphics;
import java.util.Arrays;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private EditBox apiUrlField;
    private EditBox apiKeyField;
    private EditBox timeoutField;
    // 新增：聊天栏显示名称输入框
    private EditBox chatDisplayNameField;
    private EditBox modelNameField;
    private Checkbox prefixCheckbox; //
    private Checkbox contextCheckbox; // 新增：上下文开关
    private EditBox contextLimitField; // 新增：上下文消息数量
    
    // 修改：将 DropdownWidget 替换为 CycleButton
    private CycleButton<String> apiTypeButton; // 修改：API类型选择按钮
    
    // 新增：联网搜索复选框
    private Checkbox webSearchCheckbox;
    // 新增：深度思考复选框
    private Checkbox deepThinkingCheckbox;
    // 新增：显示思考过程复选框
    private Checkbox showThoughtCheckbox;
    
    // 新增：背景图片资源位置
    private static final ResourceLocation BACKGROUND = new ResourceLocation("aichatmod", "textures/gui/background.png"); //
    
    public ConfigScreen(Screen parent) {
        super(Component.literal("AI Chat Config"));
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        super.init();
        int yPos = 100;

        addLabel("(哈气) 警告：GUI还不可以用喵，去配置文件改喵(aichatmod-client.toml)", this.width / 2 - 125, yPos - 60);
        // 新增模型名称标签和输入框
        addLabel("模型名称:(例如：deepseek-chat)", this.width / 2 - 125, yPos - 10);
        modelNameField = new EditBox(this.font, this.width / 2 - 125, yPos, 250, 20, Component.literal("")); // 宽度改为250
        modelNameField.setValue(ConfigManager.getModelName());
        modelNameField.setMaxLength(50);
        addRenderableWidget(modelNameField);
        yPos += 30;
        
        // 添加API URL标签和输入框
        addLabel("API 请求地址:(例如：https://api.deepseek.com/v1/chat/completions)", this.width / 2 - 125, yPos - 10);
        apiUrlField = new EditBox(this.font, this.width / 2 - 125, yPos, 250, 20, Component.literal("")); // 宽度改为250
        apiUrlField.setValue(ConfigManager.getApiUrl());
        apiUrlField.setMaxLength(200);
        addRenderableWidget(apiUrlField);
        yPos += 30;
        
        // 添加API Key标签和输入框
        addLabel("API Key:", this.width / 2 - 125, yPos - 10);
        apiKeyField = new EditBox(this.font, this.width / 2 - 125, yPos, 250, 20, Component.literal("")); // 宽度改为250
        apiKeyField.setValue(ConfigManager.getApiKey());
        apiKeyField.setMaxLength(100);
        addRenderableWidget(apiKeyField);
        yPos += 30;
        
        // 添加超时标签和输入框
        addLabel("超时时间 (s):", this.width / 2 - 125, yPos - 10);
        timeoutField = new EditBox(this.font, this.width / 2 - 125, yPos, 250, 20, Component.literal("")); // 宽度改为250
        timeoutField.setValue(String.valueOf(ConfigManager.getTimeout()));
        timeoutField.setMaxLength(3);
        addRenderableWidget(timeoutField);
        yPos += 30;
        
        // 新增：聊天栏显示名称标签和输入框
        addLabel("聊天中显示名称:", this.width / 2 - 125, yPos - 10);
        chatDisplayNameField = new EditBox(this.font, this.width / 2 - 125, yPos, 250, 20, Component.literal("")); // 宽度改为250
        chatDisplayNameField.setValue(ConfigManager.getChatDisplayName());
        chatDisplayNameField.setMaxLength(20);
        addRenderableWidget(chatDisplayNameField);
        yPos += 30;
        
        // 新增：是否启用前缀检测复选框
        prefixCheckbox = new Checkbox( //
            this.width / 2 - 125, yPos, 250, 20, //
            Component.literal("启用消息前缀检测（默认开启）"), //
            ConfigManager.isPrefixEnabled() // 初始值
        ); //
        addRenderableWidget(prefixCheckbox); //
        yPos += 30; //
        
        // 新增：上下文功能配置
        contextCheckbox = new Checkbox(
            this.width / 2 - 125, yPos, 250, 20,
            Component.literal("启用上下文功能"),
            ConfigManager.isContextEnabled()
        );
        addRenderableWidget(contextCheckbox);
        yPos += 30;
        
        addLabel("上下文消息数量:", this.width / 2 - 125, yPos - 10);
        contextLimitField = new EditBox(this.font, this.width / 2 - 125, yPos, 250, 20, Component.literal(""));
        contextLimitField.setValue(String.valueOf(ConfigManager.getContextLimit()));
        contextLimitField.setMaxLength(2);
        addRenderableWidget(contextLimitField);
        yPos += 30;
        
        // 修改API类型选择按钮构建方式
        addLabel("API类型:", this.width / 2 - 125, yPos - 10);
        apiTypeButton = CycleButton.<String>builder(apiType -> Component.literal(apiType))
            .withValues(Arrays.asList("deepseek", "spark", "openai(兼容)"))
            .withInitialValue(ConfigManager.getApiType())
            .create(this.width / 2 - 125, yPos, 250, 20, Component.literal("API类型"));
        
        addRenderableWidget(apiTypeButton);
        yPos += 30;
        
        // 新增：联网搜索开关
        // 修改：联网搜索开关描述
        webSearchCheckbox = new Checkbox(
            this.width / 2 - 125, yPos, 250, 20,
            Component.literal("启用联网搜索（支持此功能的模型）"),
            ConfigManager.isWebSearchEnabled()
        );
        addRenderableWidget(webSearchCheckbox);
        yPos += 30;
        
        // 新增：深度思考开关
        // 修改：深度思考开关描述
        deepThinkingCheckbox = new Checkbox(
            this.width / 2 - 125, yPos, 250, 20,
            Component.literal("启用深度思考（支持此功能的模型）"),
            ConfigManager.isDeepThinkingEnabled()
        );
        addRenderableWidget(deepThinkingCheckbox);
        yPos += 30;
        
        // 新增：显示思考过程开关
        showThoughtCheckbox = new Checkbox(
            this.width / 2 - 125, yPos, 250, 20,
            Component.literal("显示思考过程"),
            ConfigManager.isShowThoughtProcess()
        );
        addRenderableWidget(showThoughtCheckbox);
        yPos += 30;
        
        // 保存按钮
        addRenderableWidget(Button.builder(
            Component.literal("保存配置"),
            button -> {
                saveConfig();
                // 显示保存成功提示
                Minecraft.getInstance().player.displayClientMessage(
                    Component.literal("配置已经保存!"),
                    false
                );
                this.onClose();
            })
            .pos(this.width / 2 - 125, yPos) // 位置调整
            .size(250, 20) // 宽度改为250
            .tooltip(Tooltip.create(Component.empty()))
            .build()
        );
    }
    
    // 修改：使用标准Label组件
    private void addLabel(String text, int x, int y) {
        int width = this.font.width(text);
        addRenderableWidget(new StringWidget(
                x, y, width, 10,
                Component.literal(text),
                this.font
        ));
        
        // 添加预置提示词说明文本
        if (text.equals("聊天中显示名称:")) {
            addRenderableWidget(new StringWidget(
                this.width / 2 + 145, y, 150, 10, // 位置调整
                Component.literal("(预置提示词在配置文件中修改)"),
                this.font
            ));
        }
    }


    private void saveConfig() {
        String modelName = modelNameField.getValue().trim();
        String apiUrl = apiUrlField.getValue().trim();
        String apiKey = apiKeyField.getValue().trim();
        String timeoutStr = timeoutField.getValue().trim();
        String chatDisplayName = chatDisplayNameField.getValue().trim();
        boolean enablePrefix = prefixCheckbox.selected(); //
        boolean enableContext = contextCheckbox.selected();
        String contextLimitStr = contextLimitField.getValue().trim();
        
        // 新增：获取新增配置值
        String apiType = apiTypeButton.getValue();
        boolean enableWebSearch = webSearchCheckbox.selected();
        boolean enableDeepThinking = deepThinkingCheckbox.selected();
        boolean showThoughtProcess = showThoughtCheckbox.selected();
        
        // 添加输入安全清理
        chatDisplayName = chatDisplayName.replaceAll("[\\x00-\\x1F\\x7F]", "");
    
        // 验证完整性
        if (apiUrl.isEmpty() || apiKey.isEmpty() || timeoutStr.isEmpty() || chatDisplayName.isEmpty()) {
            Minecraft.getInstance().player.displayClientMessage(
                Component.literal("所有字段都必须填写！").withStyle(ChatFormatting.RED),
                false
            );
            return;
        }
    
        // 添加更详细的错误提示
        try {
            int timeout = Integer.parseInt(timeoutStr);
            if (timeout < 5 || timeout > 120) {
                Minecraft.getInstance().player.displayClientMessage(
                    Component.literal("超时时间必须在5-120秒之间").withStyle(ChatFormatting.YELLOW),
                    false
                );
                return;
            }
            
            int contextLimit = Integer.parseInt(contextLimitStr);
            
            // 修改：添加enablePrefix和contextLimit参数
            ConfigManager.saveConfig(
                modelName, apiUrl, apiKey, timeout, 
                chatDisplayName, enablePrefix, 
                enableContext, contextLimit,
                apiType, enableWebSearch, enableDeepThinking, showThoughtProcess
            );
            
        } catch (NumberFormatException e) {
            Minecraft.getInstance().player.displayClientMessage(
                Component.literal("超时时间必须为数字").withStyle(ChatFormatting.RED),
                false
            );
            AIChatMod.LOGGER.error("无效的超时值: {}", timeoutStr, e);
        } catch (Exception e) {
            Minecraft.getInstance().player.displayClientMessage(
                Component.literal("配置保存失败: " + e.getMessage()).withStyle(ChatFormatting.BOLD, ChatFormatting.RED),
                false
            );
            AIChatMod.LOGGER.error("配置保存失败", e);
        }
    }
    
    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
    
    // 新增：重写render方法绘制背景图片
    @Override //
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) { //
        // 绘制背景图片
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.7F); // 设置透明度为50%
        guiGraphics.blit(BACKGROUND, 0, 0, 0, 0, this.width, this.height, this.width, this.height); //
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); // 重置颜色
        
        // 继续绘制其他UI元素
        super.render(guiGraphics, mouseX, mouseY, partialTicks); //
    } //
}