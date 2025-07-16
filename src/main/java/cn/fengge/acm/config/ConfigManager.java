package cn.fengge.acm.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigManager {
    // 添加日志记录器（修复LOGGER无法解析问题）
    private static final Logger LOGGER = LogManager.getLogger("aichatmod");
    
    public static final ForgeConfigSpec CLIENT_CONFIG;
    
    static {
        CLIENT_CONFIG = ModConfig.SPEC;
    }
    
    public static void setup() {
        ModLoadingContext.get().registerConfig(Type.CLIENT, CLIENT_CONFIG);
    }
    
    public static String getApiUrl() {
        return ModConfig.API_URL.get();
    }
    
    public static String getApiKey() {
        return ModConfig.API_KEY.get();
    }
    
    public static int getTimeout() {
        return ModConfig.TIMEOUT.get();
    }
    
    public static String getPresetPrompt() {
        return ModConfig.PRESET_PROMPT.get();
    }
    
    // 新增：获取聊天栏显示名称
    public static String getChatDisplayName() {
        return ModConfig.CHAT_DISPLAY_NAME.get();
    }
    
    // 新增：获取模型名称
    public static String getModelName() {
        return ModConfig.MODEL_NAME.get();
    }
    
    // 新增：获取是否启用前缀检测
    public static boolean isPrefixEnabled() {
        return ModConfig.ENABLE_PREFIX.get();
    }
    
    // 新增：获取是否启用上下文
    public static boolean isContextEnabled() {
        return ModConfig.ENABLE_CONTEXT.get();
    }
    
    // 新增：获取上下文消息数量
    public static int getContextLimit() {
        return ModConfig.CONTEXT_LIMIT.get();
    }
    
    // 新增：获取API类型
    public static String getApiType() {
        return ModConfig.API_TYPE.get();
    }
    
    // 新增：获取是否启用联网搜索
    public static boolean isWebSearchEnabled() {
        return ModConfig.ENABLE_WEB_SEARCH.get();
    }
    
    // 新增：获取是否启用深度思考
    public static boolean isDeepThinkingEnabled() {
        return ModConfig.ENABLE_DEEP_THINKING.get();
    }
    
    // 新增：获取是否显示思考过程
    public static boolean isShowThoughtProcess() {
        return ModConfig.SHOW_THOUGHT_PROCESS.get();
    }
    
    // 修改：保存配置的方法，添加模型名称参数
    public static void saveConfig(String modelName, String apiUrl, String apiKey, int timeout, String chatDisplayName) {
        // 添加配置值验证
        if (apiUrl == null || apiKey == null || chatDisplayName == null || modelName == null) {
            LOGGER.error("配置值为空，保存失败");
            return;
        }

        // 设置模型名称
        ModConfig.MODEL_NAME.set(modelName.trim());
        ModConfig.API_URL.set(apiUrl.trim());
        ModConfig.API_KEY.set(apiKey.trim());
        ModConfig.TIMEOUT.set(timeout);
        ModConfig.CHAT_DISPLAY_NAME.set(chatDisplayName.trim());

        try {
            ModConfig.SPEC.save();

            // 统一提示信息为中文
            LOGGER.info("配置保存成功"); 
        } catch (Exception e) {
            LOGGER.error("配置保存失败: " + e.getMessage(), e);
            throw new RuntimeException("配置保存失败: " + e.getMessage(), e);
        }
    }
    
    // 修改：保存配置的方法，增加enablePrefix参数
    public static void saveConfig(String modelName, String apiUrl, String apiKey, int timeout, String chatDisplayName, boolean enablePrefix) {
        // 添加配置值验证
        if (apiUrl == null || apiKey == null || chatDisplayName == null || modelName == null) {
            LOGGER.error("配置值为空，保存失败");
            return;
        }

        // 设置模型名称
        ModConfig.MODEL_NAME.set(modelName.trim());
        ModConfig.API_URL.set(apiUrl.trim());
        ModConfig.API_KEY.set(apiKey.trim());
        ModConfig.TIMEOUT.set(timeout);
        ModConfig.CHAT_DISPLAY_NAME.set(chatDisplayName.trim());
        
        // 设置是否启用前缀
        ModConfig.ENABLE_PREFIX.set(enablePrefix);

        try {
            ModConfig.SPEC.save();

            // 统一提示信息为中文
            LOGGER.info("配置保存成功"); 
        } catch (Exception e) {
            LOGGER.error("配置保存失败: " + e.getMessage(), e);
            throw new RuntimeException("配置保存失败: " + e.getMessage(), e);
        }
    }
    
    // 修改：保存配置的方法，添加上下文相关参数
    public static void saveConfig(String modelName, String apiUrl, String apiKey, int timeout, 
                                  String chatDisplayName, boolean enablePrefix, 
                                  boolean enableContext, int contextLimit) {
        // 添加配置值验证
        if (apiUrl == null || apiKey == null || chatDisplayName == null || modelName == null) {
            LOGGER.error("配置值为空，保存失败");
            return;
        }

        // 设置模型名称
        ModConfig.MODEL_NAME.set(modelName.trim());
        ModConfig.API_URL.set(apiUrl.trim());
        ModConfig.API_KEY.set(apiKey.trim());
        ModConfig.TIMEOUT.set(timeout);
        ModConfig.CHAT_DISPLAY_NAME.set(chatDisplayName.trim());
        
        // 设置是否启用前缀
        ModConfig.ENABLE_PREFIX.set(enablePrefix);
        
        // 设置是否启用上下文
        ModConfig.ENABLE_CONTEXT.set(enableContext);
        ModConfig.CONTEXT_LIMIT.set(contextLimit);

        try {
            ModConfig.SPEC.save();
            LOGGER.info("配置保存成功"); 
        } catch (Exception e) {
            LOGGER.error("配置保存失败: " + e.getMessage(), e);
            throw new RuntimeException("配置保存失败: " + e.getMessage(), e);
        }
    }
    
    // 修改：保存配置方法，支持新API类型
    public static void saveConfig(String modelName, String apiUrl, String apiKey, int timeout, 
                                  String chatDisplayName, boolean enablePrefix, 
                                  boolean enableContext, int contextLimit,
                                  String apiType, boolean enableWebSearch, 
                                  boolean enableDeepThinking, boolean showThoughtProcess) {
        // 添加配置值验证
        if (apiUrl == null || apiKey == null || chatDisplayName == null || modelName == null) {
            LOGGER.error("配置值为空，保存失败");
            return;
        }

        // 设置模型名称
        ModConfig.MODEL_NAME.set(modelName.trim());
        ModConfig.API_URL.set(apiUrl.trim());
        ModConfig.API_KEY.set(apiKey.trim());
        ModConfig.TIMEOUT.set(timeout);
        ModConfig.CHAT_DISPLAY_NAME.set(chatDisplayName.trim());
        
        // 设置是否启用前缀
        ModConfig.ENABLE_PREFIX.set(enablePrefix);
        
        // 设置是否启用上下文
        ModConfig.ENABLE_CONTEXT.set(enableContext);
        ModConfig.CONTEXT_LIMIT.set(contextLimit);
        
        // 设置新增配置
        ModConfig.API_TYPE.set(apiType);
        ModConfig.ENABLE_WEB_SEARCH.set(enableWebSearch);
        ModConfig.ENABLE_DEEP_THINKING.set(enableDeepThinking);
        ModConfig.SHOW_THOUGHT_PROCESS.set(showThoughtProcess);

        try {
            ModConfig.SPEC.save();
            LOGGER.info("配置保存成功"); 
        } catch (Exception e) {
            LOGGER.error("配置保存失败: " + e.getMessage(), e);
            throw new RuntimeException("配置保存失败: " + e.getMessage(), e);
        }
    }
    
    // 新增：单独设置消息前缀检测开关
    public static void setEnablePrefix(boolean enabled) {
        ModConfig.ENABLE_PREFIX.set(enabled);
        try {
            ModConfig.SPEC.save();
        } catch (Exception e) {
            LOGGER.error("消息前缀检测开关保存失败", e);
        }
    }
    
    // 新增：单独设置上下文开关
    public static void setContextEnabled(boolean enabled) {
        ModConfig.ENABLE_CONTEXT.set(enabled);
        try {
            ModConfig.SPEC.save();
        } catch (Exception e) {
            LOGGER.error("配置变动保存失败", e);
        }
    }
    
    // 新增：单独设置上下文数量
    public static void setContextLimit(int limit) {
        ModConfig.CONTEXT_LIMIT.set(limit);
        try {
            ModConfig.SPEC.save();
        } catch (Exception e) {
            LOGGER.error("配置变动保存失败", e);
        }
    }
}