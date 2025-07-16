package cn.fengge.acm.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    // 会话缓存配置项
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_HISTORY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PERSIST_HISTORY;
    
    static {
        BUILDER.push("AIChatMod Config");
        
        // 保留现有配置项...
        PRESET_PROMPT = BUILDER.comment(
            "System prompt for AI (will be enforced in every request)",
            "This prompt will be added as system role and NOT counted in context messages"
        ).define("presetPrompt", "You are a helpful assistant in Minecraft.");
        
        // 会话缓存配置
        MAX_HISTORY = BUILDER.comment(
            "Maximum number of conversation history entries to keep",
            "Set to 0 to disable history persistence"
        ).defineInRange("maxHistory", 20, 0, 100);
        
        PERSIST_HISTORY = BUILDER.comment(
            "Whether to persist conversation history between sessions"
        ).define("persistHistory", true);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}