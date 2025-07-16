package cn.fengge.acm.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> API_URL;
    public static final ForgeConfigSpec.ConfigValue<String> API_KEY;
    public static final ForgeConfigSpec.ConfigValue<Integer> TIMEOUT;
    public static final ForgeConfigSpec.ConfigValue<String> PRESET_PROMPT;
    public static final ForgeConfigSpec.ConfigValue<String> CHAT_DISPLAY_NAME;
    public static final ForgeConfigSpec.ConfigValue<String> MODEL_NAME;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_PREFIX; //
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_CONTEXT; // 新增：上下文开关
    public static final ForgeConfigSpec.ConfigValue<Integer> CONTEXT_LIMIT; // 新增：上下文消息数量
    
    // 新增：API类型配置
    public static final ForgeConfigSpec.ConfigValue<String> API_TYPE;
    // 新增：是否启用联网搜索
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_WEB_SEARCH;
    // 新增：是否启用深度思考
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_DEEP_THINKING;
    // 新增：是否显示思考过程
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_THOUGHT_PROCESS;
    
    static {
        BUILDER.push("AIChatMod Config");
        
        // 修改：API类型选择描述
        API_TYPE = BUILDER.comment(
            "API类型选择",
            "支持: deepseek, spark (讯飞星火), openai"
        ).define("apiType", "deepseek");
        
        // 是否启用消息前缀检测
        ENABLE_PREFIX = BUILDER.comment(
            "是否启用消息前缀检测（默认开启）",
            "开启时只处理以'#'开头的消息，关闭时处理所有非命令消息" //
        ).define("enablePrefix", true); //
        
        API_URL = BUILDER.comment("API 请求地址（默认是Deepseek）",
                "示例: https://api.deepseek.com/v1/chat/completions")
                .define("apiUrl", "https://api.deepseek.com/v1/chat/completions");
        API_KEY = BUILDER.comment("APIKEY (请勿将此条目透露给他人)")
                .define("apiKey", "");
        TIMEOUT = BUILDER.comment("API请求超时时间(单位：秒,范围5~120)")
                .defineInRange("timeout", 30, 5, 120);
        PRESET_PROMPT = BUILDER.comment(
            "System提示词(将在每次请求中强制添加在最前)",
            "System提示词独立于用户聊天历史之外"
        ).define("presetPrompt", ""); // 硬编预置提示词配置
        CHAT_DISPLAY_NAME = BUILDER.comment(
            "AI在聊天界面中显示的聊天名称",
            "这个名字会出现在AI消息之前"
        ).define("chatDisplayName", "AI");
        MODEL_NAME = BUILDER.comment(
            "API请求使用模型的名称",
            "指定使用哪个模型"
        ).define("modelName", "");
        
        // 修改：联网搜索开关描述
        ENABLE_WEB_SEARCH = BUILDER.comment(
            "是否启用联网搜索（支持此功能的API）",
            "启用后AI可以访问实时网络信息"
        ).define("enableWebSearch", false);
        
        // 修改：深度思考开关描述
        ENABLE_DEEP_THINKING = BUILDER.comment(
            "是否启用深度思考（支持此功能的API）",
            "启用后AI会进行更深入的推理"
        ).define("enableDeepThinking", false);
        
        // 新增：是否显示思考过程
        SHOW_THOUGHT_PROCESS = BUILDER.comment(
            "是否显示思考过程",
            "启用后会显示AI的推理过程"
        ).define("showThoughtProcess", false);
        
        // 新增：上下文功能配置
        ENABLE_CONTEXT = BUILDER.comment(
            "是否启用上下文功能",
            "启用后AI将记住之前的对话内容"
        ).define("enableContext", true);
        
        CONTEXT_LIMIT = BUILDER.comment(
            "上下文消息数量限制",
            "设置AI能记住的对话消息数量（1-20）"
        ).defineInRange("contextLimit", 5, 1, 20);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}